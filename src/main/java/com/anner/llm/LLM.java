package com.anner.llm;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LLM {
    public static void main(String[] args) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .modelName(System.getenv("DOUBAO_MODEL_NAME"))
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .build();

        System.out.println(model.chat("你好，介绍一下自己的模型"));
    }
}
