package com.jinyue.cixiu.module.customize.dto;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import com.jinyue.cixiu.common.enums.OrderType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 计算报价DTO
 */
@Data
public class CalculatePriceDTO {

    /**
     * 定制类型
     */
    @NotNull(message = "定制类型不能为空")
    private OrderType type;

    /**
     * 绣制风格
     */
    private EmbroideryStyle embroideryStyle;

    /**
     * 针数
     */
    @NotNull(message = "针数不能为空")
    @Min(value = 1, message = "针数必须大于0")
    private Integer stitchCount;

    /**
     * 色数
     */
    @NotNull(message = "色数不能为空")
    @Min(value = 1, message = "色数必须大于0")
    private Integer colorCount;

    /**
     * 剪线次数
     */
    @NotNull(message = "剪线次数不能为空")
    @Min(value = 0, message = "剪线次数不能为负")
    private Integer trimCount;

    /**
     * 宽度(cm)
     */
    @NotNull(message = "宽度不能为空")
    @DecimalMin(value = "1.0", message = "宽度最小为1cm")
    private BigDecimal width;

    /**
     * 高度(cm)
     */
    @NotNull(message = "高度不能为空")
    @DecimalMin(value = "1.0", message = "高度最小为1cm")
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
     * 是否需要图案精修
     */
    private Boolean needRefinement;
}
