package com.anner.llm.service;

import com.anner.llm.dto.ChatRequest;
import com.anner.llm.dto.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest request);
}