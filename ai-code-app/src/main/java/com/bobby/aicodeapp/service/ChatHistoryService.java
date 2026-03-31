package com.bobby.aicodeapp.service;

import com.bobby.aicode.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.bobby.aicode.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.bobby.aicode.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/daydayde">Bobby</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * @param appId       应用Id
     * @param message     消息
     * @param messageType 消息类型
     * @param userId      用户id
     * @return
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * @param appId 应用ID
     * @return
     */
    boolean deleteByAppId(Long appId);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);
}
