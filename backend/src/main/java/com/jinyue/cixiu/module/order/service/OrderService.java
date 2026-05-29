package com.jinyue.cixiu.module.order.service;

import com.jinyue.cixiu.module.order.vo.OrderDetailVO;
import com.jinyue.cixiu.module.order.vo.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 获取用户订单列表
     *
     * @param userId 用户ID
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    List<OrderVO> listOrders(Long userId, String status);

    /**
     * 获取订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailVO getOrderDetail(Long userId, Long orderId);

    /**
     * 根据订单号获取订单详情
     *
     * @param userId  用户ID
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderDetailVO getOrderDetailByOrderNo(Long userId, String orderNo);

    /**
     * 取消订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 根据订单号获取订单（用于AI工具调用）
     *
     * @param orderNo 订单号
     * @return 订单实体
     */
    com.jinyue.cixiu.module.order.entity.Order getByOrderNo(String orderNo);
}
