package com.anner.llm.tool;


import dev.langchain4j.agent.tool.Tool;

/**
 * @author Anner
 * @since 12.0
 * Created on 2025/3/19
 */
public class MathCalculateTool {

    @Tool("检查公式是否合法")
    boolean checkFormulaValid1(String formula) {
        return false;
    }
}
