package com.jinyue.cixiu.module.customize.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文字定制订单提交DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextSubmitOrderDTO extends TextCustomizeDTO {

    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    /**
     * AI生成的设计稿ID（如果有）
     */
    private Long designId;

    /**
     * AI预览的结果图URL（如果已预览）
     */
    private String resultImageUrl;

    /**
     * AI返回的针数（如果已预览）
     */
    private Integer stitchCount;

    /**
     * AI返回的色数（如果已预览）
     */
    private Integer colorCount;
}
