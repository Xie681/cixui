package com.jinyue.cixiu.module.chatbot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "聊天响应")
public class ChatResponseVO {

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "AI回复内容")
    private String reply;

    @Schema(description = "使用的工具列表")
    private List<String> toolsUsed;

    @Schema(description = "响应时间")
    private LocalDateTime timestamp;

    @Schema(description = "Token使用情况")
    private TokenUsage tokenUsage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenUsage {
        @Schema(description = "输入Token数")
        private Integer promptTokens;

        @Schema(description = "输出Token数")
        private Integer completionTokens;

        @Schema(description = "总Token数")
        private Integer totalTokens;
    }
}
