package com.jinyue.cixiu.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate 配置
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建 RestTemplate Bean
     * 用于 HTTP 客户端调用（如调用 AI 服务）
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(30))   // 连接超时 30 秒
                .setReadTimeout(Duration.ofSeconds(120))     // 读取超时 120 秒（AI处理需要更长时间）
                .build();
    }
}
