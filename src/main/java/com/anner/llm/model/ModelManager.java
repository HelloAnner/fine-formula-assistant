package com.anner.llm.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ModelManager {
    public static ChatLanguageModel doubao() {
        return OpenAiChatModel.builder()
                .modelName("doubao-1-5-pro-32k-250115")
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .build();
    }

    public static ChatLanguageModel kimi() {
        return OpenAiChatModel.builder()
                .modelName("moonshot-v1-8k")
                .baseUrl(System.getenv("KIMI_BASE_URL"))
                .apiKey(System.getenv("KIMI_API_KEY"))
                .build();
    }
}
