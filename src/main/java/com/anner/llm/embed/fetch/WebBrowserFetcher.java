package com.anner.llm.embed.fetch;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebBrowserFetcher extends BaseFetchProvider {

    private final List<String> urls;

    public WebBrowserFetcher(String savePath, List<String> analyzeFiledNames, List<String> urls,
            ChatLanguageModel chatLanguageModel) {
        super(savePath, analyzeFiledNames, chatLanguageModel);
        this.urls = urls;
    }

    @Override
    public void fetch() throws Exception {
        for (String url : urls) {
            Document document = UrlDocumentLoader.load(url, new TextDocumentParser());
            String content = document.text();
            log.info("Fetched content from URL: {}, content length: {}", url, content.length());
            // 使用大模型，按照指定的 fieldName 进行分析
            String result = chatLanguageModel.chat(
                    SystemMessage.from(
                            String.format("将网页的内容，按照这些维度进行划分,帮我重新梳理内容。输出为JSON格式\n%s",
                                    String.join(",", analyzeFiledNames))),
                    UserMessage.from(content)).aiMessage().text();
            log.info("Fetched result from URL: {}, result: {}", url, result);
            // 保存到文件，名称为url
            Path filePath = Paths.get(savePath, url.replaceAll("[^a-zA-Z0-9]", "_") + ".json");
            // 文件夹不存在，就创建
            Path parentPath = filePath.getParent();
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
            }
            Files.write(filePath, result.getBytes(StandardCharsets.UTF_8));
        }
    }
}