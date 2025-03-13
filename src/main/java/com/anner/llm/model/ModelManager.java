package com.anner.llm.model;

import com.anner.llm.common.AssistantConstants;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ModelManager {
    public static ChatLanguageModel doubaoPro32K() {
        return OpenAiChatModel.builder()
                .modelName("doubao-1-5-pro-32k-250115")
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .maxRetries(AssistantConstants.MAX_RETRIES)
                .maxTokens(AssistantConstants.MAX_TOKENS)
                .temperature(AssistantConstants.TEMPERATURE)
                .topP(AssistantConstants.TOP_P)
                .build();
    }

    public static ChatLanguageModel doubaoLite32K() {
        return OpenAiChatModel.builder()
                .modelName("doubao-1-5-lite-32k-250115")
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .maxRetries(AssistantConstants.MAX_RETRIES)
                // 12288
                // .maxTokens(AssistantConstants.MAX_TOKENS)
                .temperature(AssistantConstants.TEMPERATURE)
                .topP(AssistantConstants.TOP_P)
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
