package com.anner.llm.service.chat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.anner.llm.tool.MathCalculateTool;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anner.llm.common.Assistant;
import com.anner.llm.dto.ChatRequest;
import com.anner.llm.dto.ChatResponse;
import com.anner.llm.embed.EmbedService;
import com.anner.llm.model.EmbedModelManager;
import com.anner.llm.model.ModelManager;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl {

    private ChatLanguageModel model;
    private Map<String, ChatMemory> sessionMemories;
    private ContentRetriever contentRetriever;

    @Autowired
    private EmbedService embedService;

    @PostConstruct
    public void init() {
        this.model = ModelManager.doubaoPro32K();
        this.sessionMemories = new ConcurrentHashMap<>();
        this.contentRetriever = initializeContentRetriever();
    }

    private ContentRetriever initializeContentRetriever() {
        EmbeddingStore<TextSegment> embeddingStore;
        try {
            embeddingStore = embedService.buildEmbeddingStore();
        } catch (IOException e) {
            throw new RuntimeException("build embeddingStore failed", e);
        }
        // EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
        // .baseUrl("http://localhost:11434")
        // .modelName("nomic-embed-text")
        // .build();

        EmbeddingModel embeddingModel = EmbedModelManager.doubao();

        // 创建内容检索器
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.5)
                .build();
    }

    private Assistant initializeAssistant(ChatMemory memory) {
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .tools(new MathCalculateTool())
                .contentRetriever(contentRetriever)
//                .retrievalAugmentor()
                .chatMemory(memory)
                .build();
    }

    public ChatResponse chat(ChatRequest request) {
        try {
            ChatMemory memory = getOrCreateMemory(request.getSessionId());
            Assistant assistant = initializeAssistant(memory);
            String response = assistant.answer(request.getMessage());

            return ChatResponse.builder()
                    .message(response)
                    .sessionId(request.getSessionId())
                    .success(true)
                    .build();
        } catch (Exception e) {
            log.error("Chat error", e);
            return ChatResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .sessionId(request.getSessionId())
                    .build();
        }
    }

    private ChatMemory getOrCreateMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId,
                key -> MessageWindowChatMemory.withMaxMessages(10));
    }
}