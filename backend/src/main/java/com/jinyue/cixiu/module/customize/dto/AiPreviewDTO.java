package com.jinyue.cixiu.module.customize.dto;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * AI预览请求DTO
 */
@Data
public class AiPreviewDTO {

    /**
     * 图片URL（图片定制时必填）
     */
    private String imageUrl;

    /**
     * 文字内容（文字定制时必填）
     */
    private String text;

    /**
     * 字体样式（文字定制时使用）
     */
    private String fontStyle;

    /**
     * 字体大小（文字定制时使用）
     */
    private Integer fontSize;

    /**
     * 颜色（文字定制时使用）
     */
    private String color;

    /**
     * 绣制风格
     */
    @NotNull(message = "绣制风格不能为空")
    private EmbroideryStyle embroideryStyle;
}
