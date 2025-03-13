package com.anner.llm.embed.fetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class FormulaStructure {

    @Description("公式名称")
    private String formulaName;

    @Description("公式描述")
    private String formulaDescription;

    @Description("公式使用场景")
    private String formulaUsageScenario;

    @Description("公式使用示例")
    private String formulaUsageExample;

    @Description("公式使用详细说明")
    private String formulaUsageDetail;

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(this);
    }

}
