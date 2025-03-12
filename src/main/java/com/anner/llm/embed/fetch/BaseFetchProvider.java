package com.anner.llm.embed.fetch;

import java.util.List;

public abstract class BaseFetchProvider implements FetchProvider {

    protected String savePath;

    protected List<String> analyzeFiledNames;

    protected BaseFetchProvider(String savePath, List<String> analyzeFiledNames) {
        this.savePath = savePath;
        this.analyzeFiledNames = analyzeFiledNames;
    }
}
