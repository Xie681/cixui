package com.jinyue.cixiu.module.customize.service;

import com.jinyue.cixiu.module.customize.dto.AiPreviewDTO;
import com.jinyue.cixiu.module.customize.dto.ImageCustomizeDTO;
import com.jinyue.cixiu.module.customize.dto.SubmitOrderDTO;
import com.jinyue.cixiu.module.customize.dto.TextSubmitOrderDTO;
import com.jinyue.cixiu.module.customize.vo.AiPreviewVO;
import com.jinyue.cixiu.module.customize.vo.StyleVO;

import java.util.List;

/**
 * 定制服务接口
 */
public interface CustomizeService {

    /**
     * 获取所有绣制风格
     *
     * @return 风格列表
     */
    List<StyleVO> getStyles();

    /**
     * 获取所有面料类型
     *
     * @return 面料列表
     */
    List<String> getFabrics();

    /**
     * 获取所有线材类型
     *
     * @return 线材列表
     */
    List<String> getThreads();

    /**
     * AI预览刺绣效果
     *
     * @param dto 预览请求参数
     * @return AI生成的结果图URL及相关信息
     */
    AiPreviewVO previewAiResult(AiPreviewDTO dto);

    /**
     * 提交图片定制订单
     *
     * @param userId 用户ID
     * @param dto    订单信息
     * @return 订单号
     */
    String submitOrder(Long userId, SubmitOrderDTO dto);

    /**
     * 提交文字定制订单
     *
     * @param userId 用户ID
     * @param dto    文字订单信息
     * @return 订单号
     */
    String submitTextOrder(Long userId, TextSubmitOrderDTO dto);
}
