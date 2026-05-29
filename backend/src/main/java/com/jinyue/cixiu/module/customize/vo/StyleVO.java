package com.jinyue.cixiu.module.customize.vo;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 绣制风格VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StyleVO {

    /**
     * 风格编码
     */
    private EmbroideryStyle code;

    /**
     * 风格名称
     */
    private String name;

    /**
     * 风格描述
     */
    private String description;

    /**
     * 预览图
     */
    private String previewImage;
}
