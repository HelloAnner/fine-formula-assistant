package com.anner.llm.embed.fetch;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import dev.ai4j.openai4j.OpenAiHttpException;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Setter
@NoArgsConstructor
public class WebBrowserFetcher extends BaseFetchProvider {

    private List<String> urls;

    public WebBrowserFetcher(String savePath, List<String> urls, ChatLanguageModel chatLanguageModel) {
        super(savePath, chatLanguageModel);
        this.urls = urls;
    }

    @Override
    public void fetch() throws Exception {
        log.info("Starting to fetch URLs: {}", urls);
        for (String url : urls) {
            try {
                fetchWithRetry(url);
            } catch (Exception e) {
                log.error("Failed to fetch URL after all retries: {}", url, e);
                throw e;
            }
        }
    }

    @Retryable(value = {
            Exception.class }, maxAttempts = 10, backoff = @Backoff(delay = 2000, multiplier = 2, random = true))
    private void fetchWithRetry(String url) throws Exception {
        try {
            log.info("Attempting to fetch URL: {}", url);
            String formulaStructure = AiServices.create(FormulaFetchAssistant.class,
                    chatLanguageModel)
                    .fetch(url);
            log.info("Successfully fetched result from URL: {}", url);

            // 保存结果
            Path filePath = Paths.get(savePath, url.replaceAll("[^a-zA-Z0-9]", "_") + ".json");
            Path parentPath = filePath.getParent();
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
            }
            Files.write(filePath, formulaStructure.getBytes(StandardCharsets.UTF_8));
            log.info("Successfully saved result to file: {}", filePath);

        } catch (OpenAiHttpException e) {
            log.warn("Rate limit reached for URL: {}, will retry. Error: {}", url, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching URL: {}, error: {}", url, e.getMessage());
            throw e;
        }
    }
}