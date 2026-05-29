package com.jinyue.cixiu.common.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置
 */
@Data
@Configuration
@ConditionalOnProperty(name = "file.storage.type", havingValue = "oss", matchIfMissing = true)
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {

    /**
     * OSS服务地域节点
     */
    private String endpoint;

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * Bucket名称
     */
    private String bucketName;

    /**
     * 自定义域名（可选，如果配置了CDN加速域名）
     */
    private String customDomain;

    /**
     * 创建OSS客户端
     */
    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
