package com.anner.llm.rag;

import java.util.ArrayList;
import java.util.List;

import com.anner.llm.common.Assistant;
import static com.anner.llm.common.Utils.startConversationWith;
import com.anner.llm.model.ModelManager;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class WebURLRagDemo {

    private static List<Document> loadDocuments(List<String> urls) {
        List<Document> documents = new ArrayList<>();
        for (String url : urls) {
            try {
                Document document = UrlDocumentLoader.load(url, new TextDocumentParser());
                documents.add(document);
            } catch (Exception e) {
                System.err.println("Failed to load document from URL: " + url);
                e.printStackTrace();
            }
        }
        return documents;
    }

    public static void main(String[] args) {
        ChatLanguageModel model = ModelManager.doubao();

        List<String> urls = List.of(
                "https://help.fanruan.com/finereport/doc-view-4244.html",
                "https://help.fanruan.com/finereport/doc-view-253.html",
                "https://help.fanruan.com/finereport/doc-view-249.html",
                "https://help.fanruan.com/finereport/doc-view-252.html",
                "https://help.fanruan.com/finereport/doc-view-3930.html",
                "https://help.fanruan.com/finereport/doc-view-831.html",
                "https://help.fanruan.com/finereport/doc-view-869.html",
                "https://help.fanruan.com/finereport/doc-view-867.html",
                "https://help.fanruan.com/finereport/doc-view-827.html",
                "https://help.fanruan.com/finereport/doc-view-840.html",
                "https://help.fanruan.com/finereport/doc-view-3598.html",
                "https://help.fanruan.com/finereport/doc-view-824.html",
                "https://help.fanruan.com/finereport/doc-view-1897.html",
                "https://help.fanruan.com/finereport/doc-view-880.html",
                "https://help.fanruan.com/finereport/doc-view-4456.html",
                "https://help.fanruan.com/finereport/doc-view-843.html",
                "https://help.fanruan.com/finereport/doc-view-1903.html",
                "https://help.fanruan.com/finereport/doc-view-853.html",
                "https://help.fanruan.com/finereport/doc-view-846.html",
                "https://help.fanruan.com/finereport/doc-view-839.html",
                "https://help.fanruan.com/finereport/doc-view-3637.html",
                "https://help.fanruan.com/finereport/doc-view-810.html",
                "https://help.fanruan.com/finereport/doc-view-854.html",
                "https://help.fanruan.com/finereport/doc-view-842.html",
                "https://help.fanruan.com/finereport/doc-view-841.html",
                "https://help.fanruan.com/finereport/doc-view-838.html",
                "https://help.fanruan.com/finereport/doc-view-850.html",
                "https://help.fanruan.com/finereport/doc-view-802.html",
                "https://help.fanruan.com/finereport/doc-view-4583.html",
                "https://help.fanruan.com/finereport/doc-view-4580.html",
                "https://help.fanruan.com/finereport/doc-view-4581.html",
                "https://help.fanruan.com/finereport/doc-view-4582.html",
                "https://help.fanruan.com/finereport/doc-view-4585.html",
                "https://help.fanruan.com/finereport/doc-view-4584.html",
                "https://help.fanruan.com/finereport/doc-view-1493.html",
                "https://help.fanruan.com/finereport/doc-view-3624.html",
                "https://help.fanruan.com/finereport/doc-view-4133.html",
                "https://help.fanruan.com/finereport/doc-view-4454.html",
                "https://help.fanruan.com/finereport/doc-view-4710.html",
                "https://help.fanruan.com/finereport/doc-view-4732.html",
                "https://help.fanruan.com/finereport/doc-view-4869.html",
                "https://help.fanruan.com/finereport/doc-view-4870.html",
                "https://help.fanruan.com/finereport/doc-view-4871.html",
                "https://help.fanruan.com/finereport/doc-view-4872.html",
                "https://help.fanruan.com/finereport/doc-view-4873.html",
                "https://help.fanruan.com/finereport/doc-view-4874.html",
                "https://help.fanruan.com/finereport/doc-view-4875.html",
                "https://help.fanruan.com/finereport/doc-view-5040.html",
                "https://help.fanruan.com/finereport/doc-view-5096.html",
                "https://help.fanruan.com/finereport/doc-view-5456.html",
                "https://help.fanruan.com/finereport/doc-view-3373.html",
                "https://help.fanruan.com/finereport/doc-view-813.html#",
                "https://help.fanruan.com/finereport/doc-view-816.html#",
                "https://help.fanruan.com/finereport/doc-view-817.html#",
                "https://help.fanruan.com/finereport/doc-view-819.html#",
                "https://help.fanruan.com/finereport/doc-view-824.html#",
                "https://help.fanruan.com/finereport/doc-view-3598.html#",
                "https://help.fanruan.com/finereport/doc-view-827.html#",
                "https://help.fanruan.com/finereport/doc-view-1785.html",
                "https://help.fanruan.com/finereport/doc-view-810.html",
                "https://help.fanruan.com/finereport/doc-view-854.html",
                "https://help.fanruan.com/finereport/doc-view-842.html",
                "https://help.fanruan.com/finereport/doc-view-841.html",
                "https://help.fanruan.com/finereport/doc-view-838.html",
                // 层次坐标公式
                "https://help.fanruan.com/finereport/doc-view-4583.html",
                "https://help.fanruan.com/finereport/doc-view-4580.html",
                "https://help.fanruan.com/finereport/doc-view-4581.html",
                "https://help.fanruan.com/finereport/doc-view-4582.html",
                "https://help.fanruan.com/finereport/doc-view-4585.html",
                "https://help.fanruan.com/finereport/doc-view-4584.html",
                // 函数应用
                "https://help.fanruan.com/finereport/doc-view-2503.html",
                "https://help.fanruan.com/finereport/doc-view-3741.html",
                "https://help.fanruan.com/finereport/doc-view-3764.html",
                "https://help.fanruan.com/finereport/doc-view-3945.html",
                "https://help.fanruan.com/finereport/doc-view-4233.html",
                "https://help.fanruan.com/finereport/doc-view-2363.html",
                "https://help.fanruan.com/finereport/doc-view-2364.html",
                "https://help.fanruan.com/finereport/doc-view-2382.html",
                "https://help.fanruan.com/finereport/doc-view-2383.html",
                "https://help.fanruan.com/finereport/doc-view-2388.html",
                "https://help.fanruan.com/finereport/doc-view-3549.html",
                "https://help.fanruan.com/finereport/doc-view-5460.html",
                "https://help.fanruan.com/finereport/doc-view-878.html",
                "https://help.fanruan.com/finereport/doc-view-847.html");

        // 加载所有文档
        List<Document> documents = loadDocuments(urls);

        // 将文档分割成多个段落
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = new ArrayList<>();
        for (Document document : documents) {
            segments.addAll(splitter.split(document));
        }

        // 使用BgeSmallEnV15QuantizedEmbeddingModel生成嵌入向量
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 将段落和对应的嵌入向量存储在内存中
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, segments);

        // 创建内容检索器
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.5)
                .build();

        // 创建聊天记忆
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        // 创建助手
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .chatMemory(chatMemory)
                .build();

        startConversationWith(assistant);
    }
}
