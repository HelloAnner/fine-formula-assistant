package com.anner.llm.embed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anner.llm.common.AssistantConstants;
import com.anner.llm.embed.fetch.WebBrowserFetcher;
import com.anner.llm.model.EmbedModelManager;
import com.anner.llm.model.ModelManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Anner
 * @since 12.0
 *        Created on 2025/3/12
 */
@Slf4j
@Service
public class EmbedService {

    @Autowired
    private WebBrowserFetcher webBrowserFetcher;

    public EmbeddingStore<TextSegment> buildEmbeddingStore() throws IOException {
        // 存在则读取，不存在则创建
        Path filePath = Paths.get(AssistantConstants.EMBED_JSON_FILE_NAME);
        if (Files.exists(filePath)) {
            InMemoryEmbeddingStore<TextSegment> memoryChatMemoryStore = InMemoryEmbeddingStore.fromFile(filePath);
            log.info("load embeddingStore from file: {}", filePath);
            return memoryChatMemoryStore;
        }
        // 创建
        // 加载所有文档
        List<TextSegment> segments = loadDocuments();

        // 将文档分割成多个段落
        // DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        // List<TextSegment> segments = new ArrayList<>();
        // for (Document document : documents) {
        // segments.addAll(splitter.split(document));
        // }

        // EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
        // .baseUrl("http://localhost:11434")
        // .modelName("nomic-embed-text")
        // .build();

        // EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 使用大模型自己的embedding模型
        EmbeddingModel embeddingModel = EmbedModelManager.doubao();
        List<Embedding> embeddings = new ArrayList<>();

        // 将segments进行分批处理
        // for (int i = 0; i < segments.size(); i += AssistantConstants.BATCH_SIZE) {
        // int end = Math.min(i + AssistantConstants.BATCH_SIZE, segments.size());
        // embeddings.addAll(embeddingModel.embedAll(segments.subList(i,
        // end)).content());
        // log.info("embedding , {}/{}", i, segments.size());
        // }

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        for (int i = 0; i < segments.size(); i++) {
            TextSegment segment = segments.get(i);
            Embedding embedding = embeddingModel.embed(segment.text()).content();
            embeddingStore.add(embedding, segment);
        }

//        embeddingStore.addAll(embeddings, segments);
        embeddingStore.serializeToFile(AssistantConstants.EMBED_JSON_FILE_NAME);
        log.info("save embeddingStore to file: {}", AssistantConstants.EMBED_JSON_FILE_NAME);
        return embeddingStore;
    }

    public void fetchAndSave() throws Exception {
        webBrowserFetcher.setUrls(AssistantConstants.DOCUMENT_URLS);
        webBrowserFetcher.setSavePath("test");
        webBrowserFetcher.setChatLanguageModel(ModelManager.doubaoDeepseek());
        webBrowserFetcher.fetch();
    }

    private static List<TextSegment> loadDocuments() throws IOException {
        // 获取 test/docs 下面的全部json 文件
        File docsDir = new File("test/docs");
        File[] jsonFiles = docsDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null) {
            throw new RuntimeException("No JSON files found in test/docs");
        }

        List<TextSegment> textSegments = new ArrayList<>();
        for (File jsonFile : jsonFiles) {
            textSegments.addAll(loadJsonDocuments(jsonFile.getAbsolutePath(), 1000, 20));
        }

        return textSegments;
    }

    private static List<TextSegment> loadJsonDocuments(String resourcePath, int maxTokensPerChunk, int overlapTokens)
            throws IOException {

        InputStream inputStream = new FileInputStream(resourcePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<Document> batch = new ArrayList<>();

        String text = "";
        String line;
        while ((line = reader.readLine()) != null) {
            text += line;
        }

        JsonNode jsonNode = objectMapper.readTree(text);

        // 公式名称
        String title = jsonNode.path("公式名称").asText(null);
        // 公式描述
        String description = jsonNode.path("公式描述").asText(null);
        // 参数说明
        String parameters = jsonNode.path("参数说明").asText(null);
        // 使用示例
        String examples = jsonNode.path("使用示例").asText(null);
        // 特殊情况
        String specialCases = jsonNode.path("特殊情况").asText(null);

        text = (title != null ? title + "\n\n" + description : description)
                + "\n\n" + parameters + "\n\n" + examples + "\n\n" + specialCases;

        Metadata metadata = new Metadata();
        metadata.put("公式名称", title);
        metadata.put("公式描述", description);
        metadata.put("参数说明", parameters);
        metadata.put("使用示例", examples);
        metadata.put("特殊情况", specialCases);

        Document document = Document.from(text, metadata);
        batch.add(document);

        return new ArrayList<>(splitIntoChunks(batch, maxTokensPerChunk, overlapTokens));
    }

    private static List<TextSegment> splitIntoChunks(List<Document> documents, int maxTokensPerChunk,
            int overlapTokens) {
        OpenAiTokenizer tokenizer = new OpenAiTokenizer(OpenAiEmbeddingModelName.TEXT_EMBEDDING_ADA_002);

        DocumentSplitter splitter = DocumentSplitters.recursive(
                maxTokensPerChunk,
                overlapTokens,
                tokenizer);

        List<TextSegment> allSegments = new ArrayList<>();
        for (Document document : documents) {
            List<TextSegment> segments = splitter.split(document);
            allSegments.addAll(segments);
        }

        return allSegments;
    }
}
