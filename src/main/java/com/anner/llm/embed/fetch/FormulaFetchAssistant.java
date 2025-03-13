package com.anner.llm.embed.fetch;

import dev.langchain4j.service.SystemMessage;

/**
 * 
 * @author Anner
 */
public interface FormulaFetchAssistant {

    /**
     * 
     * @param content
     * @return
     * @throws Exception
     */
    @SystemMessage("""
            将提供的网页内容或者文本内容，按照下面指定的维度进行划分,帮我重新梳理内容。网页内容尽可能全部按照格式梳理到JSON中。输出为JSON格式,不要输出多余内容.
            如果存在多个公式，请按照公式名称进行分类，每个公式一个JSON对象。
            要求展示提供网页内容或者文本内容中的全部的公式，不要遗漏。每一个网页存在的全部公式都需要获取和分析。
            一次性输出全部的内容，不要中断。
            分析的维度为:
            1. 公式名称
            2. 公式内容
            3. 公式描述
            4. 公式使用场景
            5. 公式使用示例
            """)
    String fetch(String content) throws Exception;
}
