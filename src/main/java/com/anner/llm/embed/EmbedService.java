package com.anner.llm.embed;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.anner.llm.common.AssistantConstants;
import com.anner.llm.model.EmbedModelManager;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Anner
 * @since 12.0
 *        Created on 2025/3/12
 */
@Slf4j
public class EmbedService {

    private final static class Holder {
        private static final EmbedService INSTANCE = new EmbedService();
    }

    public static EmbedService getInstance() {
        return Holder.INSTANCE;
    }

    public EmbeddingStore<TextSegment> buildEmbeddingStore() {
        // 存在则读取，不存在则创建
        Path filePath = Paths.get(AssistantConstants.EMBED_JSON_FILE_NAME);
        if (Files.exists(filePath)) {
            InMemoryEmbeddingStore<TextSegment> memoryChatMemoryStore = InMemoryEmbeddingStore.fromFile(filePath);
            log.info("load embeddingStore from file: {}", filePath);
            return memoryChatMemoryStore;
        }
        // 创建
        // 加载所有文档
        List<Document> documents = loadDocuments(AssistantConstants.DOCUMENT_URLS);

        // 将文档分割成多个段落
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 30);
        List<TextSegment> segments = new ArrayList<>();
        for (Document document : documents) {
            segments.addAll(splitter.split(document));
        }

        // 使用大模型自己的embedding模型
        EmbeddingModel embeddingModel = EmbedModelManager.doubao();
        List<Embedding> embeddings = new ArrayList<>();

        // 将segments进行分批处理
        for (int i = 0; i < segments.size(); i += AssistantConstants.BATCH_SIZE) {
            int end = Math.min(i + AssistantConstants.BATCH_SIZE, segments.size());
            embeddings.addAll(embeddingModel.embedAll(segments.subList(i, end)).content());
            log.info("embedding , {}/{}", i, segments.size());
        }

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);
        embeddingStore.serializeToFile(AssistantConstants.EMBED_JSON_FILE_NAME);
        log.info("save embeddingStore to file: {}", AssistantConstants.EMBED_JSON_FILE_NAME);
        return embeddingStore;
    }

    private static List<Document> loadDocuments(List<String> urls) {
        List<Document> documents = new ArrayList<>();
        for (String url : urls) {
            try {
                Document document = UrlDocumentLoader.load(url, new TextDocumentParser());
                documents.add(document);
            } catch (Exception e) {
                log.error("Failed to load document from URL: " + url, e);
            }
        }
        return documents;
    }

}
