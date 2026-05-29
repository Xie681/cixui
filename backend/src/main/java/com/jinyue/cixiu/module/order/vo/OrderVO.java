package com.jinyue.cixiu.module.order.vo;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import com.jinyue.cixiu.common.enums.OrderStatus;
import com.jinyue.cixiu.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 */
@Data
public class OrderVO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 定制类型
     */
    private OrderType type;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    private BigDecimal paidAmount;

    /**
     * 是否加急
     */
    private Boolean isUrgent;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 支付时间
     */
    private LocalDateTime paidAt;

    /**
     * 订单详情（简要信息）
     */
    private String summary;

    /**
     * 原始图片URL
     */
    private String imageUrl;

    /**
     * AI生成的结果图URL
     */
    private String resultImageUrl;

    /**
     * 绣制风格
     */
    private EmbroideryStyle embroideryStyle;

    /**
     * 绣制风格描述
     */
    private String styleDesc;

    /**
     * 面料类型
     */
    private String fabricType;

    /**
     * 线材类型
     */
    private String threadType;

    /**
     * 宽度(cm)
     */
    private BigDecimal width;

    /**
     * 高度(cm)
     */
    private BigDecimal height;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否需要图案精修
     */
    private Boolean needRefinement;
}
