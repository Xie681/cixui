package com.jinyue.cixiu.module.customize.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.customize.dto.DesignChatDTO;
import com.jinyue.cixiu.module.customize.service.DesignAssistantService;
import com.jinyue.cixiu.module.customize.vo.DesignChatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 设计助手控制器
 * 专门用于定制流程中的需求理解和整理
 */
@Slf4j
@RestController
@RequestMapping("/api/customize/design")
@RequiredArgsConstructor
@Tag(name = "设计助手", description = "定制流程中的AI设计助手")
public class DesignAssistantController {

    private final DesignAssistantService designAssistantService;

    @PostMapping("/chat")
    @Operation(summary = "设计对话", description = "与AI设计助手对话，理解并整理需求")
    public Result<DesignChatVO> chat(
            @Valid @RequestBody DesignChatDTO dto,
            Authentication authentication
    ) {
        // 获取用户ID（如果未登录则为null）
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                userId = Long.parseLong(authentication.getName());
            } catch (NumberFormatException e) {
                log.warn("无法解析用户ID: {}", authentication.getName());
            }
        }
        
        log.info("用户 {} 发起设计对话: {}", userId != null ? userId : "未登录", dto.getMessage());

        DesignChatVO response = designAssistantService.chat(dto, userId);
        return Result.success(response);
    }

    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "清除设计会话", description = "清除指定的设计会话记录")
    public Result<Void> clearSession(@PathVariable String sessionId) {
        log.info("清除设计会话: {}", sessionId);
        designAssistantService.clearSession(sessionId);
        return Result.success(null);
    }
}
