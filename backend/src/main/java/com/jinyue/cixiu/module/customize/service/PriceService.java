package com.jinyue.cixiu.module.customize.service;

import com.jinyue.cixiu.module.customize.dto.CalculatePriceDTO;
import com.jinyue.cixiu.module.customize.vo.PriceVO;

/**
 * 报价服务接口
 */
public interface PriceService {

    /**
     * 计算订单报价
     *
     * @param dto 报价参数
     * @return 报价详情
     */
    PriceVO calculatePrice(CalculatePriceDTO dto);
}
