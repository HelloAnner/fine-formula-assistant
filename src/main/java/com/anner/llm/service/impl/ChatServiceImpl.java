package com.anner.llm.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.anner.llm.common.Assistant;
import com.anner.llm.dto.ChatRequest;
import com.anner.llm.dto.ChatResponse;
import com.anner.llm.embed.EmbedService;
import com.anner.llm.model.EmbedModelManager;
import com.anner.llm.model.ModelManager;
import com.anner.llm.service.ChatService;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatLanguageModel model;
    private final Map<String, ChatMemory> sessionMemories;
    private final ContentRetriever contentRetriever;

    public ChatServiceImpl() {
        this.model = ModelManager.doubao();
        this.sessionMemories = new ConcurrentHashMap<>();
        this.contentRetriever = initializeContentRetriever();
    }

    private ContentRetriever initializeContentRetriever() {
        EmbeddingStore<TextSegment> embeddingStore = EmbedService.getInstance().buildEmbeddingStore();
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
                .contentRetriever(contentRetriever)
                .chatMemory(memory)
                .build();
    }

    @Override
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