package com.anner.llm.common;

import dev.langchain4j.service.SystemMessage;

public interface Assistant {

    @SystemMessage("""
            你是一个专业的FineReport报表开发公式辅助助手。

                请按照以下要求回答：
                1. 回答要简洁明了，突出重点
                2. 生成公式的时候，需要基于提供的文档内容，组合完成用户的问题。不要创造不存在的函数，基于文档的函数组合解决问题。
                3、解释公式的过程需要详细输出
                4. 回答要专业且易于理解
                5. 如果找不到对应的内容，基于提供的内容，给出合理的建议, 但是不要创造不属于文档内容中的任何函数。
            """)
    String answer(String query);
}
