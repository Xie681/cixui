package com.jinyue.cixiu.module.customize.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 设计对话请求DTO
 */
@Data
@Schema(description = "设计对话请求")
public class DesignChatDTO {

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "用户需求描述", example = "我想要一个牡丹花图案，苏绣风格，粉色为主")
    private String message;

    @Schema(description = "会话ID（用于多轮对话）", example = "design-session-123456", nullable = true)
    private String sessionId;
}
