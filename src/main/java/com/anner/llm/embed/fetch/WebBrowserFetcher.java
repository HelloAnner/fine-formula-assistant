package com.anner.llm.embed.fetch;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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

    private static final int MAX_RETRIES = 10;
    private static final long INITIAL_DELAY_MS = 2000;
    private static final double BACKOFF_MULTIPLIER = 2.0;
    private static final double RANDOM_FACTOR = 0.5;

    private List<String> urls;
    private final Random random = new Random();

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

    private void fetchWithRetry(String url) throws Exception {
        int attempt = 0;
        long delay = INITIAL_DELAY_MS;

        while (attempt < MAX_RETRIES) {
            try {
                attempt++;
                log.info("Attempt {}/{} to fetch URL: {}", attempt, MAX_RETRIES, url);

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

                return; // 成功完成，退出重试循环

            } catch (OpenAiHttpException e) {
                if (e.getMessage() != null && e.getMessage().contains("rate_limit_reached_error")) {
                    log.warn("Rate limit reached for URL: {}, attempt {}/{}. Error: {}",
                            url, attempt, MAX_RETRIES, e.getMessage());

                    if (attempt == MAX_RETRIES) {
                        log.error("Max retries reached for URL: {}", url);
                        throw e;
                    }

                    // 计算下一次重试的延迟时间
                    delay = calculateNextDelay(delay);
                    log.info("Waiting {} ms before next retry for URL: {}", delay, url);
                    try {
                        TimeUnit.MILLISECONDS.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                } else {
                    // 其他类型的 OpenAiHttpException，直接抛出
                    log.error("OpenAI API error for URL: {}, error: {}", url, e.getMessage());
                    throw e;
                }
            } catch (Exception e) {
                // 非 OpenAI 相关的异常，直接抛出
                log.error("Unexpected error while fetching URL: {}, error: {}", url, e.getMessage());
                throw e;
            }
        }
    }

    private long calculateNextDelay(long currentDelay) {
        // 使用指数退避策略，并添加随机因子
        double randomFactor = 1.0 + (random.nextDouble() * RANDOM_FACTOR);
        return (long) (currentDelay * BACKOFF_MULTIPLIER * randomFactor);
    }
}