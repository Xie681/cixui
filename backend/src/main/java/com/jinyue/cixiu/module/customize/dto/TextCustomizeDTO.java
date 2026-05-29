package com.jinyue.cixiu.module.customize.dto;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 文字定制DTO
 */
@Data
public class TextCustomizeDTO {

    /**
     * 文字内容
     */
    @NotBlank(message = "文字内容不能为空")
    private String text;

    /**
     * 字体样式
     */
    private String fontStyle;

    /**
     * 字号大小
     */
    @Min(value = 12, message = "字号最小为12")
    @Max(value = 200, message = "字号最大为200")
    private Integer fontSize;

    /**
     * 文字颜色
     */
    private String color;

    /**
     * 绣制风格
     */
    @NotNull(message = "绣制风格不能为空")
    private EmbroideryStyle embroideryStyle;

    /**
     * 宽度(cm)
     */
    @NotNull(message = "宽度不能为空")
    @DecimalMin(value = "1.0", message = "宽度最小为1cm")
    @DecimalMax(value = "200.0", message = "宽度最大为200cm")
    private BigDecimal width;

    /**
     * 高度(cm)
     */
    @NotNull(message = "高度不能为空")
    @DecimalMin(value = "1.0", message = "高度最小为1cm")
    @DecimalMax(value = "200.0", message = "高度最大为200cm")
    private BigDecimal height;

    /**
     * 面料类型
     */
    @NotBlank(message = "面料类型不能为空")
    private String fabricType;

    /**
     * 线材类型
     */
    @NotBlank(message = "线材类型不能为空")
    private String threadType;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer quantity;

    /**
     * 是否加急
     */
    private Boolean isUrgent;

    /**
     * 备注
     */
    private String remark;
}
