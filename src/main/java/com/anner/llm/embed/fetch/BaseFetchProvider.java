package com.anner.llm.embed.fetch;

import java.util.List;

import dev.langchain4j.model.chat.ChatLanguageModel;

public abstract class BaseFetchProvider implements FetchProvider {

    protected String savePath;

    protected List<String> analyzeFiledNames;

    protected ChatLanguageModel chatLanguageModel;

    protected BaseFetchProvider(String savePath, List<String> analyzeFiledNames, ChatLanguageModel chatLanguageModel) {
        this.savePath = savePath;
        this.analyzeFiledNames = analyzeFiledNames;
        this.chatLanguageModel = chatLanguageModel;
    }
}
