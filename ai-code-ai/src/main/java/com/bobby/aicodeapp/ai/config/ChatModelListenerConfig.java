package com.bobby.aicodeapp.ai.config;


import com.bobby.aicodeapp.ai.monitor.AiModelMetricsCollector;
import com.bobby.aicodeapp.ai.monitor.MonitorContext;
import com.bobby.aicodeapp.ai.monitor.MonitorContextHolder;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Configuration
@Slf4j
public class ChatModelListenerConfig {
    private static final String REQUEST_START_TIME_KEY = "request_start_time";
    private static final String MONITOR_CONTEXT_KEY = "monitor_context";

    @Resource
    private AiModelMetricsCollector aiModelMetricsCollector;

    /**
     * 记录响应时间
     */
    private void recordResponseTime(Map<Object, Object> attributes, String userId, String appId, String modelName) {
        Instant startTime = (Instant) attributes.get(REQUEST_START_TIME_KEY);
        Duration responseTime = Duration.between(startTime, Instant.now());
        aiModelMetricsCollector.recordResponseTime(userId, appId, modelName, responseTime);
    }

    /**
     * 记录Token使用情况
     */
    private void recordTokenUsage(ChatModelResponseContext responseContext, String userId, String appId, String modelName) {
        TokenUsage tokenUsage = responseContext.chatResponse().metadata().tokenUsage();
        if (tokenUsage != null) {
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "input", tokenUsage.inputTokenCount());
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "output", tokenUsage.outputTokenCount());
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "total", tokenUsage.totalTokenCount());
        }
    }

    @Bean
    public ChatModelListener modelListener() {

        return new ChatModelListener() {
            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                // 记录请求开始时间
                requestContext.attributes().put(REQUEST_START_TIME_KEY, Instant.now());
                // 从监控上下文中获取信息
                MonitorContext context = MonitorContextHolder.getContext();
                String userId = context.getUserId();
                String appId = context.getAppId();
                requestContext.attributes().put(MONITOR_CONTEXT_KEY, context);
                // 获取模型名称
                String modelName = requestContext.chatRequest().modelName();
                // 记录请求指标
                aiModelMetricsCollector.recordRequest(userId, appId, modelName, "started");
                log.info("模型请求信息: {}", requestContext.chatRequest());
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                // 从属性中获取监控信息（由 onRequest 方法存储）
                Map<Object, Object> attributes = responseContext.attributes();
                // 从监控上下文中获取信息
                MonitorContext context = (MonitorContext) attributes.get(MONITOR_CONTEXT_KEY);
                String userId = context.getUserId();
                String appId = context.getAppId();
                // 获取模型名称
                String modelName = responseContext.chatResponse().modelName();
                // 记录成功请求
                aiModelMetricsCollector.recordRequest(userId, appId, modelName, "success");
                // 记录响应时间
                recordResponseTime(attributes, userId, appId, modelName);
                // 记录 Token 使用情况
                recordTokenUsage(responseContext, userId, appId, modelName);
                log.info("模型回复信息: {}", responseContext.chatResponse());
            }

            @Override
            public void onError(ChatModelErrorContext errorContext) {
                // 从监控上下文中获取信息
                MonitorContext context = MonitorContextHolder.getContext();
                String userId = context.getUserId();
                String appId = context.getAppId();
                // 获取模型名称和错误类型
                String modelName = errorContext.chatRequest().modelName();
                String errorMessage = errorContext.error().getMessage();
                // 记录失败请求
                aiModelMetricsCollector.recordRequest(userId, appId, modelName, "error");
                aiModelMetricsCollector.recordError(userId, appId, modelName, errorMessage);
                // 记录响应时间（即使是错误响应）
                Map<Object, Object> attributes = errorContext.attributes();
                recordResponseTime(attributes, userId, appId, modelName);
                log.error("错误信息: {}", errorContext.error().getMessage());
            }
        };
    }
}