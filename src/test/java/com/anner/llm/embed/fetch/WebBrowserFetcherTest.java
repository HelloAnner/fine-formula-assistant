package com.anner.llm.embed.fetch;

import java.util.Arrays;

import org.junit.Test;

import com.anner.llm.model.ModelManager;

public class WebBrowserFetcherTest {

    @Test
    public void testFetch() throws Exception {
        WebBrowserFetcher fetcher = new WebBrowserFetcher("test",
                Arrays.asList(
                        "公式名称",
                        "公式描述",
                        "公式使用场景",
                        "公式使用例子"),
                Arrays.asList("https://help.fanruan.com/finereport/doc-view-3930.html"), ModelManager.doubao());
        fetcher.fetch();
    }
}