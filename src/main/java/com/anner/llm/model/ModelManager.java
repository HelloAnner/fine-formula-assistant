package com.anner.llm.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ModelManager {
    public static ChatLanguageModel doubao() {
        return OpenAiChatModel.builder()
                .modelName(System.getenv("DOUBAO_MODEL_NAME"))
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .build();
    }
}
