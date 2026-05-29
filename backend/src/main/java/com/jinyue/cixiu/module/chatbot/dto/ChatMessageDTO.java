package com.jinyue.cixiu.module.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "聊天消息请求")
public class ChatMessageDTO {

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "用户消息内容", example = "我想定制一件刺绣")
    private String message;

    @Schema(description = "会话ID（用于多轮对话）", example = "session-123456")
    private String sessionId;

    @Schema(description = "是否流式返回", example = "false")
    private Boolean stream = false;

    @Schema(description = "图片URL列表（支持多模态）", example = "[\"https://example.com/image.jpg\"]")
    private List<String> imageUrls;
}
