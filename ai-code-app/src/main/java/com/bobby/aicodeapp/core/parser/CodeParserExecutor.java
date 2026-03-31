package com.bobby.aicodeapp.core.parser;

import com.bobby.aicode.exception.BusinessException;
import com.bobby.aicode.exception.ErrorCode;
import com.bobby.aicode.model.enums.CodeGenTypeEnum;

public class CodeParserExecutor {
    public static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();
    public static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    public static Object executorParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        };
    }
}
