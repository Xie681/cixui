package com.jinyue.cixiu.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情实体类
 */
@Data
@TableName("order_detail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 设计稿ID
     */
    private Long designId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 原始图片URL
     */
    private String sourceImage;

    /**
     * AI生成的结果图URL
     */
    private String resultImage;

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
     * 剪线次数
     */
    private Integer trimCount;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

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

    /**
     * 是否需要图案精修（人工设计师精修）
     */
    private Boolean needRefinement;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
