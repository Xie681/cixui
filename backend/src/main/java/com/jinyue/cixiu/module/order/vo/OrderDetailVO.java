package com.jinyue.cixiu.module.order.vo;

import com.alibaba.fastjson2.JSONObject;
import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import com.jinyue.cixiu.common.enums.OrderStatus;
import com.jinyue.cixiu.common.enums.OrderType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情VO
 */
@Data
public class OrderDetailVO {

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
     * 定制类型描述
     */
    private String typeDesc;

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
     * 收货地址快照
     */
    private JSONObject addressSnapshot;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否加急
     */
    private Boolean isUrgent;

    /**
     * 加急系数
     */
    private BigDecimal urgentFactor;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 支付时间
     */
    private LocalDateTime paidAt;

    /**
     * 原始图片URL
     */
    private String sourceImage;

    /**
     * AI生成的结果图URL
     */
    private String resultImageUrl;

    /**
     * 绣制风格
     */
    private EmbroideryStyle embroideryStyle;

    /**
     * 宽度(cm)
     */
    private BigDecimal width;

    /**
     * 高度(cm)
     */
    private BigDecimal height;

    /**
     * 面料类型
     */
    private String fabricType;

    /**
     * 线材类型
     */
    private String threadType;

    /**
     * 针数
     */
    private Integer stitchCount;

    /**
     * 色数
     */
    private Integer colorCount;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

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
     * 绣制风格描述
     */
    private String styleDesc;

    /**
     * 是否需要图案精修
     */
    private Boolean needRefinement;

    /**
     * 文字定制：文字内容
     */
    private String customText;

    /**
     * 文字定制：字体样式
     */
    private String fontStyle;

    /**
     * 文字定制：字号大小
     */
    private Integer fontSize;

    /**
     * 文字定制：文字颜色
     */
    private String textColor;
}
