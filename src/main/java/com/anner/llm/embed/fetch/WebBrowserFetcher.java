package com.anner.llm.embed.fetch;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebBrowserFetcher extends BaseFetchProvider {

    private List<String> urls;

    public WebBrowserFetcher(String savePath, List<String> analyzeFiledNames, List<String> urls) {
        super(savePath, analyzeFiledNames);
        this.urls = urls;
    }

    @Override
    public void fetch() throws Exception {

    }
}