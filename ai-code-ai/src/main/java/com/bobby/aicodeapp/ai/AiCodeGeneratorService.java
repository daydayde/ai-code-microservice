package com.bobby.aicodeapp.ai;

import com.bobby.aicodeapp.ai.model.HtmlCodeResult;
import com.bobby.aicodeapp.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface AiCodeGeneratorService {
    /**
     * 生成单文件代码
     *
     * @param inputFile 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/htmlSystemPrompt.txt")
    HtmlCodeResult generateHTMLCode(String inputFile);

    /**
     * 生成多文件代码
     *
     * @param inputFile 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/multiFileSystemPrompt.txt")
    MultiFileCodeResult generateMultiFileCode(String inputFile);

    /**
     * 生成单文件代码
     *
     * @param inputFile 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/htmlSystemPrompt.txt")
    Flux<String> generateHTMLCodeStream(String inputFile);

    /**
     * 生成多文件代码
     *
     * @param inputFile 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/multiFileSystemPrompt.txt")
    Flux<String> generateMultiFileCodeStream(String inputFile);

    /**
     * 生成 Vue 项目代码（流式）
     *
     * @param userMessage 用户消息
     * @return 生成过程的流式响应
     */
    @SystemMessage(fromResource = "prompt/vueProjectSystemPrompt.txt")
    TokenStream generateVueProjectCodeStream(@MemoryId long appId, @UserMessage String userMessage);

}
