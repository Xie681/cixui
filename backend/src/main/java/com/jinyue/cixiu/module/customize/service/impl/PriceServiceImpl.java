package com.jinyue.cixiu.module.customize.service.impl;

import com.jinyue.cixiu.common.enums.OrderType;
import com.jinyue.cixiu.module.customize.dto.CalculatePriceDTO;
import com.jinyue.cixiu.module.customize.service.PriceService;
import com.jinyue.cixiu.module.customize.vo.PriceVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 报价服务实现类
 * 报价计算公式：
 * 总价 = 基础价 + 针数费 + 色数费 + 面料费 + 线材费
 * 如果加急：总价 = 总价 × 加急系数
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    /**
     * 基础价（按定制类型）
     */
    private static final BigDecimal BASE_PRICE_IMAGE = new BigDecimal("50.00");
    private static final BigDecimal BASE_PRICE_TEXT = new BigDecimal("30.00");
    private static final BigDecimal BASE_PRICE_TEMPLATE = new BigDecimal("20.00");

    /**
     * 针数单价（每1000针）
     */
    private static final BigDecimal STITCH_PRICE_PER_THOUSAND = new BigDecimal("2.00");

    /**
     * 色数单价
     */
    private static final BigDecimal COLOR_PRICE = new BigDecimal("5.00");

    /**
     * 面料单价（每平方厘米）
     */
    private static final BigDecimal FABRIC_PRICE_PER_CM2 = new BigDecimal("0.05");

    /**
     * 线材单价（每色）
     */
    private static final BigDecimal THREAD_PRICE_PER_COLOR = new BigDecimal("3.00");

    /**
     * 精修费用（固定费用）
     */
    private static final BigDecimal REFINEMENT_FEE = new BigDecimal("100.00");

    /**
     * 加急系数
     */
    private static final BigDecimal URGENT_FACTOR = new BigDecimal("1.50");

    @Override
    public PriceVO calculatePrice(CalculatePriceDTO dto) {
        // 1. 基础价
        BigDecimal basePrice = getBasePrice(dto.getType());

        // 2. 针数费（每1000针）
        BigDecimal stitchFee = new BigDecimal(dto.getStitchCount())
                .divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP)
                .multiply(STITCH_PRICE_PER_THOUSAND)
                .setScale(2, RoundingMode.HALF_UP);

        // 3. 色数费
        BigDecimal colorFee = new BigDecimal(dto.getColorCount())
                .multiply(COLOR_PRICE)
                .setScale(2, RoundingMode.HALF_UP);

        // 4. 面料费（宽 × 高 × 单价）
        BigDecimal area = dto.getWidth().multiply(dto.getHeight());
        BigDecimal fabricFee = area.multiply(FABRIC_PRICE_PER_CM2)
                .setScale(2, RoundingMode.HALF_UP);

        // 5. 线材费（色数 × 单价）
        BigDecimal threadFee = new BigDecimal(dto.getColorCount())
                .multiply(THREAD_PRICE_PER_COLOR)
                .setScale(2, RoundingMode.HALF_UP);

        // 6. 小计（单件）
        BigDecimal subtotal = basePrice
                .add(stitchFee)
                .add(colorFee)
                .add(fabricFee)
                .add(threadFee)
                .setScale(2, RoundingMode.HALF_UP);

        // 7. 数量计算
        subtotal = subtotal.multiply(new BigDecimal(dto.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);

        // 8. 精修费用
        BigDecimal refinementFee = BigDecimal.ZERO;
        if (dto.getNeedRefinement() != null && dto.getNeedRefinement()) {
            refinementFee = REFINEMENT_FEE.multiply(new BigDecimal(dto.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // 9. 加急处理
        BigDecimal urgentFactor = BigDecimal.ONE;
        BigDecimal urgentFee = BigDecimal.ZERO;
        if (dto.getIsUrgent() != null && dto.getIsUrgent()) {
            urgentFactor = URGENT_FACTOR;
            urgentFee = subtotal.multiply(urgentFactor.subtract(BigDecimal.ONE))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // 10. 总价
        BigDecimal totalAmount = subtotal.add(refinementFee).add(urgentFee)
                .setScale(2, RoundingMode.HALF_UP);

        // 11. 构建报价明细
        String description = buildDescription(dto, basePrice, stitchFee, colorFee, fabricFee, threadFee, refinementFee);

        return PriceVO.builder()
                .basePrice(basePrice)
                .stitchFee(stitchFee)
                .colorFee(colorFee)
                .fabricFee(fabricFee)
                .threadFee(threadFee)
                .subtotal(subtotal)
                .refinementFee(refinementFee)
                .urgentFactor(urgentFactor)
                .urgentFee(urgentFee)
                .totalAmount(totalAmount)
                .description(description)
                .build();
    }

    /**
     * 获取基础价
     */
    private BigDecimal getBasePrice(OrderType type) {
        return switch (type) {
            case IMAGE -> BASE_PRICE_IMAGE;
            case TEXT -> BASE_PRICE_TEXT;
            case TEMPLATE -> BASE_PRICE_TEMPLATE;
        };
    }

    /**
     * 构建报价说明
     */
    private String buildDescription(CalculatePriceDTO dto, BigDecimal basePrice,
                                     BigDecimal stitchFee, BigDecimal colorFee,
                                     BigDecimal fabricFee, BigDecimal threadFee,
                                     BigDecimal refinementFee) {
        StringBuilder sb = new StringBuilder();
        sb.append("基础价: ¥").append(basePrice).append("\n");
        sb.append("针数费: ").append(dto.getStitchCount()).append("针 × ¥")
                .append(STITCH_PRICE_PER_THOUSAND).append("/千针 = ¥").append(stitchFee).append("\n");
        sb.append("色数费: ").append(dto.getColorCount()).append("色 × ¥")
                .append(COLOR_PRICE).append("/色 = ¥").append(colorFee).append("\n");
        sb.append("面料费: ").append(dto.getWidth()).append("cm × ")
                .append(dto.getHeight()).append("cm × ¥")
                .append(FABRIC_PRICE_PER_CM2).append("/cm² = ¥").append(fabricFee).append("\n");
        sb.append("线材费: ").append(dto.getColorCount()).append("色 × ¥")
                .append(THREAD_PRICE_PER_COLOR).append("/色 = ¥").append(threadFee).append("\n");
        sb.append("数量: ").append(dto.getQuantity()).append("件\n");

        if (dto.getNeedRefinement() != null && dto.getNeedRefinement()) {
            sb.append("精修费: ¥").append(REFINEMENT_FEE).append("/件 × ")
                    .append(dto.getQuantity()).append("件 = ¥").append(refinementFee).append("\n");
        }

        if (dto.getIsUrgent() != null && dto.getIsUrgent()) {
            sb.append("加急: ×").append(URGENT_FACTOR).append("\n");
        }

        return sb.toString();
    }
}
