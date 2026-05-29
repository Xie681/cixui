package com.jinyue.cixiu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用简单消息代理，用于向客户端推送消息
        registry.enableSimpleBroker("/topic", "/queue");

        // 设置客户端发送消息的目的地前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 设置用户目的地前缀（点对点消息）
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 注册STOMP端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册WebSocket端点
        registry.addEndpoint("/ws")
                // 允许跨域
                .setAllowedOriginPatterns("*")
                // 启用SockJS（降级方案）
                .withSockJS();
    }
}
