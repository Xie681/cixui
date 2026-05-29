package com.jinyue.cixiu.common.enums;

import lombok.Getter;

/**
 * 订单类型枚举
 */
@Getter
public enum OrderType {

    IMAGE("图像定制"),
    TEXT("文字定制"),
    TEMPLATE("模板定制");

    private final String description;

    OrderType(String description) {
        this.description = description;
    }
}
