package com.jinyue.cixiu.common.enums;

import lombok.Getter;

/**
 * 设计稿状态枚举
 */
@Getter
public enum DesignStatus {

    AI_GENERATED("AI生成"),
    DESIGNING("设计中"),
    PENDING_REVIEW("待审核"),
    APPROVED("已通过"),
    REJECTED("已驳回");

    private final String description;

    DesignStatus(String description) {
        this.description = description;
    }
}
