package com.jinyue.cixiu.module.customize.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设计对话响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "设计对话响应")
public class DesignChatVO {

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "AI回复内容")
    private String reply;

    @Schema(description = "生成的效果图URL")
    private String designImageUrl;

    @Schema(description = "提取的需求信息")
    private RequirementInfo requirement;

    @Schema(description = "预估价格")
    private String estimatedPrice;

    @Schema(description = "预估工期")
    private String estimatedTime;

    @Schema(description = "时间戳")
    private LocalDateTime timestamp;

    /**
     * 需求信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequirementInfo {
        @Schema(description = "图案主题")
        private String theme;

        @Schema(description = "刺绣风格")
        private String style;

        @Schema(description = "颜色要求")
        private String colors;

        @Schema(description = "尺寸要求")
        private String size;

        @Schema(description = "其他细节")
        private String additionalDetails;
    }
}
