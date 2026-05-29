package com.jinyue.cixiu.module.customize.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI预览结果VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiPreviewVO {

    /**
     * AI生成的结果图URL
     */
    private String resultImageUrl;

    /**
     * 针数
     */
    private Integer stitchCount;

    /**
     * 色数
     */
    private Integer colorCount;
}
