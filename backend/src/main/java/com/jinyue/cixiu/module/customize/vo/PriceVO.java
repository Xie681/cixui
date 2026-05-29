package com.jinyue.cixiu.module.customize.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 报价响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceVO {

    /**
     * 基础价
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
     * 小计
     */
    private BigDecimal subtotal;

    /**
     * 加急系数
     */
    private BigDecimal urgentFactor;

    /**
     * 加急费用
     */
    private BigDecimal urgentFee;

    /**
     * 精修费用
     */
    private BigDecimal refinementFee;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 明细说明
     */
    private String description;
}
