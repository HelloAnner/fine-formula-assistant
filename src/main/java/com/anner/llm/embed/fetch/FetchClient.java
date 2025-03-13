package com.anner.llm.embed.fetch;

import java.util.List;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class FetchClient {

    // 保存路径
    private String savePath;

    // 需要分析的URL
    private List<String> urls;

    // 分析的文件名称
    private List<String> fileNames;

    private ChatLanguageModel chatLanguageModel;

    public void fetch() throws Exception {
        fetchWebBrowser();
    }

    private void fetchWebBrowser() throws Exception {
        if (urls != null && !urls.isEmpty()) {
            WebBrowserFetcher webBrowserFetcher = new WebBrowserFetcher(savePath, urls,
                    chatLanguageModel);
            webBrowserFetcher.fetch();
        }
    }
}
