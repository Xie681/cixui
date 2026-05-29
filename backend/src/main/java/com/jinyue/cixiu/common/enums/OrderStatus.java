package com.jinyue.cixiu.common.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatus {

    PENDING_PAYMENT("待支付"),
    DESIGNING("设计中"),
    SAMPLING("打样中"),
    REVIEWING("复审中"),
    SCHEDULING("排产中"),
    EMBROIDERING("绣制中"),
    QUALITY_CHECK("质检中"),
    PENDING_SHIP("待发货"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
