package com.bobby.aicodeapp.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Description("生成HTML代码文件的结果")
@Data
public class HtmlCodeResult {

    @Description("HTML代码")
    private String htmlCode;

    @Description("生成代码的描述")
    private String description;
}
