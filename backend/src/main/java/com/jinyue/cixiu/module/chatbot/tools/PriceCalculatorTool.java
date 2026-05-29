package com.jinyue.cixiu.module.chatbot.tools;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import com.jinyue.cixiu.common.enums.OrderType;
import com.jinyue.cixiu.module.customize.dto.CalculatePriceDTO;
import com.jinyue.cixiu.module.customize.service.PriceService;
import com.jinyue.cixiu.module.customize.vo.PriceVO;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceCalculatorTool {

    private final PriceService priceService;

    @Tool("计算刺绣定制价格，根据风格、尺寸和数量计算总价和单价")
    public String calculatePrice(
        String style,
        int width,
        int height,
        int quantity
    ) {
        log.info("AI调用价格计算工具，风格: {}, 尺寸: {}x{}, 数量: {}", 
            style, width, height, quantity);
        
        try {
            // 解析风格
            EmbroideryStyle embroideryStyle;
            try {
                embroideryStyle = EmbroideryStyle.valueOf(style.toUpperCase());
            } catch (IllegalArgumentException e) {
                return "无效的刺绣风格: " + style + "。可选风格: SU(苏绣), XIANG(湘绣), SHU(蜀绣), YUE(粤绣)";
            }
            
            CalculatePriceDTO dto = new CalculatePriceDTO();
            dto.setType(OrderType.IMAGE);
            dto.setEmbroideryStyle(embroideryStyle);
            dto.setWidth(BigDecimal.valueOf(width));
            dto.setHeight(BigDecimal.valueOf(height));
            dto.setQuantity(quantity);
            // 设置默认值
            dto.setStitchCount(10000);
            dto.setColorCount(10);
            dto.setTrimCount(50);
            dto.setFabricType("棉布");
            dto.setThreadType("丝线");
            dto.setIsUrgent(false);
            
            PriceVO priceVO = priceService.calculatePrice(dto);
            
            return String.format(
                "价格计算结果：\n总金额：%s元\n基础价：%s元\n数量：%d件\n单价：约%s元/件",
                priceVO.getTotalAmount(),
                priceVO.getBasePrice(),
                quantity,
                priceVO.getTotalAmount().divide(BigDecimal.valueOf(quantity), 2, java.math.RoundingMode.HALF_UP)
            );
        } catch (Exception e) {
            log.error("价格计算失败", e);
            return "价格计算失败: " + e.getMessage();
        }
    }
}
