package com.jinyue.cixiu.common.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRole {

    USER("普通用户"),
    DESIGNER("设计师"),
    ADMIN("管理员");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
