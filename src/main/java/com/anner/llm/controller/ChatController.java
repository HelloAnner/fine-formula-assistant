package com.anner.llm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anner.llm.dto.ChatRequest;
import com.anner.llm.dto.ChatResponse;
import com.anner.llm.embed.EmbedService;
import com.anner.llm.service.chat.ChatServiceImpl;

@Controller
public class ChatController {

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private EmbedService embedService;

    @GetMapping("/")
    public String index() {
        return "chat";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }

    @GetMapping("/api/fetch")
    @ResponseBody
    public String fetch() {
        try {
            embedService.fetchAndSave();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}