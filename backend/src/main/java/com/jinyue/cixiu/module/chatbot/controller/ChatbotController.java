package com.jinyue.cixiu.module.chatbot.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.chatbot.dto.ChatMessageDTO;
import com.jinyue.cixiu.module.chatbot.dto.KnowledgeQueryDTO;
import com.jinyue.cixiu.module.chatbot.service.ChatbotService;
import com.jinyue.cixiu.module.chatbot.vo.ChatResponseVO;
import com.jinyue.cixiu.module.chatbot.vo.KnowledgeResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Tag(name = "智能客服", description = "AI智能客服接口")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    @Operation(summary = "多轮对话", description = "与AI客服进行多轮对话，支持工具调用")
    public Result<ChatResponseVO> chat(@Valid @RequestBody ChatMessageDTO dto, Authentication authentication) {
        Long userId = getUserId(authentication);
        ChatResponseVO response = chatbotService.chat(dto, userId);
        return Result.success(response);
    }

    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式对话", description = "流式返回AI回复内容，sessionId 通过 X-Session-Id header 返回")
    public Flux<ServerSentEvent<String>> chatStream(
            @Valid @RequestBody ChatMessageDTO dto, 
            Authentication authentication,
            jakarta.servlet.http.HttpServletResponse response
    ) {
        Long userId = getUserId(authentication);
        String sessionId = chatbotService.getOrCreateSessionId(dto.getSessionId(), userId);
        
        // 在响应头中返回 sessionId
        response.setHeader("X-Session-Id", sessionId);
        
        return chatbotService.chatStream(dto, userId)
                .map(token -> ServerSentEvent.<String>builder()
                        .data(token)
                        .build());
    }

    @PostMapping("/knowledge/query")
    @Operation(summary = "知识库问答", description = "基于知识库的问答")
    public Result<KnowledgeResponseVO> queryKnowledge(@Valid @RequestBody KnowledgeQueryDTO dto) {
        KnowledgeResponseVO response = chatbotService.queryKnowledge(dto);
        return Result.success(response);
    }

    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "清除会话", description = "清除指定会话的历史记录")
    public Result<Void> clearSession(@PathVariable String sessionId) {
        chatbotService.clearSession(sessionId);
        return Result.success();
    }

    private Long getUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }
}
