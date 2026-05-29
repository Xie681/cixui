package com.jinyue.cixiu.module.order.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.order.service.OrderService;
import com.jinyue.cixiu.module.order.vo.OrderDetailVO;
import com.jinyue.cixiu.module.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "获取订单列表")
    @GetMapping
    public Result<List<OrderVO>> listOrders(@RequestAttribute("userId") Long userId,
                                             @RequestParam(required = false) String status) {
        return Result.success(orderService.listOrders(userId, status));
    }

    @Operation(summary = "获取订单详情（按ID）")
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderDetail(@RequestAttribute("userId") Long userId,
                                                 @PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(userId, id));
    }

    @Operation(summary = "获取订单详情（按订单号）")
    @GetMapping("/no/{orderNo}")
    public Result<OrderDetailVO> getOrderDetailByOrderNo(@RequestAttribute("userId") Long userId,
                                                          @PathVariable String orderNo) {
        return Result.success(orderService.getOrderDetailByOrderNo(userId, orderNo));
    }

    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@RequestAttribute("userId") Long userId,
                                    @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        return Result.success();
    }
}
