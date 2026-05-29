package com.jinyue.cixiu.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.jinyue.cixiu.common.enums.OrderStatus;
import com.jinyue.cixiu.common.enums.OrderType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 定制类型
     */
    private OrderType type;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    private BigDecimal paidAmount;

    /**
     * 收货地址快照（JSON）
     */
    private String addressSnapshot;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否加急：1是 0否
     */
    private Integer isUrgent;

    /**
     * 加急系数
     */
    private BigDecimal urgentFactor;

    /**
     * 基础价格
     */
    private BigDecimal basePrice;

    /**
     * 针数费用
     */
    private BigDecimal stitchFee;

    /**
     * 色数费用
     */
    private BigDecimal colorFee;

    /**
     * 面料费用
     */
    private BigDecimal fabricFee;

    /**
     * 线材费用
     */
    private BigDecimal threadFee;

    /**
     * 加急费用
     */
    private BigDecimal urgentFee;

    /**
     * 精修费用
     */
    private BigDecimal refinementFee;

    /**
     * 支付时间
     */
    private LocalDateTime paidAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
