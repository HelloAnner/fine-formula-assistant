package com.anner.llm.model;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;

public class EmbedModelManager {

    public final static EmbeddingModel doubao() {
        return OpenAiEmbeddingModel.builder()
                .modelName("doubao-embedding-text-240715")
                .apiKey(System.getenv("DOUBAO_API_KEY"))
                .baseUrl(System.getenv("DOUBAO_BASE_URL"))
                .build();
    }
}
