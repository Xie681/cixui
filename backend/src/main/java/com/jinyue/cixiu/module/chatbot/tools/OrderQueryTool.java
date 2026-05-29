package com.jinyue.cixiu.module.chatbot.tools;

import com.jinyue.cixiu.module.order.entity.Order;
import com.jinyue.cixiu.module.order.service.OrderService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderQueryTool {

    private final OrderService orderService;

    @Tool("查询订单信息，根据订单号获取订单状态、价格和创建时间")
    public String queryOrder(String orderNo) {
        log.info("AI调用订单查询工具，订单号: {}", orderNo);
        
        try {
            Order order = orderService.getByOrderNo(orderNo);
            if (order == null) {
                return "未找到订单号为 " + orderNo + " 的订单";
            }
            
            return String.format(
                "订单号：%s\n状态：%s\n总金额：%s元\n创建时间：%s",
                order.getOrderNo(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt()
            );
        } catch (Exception e) {
            log.error("订单查询失败", e);
            return "查询订单失败: " + e.getMessage();
        }
    }
}
