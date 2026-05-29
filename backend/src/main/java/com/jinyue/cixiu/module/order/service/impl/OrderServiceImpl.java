package com.jinyue.cixiu.module.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.enums.OrderStatus;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.module.order.entity.Order;
import com.jinyue.cixiu.module.order.entity.OrderDetail;
import com.jinyue.cixiu.module.order.mapper.OrderDetailMapper;
import com.jinyue.cixiu.module.order.mapper.OrderMapper;
import com.jinyue.cixiu.module.order.service.OrderService;
import com.jinyue.cixiu.module.order.vo.OrderDetailVO;
import com.jinyue.cixiu.module.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderVO> listOrders(Long userId, String status) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt);

        // 如果指定了状态，添加状态过滤
        if (StrUtil.isNotBlank(status)) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status);
                queryWrapper.eq(Order::getStatus, orderStatus);
            } catch (IllegalArgumentException e) {
                throw new BusinessException(400, "无效的订单状态");
            }
        }

        List<Order> orders = orderMapper.selectList(queryWrapper);

        return orders.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        Order order = getOrderEntity(userId, orderId);
        return buildOrderDetailVO(order);
    }

    @Override
    public OrderDetailVO getOrderDetailByOrderNo(Long userId, String orderNo) {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId));

        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        return buildOrderDetailVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getOrderEntity(userId, orderId);

        // 只有待支付状态的订单才能取消
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException(400, "当前订单状态不能取消");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderMapper.updateById(order);

        log.info("用户 {} 取消订单 {} 成功", userId, orderId);
    }

    @Override
    public Order getByOrderNo(String orderNo) {
        return orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo));
    }

    /**
     * 获取订单实体（验证权限）
     */
    private Order getOrderEntity(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权访问此订单");
        }
        return order;
    }

    /**
     * 构建订单详情VO
     */
    private OrderDetailVO buildOrderDetailVO(Order order) {
        OrderDetailVO vo = BeanUtil.copyProperties(order, OrderDetailVO.class, "addressSnapshot");
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        vo.setTypeDesc(order.getType() != null ? order.getType().getDescription() : "");
        vo.setIsUrgent(order.getIsUrgent() == 1);
        vo.setAddressSnapshot(JSON.parseObject(order.getAddressSnapshot()));

        // 查询订单详情
        OrderDetail detail = orderDetailMapper.selectOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, order.getId())
                .last("LIMIT 1"));

        if (detail != null) {
            vo.setSourceImage(detail.getSourceImage());
            vo.setResultImageUrl(detail.getResultImage());  // ✅ 添加结果图
            vo.setEmbroideryStyle(detail.getEmbroideryStyle());
            vo.setStyleDesc(detail.getEmbroideryStyle() != null ? detail.getEmbroideryStyle().getDescription() : "");
            vo.setWidth(detail.getWidth());
            vo.setHeight(detail.getHeight());
            vo.setFabricType(detail.getFabricType());
            vo.setThreadType(detail.getThreadType());
            vo.setStitchCount(detail.getStitchCount());
            vo.setColorCount(detail.getColorCount());
            vo.setQuantity(detail.getQuantity());
            vo.setUnitPrice(detail.getUnitPrice());
            vo.setNeedRefinement(detail.getNeedRefinement());  // ✅ 添加是否需要精修
            
            // 文字定制相关字段
            vo.setCustomText(detail.getCustomText());
            vo.setFontStyle(detail.getFontStyle());
            vo.setFontSize(detail.getFontSize());
            vo.setTextColor(detail.getTextColor());
        }

        return vo;
    }

    /**
     * 转换为OrderVO
     */
    private OrderVO convertToVO(Order order) {
        OrderVO vo = BeanUtil.copyProperties(order, OrderVO.class);
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        vo.setIsUrgent(order.getIsUrgent() == 1);

        // 查询订单详情，填充展示信息
        OrderDetail detail = orderDetailMapper.selectOne(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, order.getId())
                .last("LIMIT 1"));

        if (detail != null) {
            vo.setImageUrl(detail.getSourceImage());
            vo.setResultImageUrl(detail.getResultImage());  // ✅ 添加结果图
            vo.setEmbroideryStyle(detail.getEmbroideryStyle());
            vo.setStyleDesc(getStyleDesc(detail.getEmbroideryStyle()));
            vo.setFabricType(detail.getFabricType());
            vo.setThreadType(detail.getThreadType());
            vo.setWidth(detail.getWidth());
            vo.setHeight(detail.getHeight());
            vo.setQuantity(detail.getQuantity());
            vo.setNeedRefinement(detail.getNeedRefinement());  // ✅ 添加是否需要精修

            // 生成订单摘要
            vo.setSummary(String.format("%s定制 × %d件", order.getType().name(), detail.getQuantity()));
        } else {
            vo.setSummary(order.getType().name() + "定制");
        }

        return vo;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(OrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "待支付";
            case DESIGNING -> "设计中";
            case SAMPLING -> "打样中";
            case REVIEWING -> "复审中";
            case SCHEDULING -> "排产中";
            case EMBROIDERING -> "绣制中";
            case QUALITY_CHECK -> "质检中";
            case PENDING_SHIP -> "待发货";
            case SHIPPED -> "已发货";
            case COMPLETED -> "已完成";
            case CANCELLED -> "已取消";
        };
    }

    /**
     * 获取绣制风格描述
     */
    private String getStyleDesc(com.jinyue.cixiu.common.enums.EmbroideryStyle style) {
        if (style == null) {
            return "";
        }
        return switch (style) {
            case SU -> "苏绣";
            case XIANG -> "湘绣";
            case SHU -> "蜀绣";
            case YUE -> "粤绣";
        };
    }
}
