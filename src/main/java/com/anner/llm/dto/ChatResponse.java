package com.anner.llm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private String message;
    private String sessionId;
    private boolean success;
    private String error;
}