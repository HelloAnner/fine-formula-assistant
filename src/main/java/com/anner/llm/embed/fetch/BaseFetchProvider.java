package com.anner.llm.embed.fetch;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class BaseFetchProvider implements FetchProvider {

    protected String savePath;

    protected ChatLanguageModel chatLanguageModel;

    protected BaseFetchProvider(String savePath, ChatLanguageModel chatLanguageModel) {
        this.savePath = savePath;
        this.chatLanguageModel = chatLanguageModel;
    }
}
