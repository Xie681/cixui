package com.jinyue.cixiu.module.customize.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import com.jinyue.cixiu.common.enums.OrderStatus;
import com.jinyue.cixiu.common.enums.OrderType;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.module.customize.dto.AiPreviewDTO;
import com.jinyue.cixiu.module.customize.dto.CalculatePriceDTO;
import com.jinyue.cixiu.module.customize.dto.SubmitOrderDTO;
import com.jinyue.cixiu.module.customize.dto.TextSubmitOrderDTO;
import com.jinyue.cixiu.module.customize.service.CustomizeService;
import com.jinyue.cixiu.module.customize.service.PriceService;
import com.jinyue.cixiu.module.customize.vo.AiPreviewVO;
import com.jinyue.cixiu.module.customize.vo.PriceVO;
import com.jinyue.cixiu.module.customize.vo.StyleVO;
import com.jinyue.cixiu.module.order.entity.Order;
import com.jinyue.cixiu.module.order.entity.OrderDetail;
import com.jinyue.cixiu.module.order.mapper.OrderDetailMapper;
import com.jinyue.cixiu.module.order.mapper.OrderMapper;
import com.jinyue.cixiu.module.user.entity.UserAddress;
import com.jinyue.cixiu.module.user.mapper.UserAddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 定制服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomizeServiceImpl implements CustomizeService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final UserAddressMapper addressMapper;
    private final PriceService priceService;
    private final com.jinyue.cixiu.common.service.AiServiceClient aiServiceClient;

    @Override
    public List<StyleVO> getStyles() {
        return Arrays.asList(
                new StyleVO(EmbroideryStyle.SU, "苏绣", "细腻柔和，色彩淡雅", null),
                new StyleVO(EmbroideryStyle.XIANG, "湘绣", "构图严谨，色彩鲜明", null),
                new StyleVO(EmbroideryStyle.SHU, "蜀绣", "针脚整齐，平整光亮", null),
                new StyleVO(EmbroideryStyle.YUE, "粤绣", "构图饱满，色彩浓郁", null)
        );
    }

    @Override
    public List<String> getFabrics() {
        return Arrays.asList(
                "真丝绸缎",
                "纯棉布",
                "麻布",
                "涤纶布",
                "丝绸"
        );
    }

    @Override
    public List<String> getThreads() {
        return Arrays.asList(
                "真丝线",
                "棉线",
                "金银线",
                "涤纶线"
        );
    }

    @Override
    public AiPreviewVO previewAiResult(AiPreviewDTO dto) {
        try {
            com.jinyue.cixiu.common.service.AiServiceClient.AiProcessResult aiResult;

            // 判断是图片定制还是文字定制
            if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
                // 图片定制
                log.info("开始AI预览(图片): 图片={}, 风格={}", dto.getImageUrl(), dto.getEmbroideryStyle());
                aiResult = aiServiceClient.processImageToEmbroidery(
                        dto.getImageUrl(),
                        dto.getEmbroideryStyle().name()
                );
            } else if (dto.getText() != null && !dto.getText().isEmpty()) {
                // 文字定制
                log.info("开始AI预览(文字): 文字={}, 字体={}, 大小={}, 颜色={}, 风格={}",
                        dto.getText(), dto.getFontStyle(), dto.getFontSize(),
                        dto.getColor(), dto.getEmbroideryStyle());
                aiResult = aiServiceClient.processTextToEmbroidery(
                        dto.getText(),
                        dto.getFontStyle(),
                        dto.getFontSize(),
                        dto.getColor(),
                        dto.getEmbroideryStyle().name()
                );
            } else {
                throw new BusinessException(400, "图片URL或文字内容至少需要提供一个");
            }

            log.info("AI预览完成: 结果图={}, 针数={}, 色数={}",
                    aiResult.getResultUrl(), aiResult.getStitchCount(), aiResult.getColorCount());

            return new AiPreviewVO(
                    aiResult.getResultUrl(),
                    aiResult.getStitchCount(),
                    aiResult.getColorCount()
            );

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI预览失败", e);
            throw new BusinessException(500, "AI预览失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitOrder(Long userId, SubmitOrderDTO dto) {
        // 1. 验证收货地址
        UserAddress address = addressMapper.selectOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getId, dto.getAddressId())
                .eq(UserAddress::getUserId, userId));

        if (address == null) {
            throw new BusinessException(400, "收货地址不存在");
        }

        // 2. 处理AI数据：优先使用前端预览的结果，避免重复调用AI
        Integer stitchCount;
        Integer colorCount;
        Integer trimCount = 20;
        String resultImageUrl;

        // 检查是否已经预览过（前端会传递预览结果）
        if (dto.getResultImageUrl() != null && dto.getStitchCount() != null && dto.getColorCount() != null) {
            // ✅ 使用前端预览时的结果，避免重复AI处理
            resultImageUrl = dto.getResultImageUrl();
            stitchCount = dto.getStitchCount();
            colorCount = dto.getColorCount();
            log.info("使用预览结果: 针数={}, 颜色数={}, 结果图={}", stitchCount, colorCount, resultImageUrl);
        } else if (dto.getImageUrl() != null) {
            // ❌ 用户跳过预览，需要调用AI服务
            stitchCount = 15000;  // 默认值
            colorCount = 8;
            resultImageUrl = null;

            try {
                log.info("用户未预览，调用AI服务处理图片: {}", dto.getImageUrl());
                com.jinyue.cixiu.common.service.AiServiceClient.AiProcessResult aiResult =
                        aiServiceClient.processImageToEmbroidery(
                                dto.getImageUrl(),
                                dto.getEmbroideryStyle().name()
                        );

                stitchCount = aiResult.getStitchCount();
                colorCount = aiResult.getColorCount();
                resultImageUrl = aiResult.getResultUrl();

                log.info("AI处理完成: 针数={}, 颜色数={}, 结果图={}",
                        stitchCount, colorCount, resultImageUrl);

            } catch (Exception e) {
                log.error("AI服务调用失败，使用默认参数", e);
                // 如果AI服务失败，使用默认值继续处理
            }
        } else {
            // 没有图片（理论上不应该发生）
            stitchCount = 15000;
            colorCount = 8;
            resultImageUrl = null;
        }

        // 3. 计算价格
        CalculatePriceDTO priceDTO = new CalculatePriceDTO();
        priceDTO.setType(OrderType.IMAGE);  // 图片定制订单
        priceDTO.setEmbroideryStyle(dto.getEmbroideryStyle());
        priceDTO.setStitchCount(stitchCount);
        priceDTO.setColorCount(colorCount);
        priceDTO.setTrimCount(trimCount);
        priceDTO.setWidth(dto.getWidth());
        priceDTO.setHeight(dto.getHeight());
        priceDTO.setFabricType(dto.getFabricType());
        priceDTO.setThreadType(dto.getThreadType());
        priceDTO.setQuantity(dto.getQuantity());
        priceDTO.setIsUrgent(dto.getIsUrgent());
        priceDTO.setNeedRefinement(dto.getNeedRefinement());  // ✅ 添加精修参数

        PriceVO price = priceService.calculatePrice(priceDTO);

        // 3. 生成订单号
        String orderNo = generateOrderNo();

        // 4. 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setType(OrderType.IMAGE);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalAmount(price.getTotalAmount());
        order.setPaidAmount(BigDecimal.ZERO);
        order.setAddressSnapshot(JSON.toJSONString(address));
        order.setRemark(dto.getRemark());
        order.setIsUrgent(dto.getIsUrgent() != null && dto.getIsUrgent() ? 1 : 0);
        order.setUrgentFactor(price.getUrgentFactor());
        // 保存价格明细
        order.setBasePrice(price.getBasePrice());
        order.setStitchFee(price.getStitchFee());
        order.setColorFee(price.getColorFee());
        order.setFabricFee(price.getFabricFee());
        order.setThreadFee(price.getThreadFee());
        order.setUrgentFee(price.getUrgentFee());
        order.setRefinementFee(price.getRefinementFee());  // ✅ 添加精修费用
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        // 5. 创建订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(order.getId());
        detail.setDesignId(dto.getDesignId());
        detail.setSourceImage(dto.getImageUrl());  // 原图URL
        detail.setResultImage(resultImageUrl);  // AI生成的结果图URL
        detail.setEmbroideryStyle(dto.getEmbroideryStyle());
        detail.setWidth(dto.getWidth());
        detail.setHeight(dto.getHeight());
        detail.setFabricType(dto.getFabricType());
        detail.setThreadType(dto.getThreadType());
        detail.setStitchCount(priceDTO.getStitchCount());
        detail.setColorCount(priceDTO.getColorCount());
        detail.setTrimCount(priceDTO.getTrimCount());
        detail.setQuantity(dto.getQuantity());
        detail.setUnitPrice(price.getTotalAmount().divide(new BigDecimal(dto.getQuantity()), 2, BigDecimal.ROUND_HALF_UP));
        detail.setNeedRefinement(dto.getNeedRefinement() != null && dto.getNeedRefinement());  // 是否需要图案精修
        detail.setCreatedAt(LocalDateTime.now());
        orderDetailMapper.insert(detail);

        log.info("用户 {} 提交订单成功，订单号: {}", userId, orderNo);
        return orderNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitTextOrder(Long userId, TextSubmitOrderDTO dto) {
        // 1. 验证收货地址
        UserAddress address = addressMapper.selectOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getId, dto.getAddressId())
                .eq(UserAddress::getUserId, userId));

        if (address == null) {
            throw new BusinessException(400, "收货地址不存在");
        }

        // 2. 处理AI数据：优先使用前端预览的结果，避免重复调用AI
        Integer stitchCount;
        Integer colorCount;
        Integer trimCount = 20;
        String resultImageUrl;

        // 检查是否已经预览过（前端会传递预览结果）
        if (dto.getResultImageUrl() != null && dto.getStitchCount() != null && dto.getColorCount() != null) {
            // ✅ 使用前端预览时的结果，避免重复AI处理
            resultImageUrl = dto.getResultImageUrl();
            stitchCount = dto.getStitchCount();
            colorCount = dto.getColorCount();
            log.info("使用预览结果: 针数={}, 颜色数={}, 结果图={}", stitchCount, colorCount, resultImageUrl);
        } else {
            // ❌ 用户跳过预览，需要调用AI服务
            stitchCount = 10000;  // 默认值
            colorCount = 5;
            resultImageUrl = null;

            try {
                log.info("用户未预览，调用AI服务处理文字: {}", dto.getText());
                com.jinyue.cixiu.common.service.AiServiceClient.AiProcessResult aiResult =
                        aiServiceClient.processTextToEmbroidery(
                                dto.getText(),
                                dto.getFontStyle(),
                                dto.getFontSize(),
                                dto.getColor(),
                                dto.getEmbroideryStyle().name()
                        );

                stitchCount = aiResult.getStitchCount();
                colorCount = aiResult.getColorCount();
                resultImageUrl = aiResult.getResultUrl();

                log.info("AI处理完成: 针数={}, 颜色数={}, 结果图={}",
                        stitchCount, colorCount, resultImageUrl);

            } catch (Exception e) {
                log.error("AI服务调用失败，使用默认参数", e);
                // 如果AI服务失败，使用默认值继续处理
            }
        }

        // 3. 计算价格
        CalculatePriceDTO priceDTO = new CalculatePriceDTO();
        priceDTO.setType(OrderType.TEXT);  // 文字定制订单
        priceDTO.setEmbroideryStyle(dto.getEmbroideryStyle());
        priceDTO.setStitchCount(stitchCount);
        priceDTO.setColorCount(colorCount);
        priceDTO.setTrimCount(trimCount);
        priceDTO.setWidth(dto.getWidth());
        priceDTO.setHeight(dto.getHeight());
        priceDTO.setFabricType(dto.getFabricType());
        priceDTO.setThreadType(dto.getThreadType());
        priceDTO.setQuantity(dto.getQuantity());
        priceDTO.setIsUrgent(dto.getIsUrgent());

        PriceVO price = priceService.calculatePrice(priceDTO);

        // 4. 生成订单号
        String orderNo = generateOrderNo();

        // 5. 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setType(OrderType.TEXT);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalAmount(price.getTotalAmount());
        order.setPaidAmount(BigDecimal.ZERO);
        order.setAddressSnapshot(JSON.toJSONString(address));
        order.setRemark(dto.getRemark());
        order.setIsUrgent(dto.getIsUrgent() != null && dto.getIsUrgent() ? 1 : 0);
        order.setUrgentFactor(price.getUrgentFactor());
        // 保存价格明细
        order.setBasePrice(price.getBasePrice());
        order.setStitchFee(price.getStitchFee());
        order.setColorFee(price.getColorFee());
        order.setFabricFee(price.getFabricFee());
        order.setThreadFee(price.getThreadFee());
        order.setUrgentFee(price.getUrgentFee());
        order.setRefinementFee(price.getRefinementFee());  // ✅ 添加精修费用
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        // 6. 创建订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(order.getId());
        detail.setDesignId(dto.getDesignId());
        detail.setSourceImage(null);  // 文字定制没有原图
        detail.setResultImage(resultImageUrl);  // AI生成的结果图URL
        detail.setEmbroideryStyle(dto.getEmbroideryStyle());
        detail.setWidth(dto.getWidth());
        detail.setHeight(dto.getHeight());
        detail.setFabricType(dto.getFabricType());
        detail.setThreadType(dto.getThreadType());
        detail.setStitchCount(priceDTO.getStitchCount());
        detail.setColorCount(priceDTO.getColorCount());
        detail.setTrimCount(priceDTO.getTrimCount());
        detail.setQuantity(dto.getQuantity());
        detail.setUnitPrice(price.getTotalAmount().divide(new BigDecimal(dto.getQuantity()), 2, BigDecimal.ROUND_HALF_UP));

        // 保存文字定制的特有信息
        detail.setCustomText(dto.getText());
        detail.setFontStyle(dto.getFontStyle());
        detail.setFontSize(dto.getFontSize());
        detail.setTextColor(dto.getColor());

        detail.setCreatedAt(LocalDateTime.now());
        orderDetailMapper.insert(detail);

        log.info("用户 {} 提交文字定制订单成功，订单号: {}", userId, orderNo);
        return orderNo;
    }

    /**
     * 生成订单号
     * 格式：CX + 年月日 + 6位随机数
     */
    private String generateOrderNo() {
        String dateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String randomStr = IdUtil.randomUUID().substring(0, 6).toUpperCase();
        return "CX" + dateStr + randomStr;
    }
}
