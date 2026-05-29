package com.jinyue.cixiu.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.jinyue.cixiu.common.config.OssConfig;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.common.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS文件存储服务实现
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "file.storage.type", havingValue = "oss", matchIfMissing = true)
@RequiredArgsConstructor
public class AliyunOssFileStorageService implements FileStorageService {

    private final OSS ossClient;
    private final OssConfig ossConfig;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "文件不能为空");
        }

        try {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();

            return uploadFile(inputStream, fileName, path, contentType);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            throw new BusinessException(500, "文件上传失败");
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String path, String contentType) {
        try {
            // 生成唯一文件名
            String extension = getFileExtension(fileName);
            String uniqueFileName = IdUtil.simpleUUID() + extension;

            // 构建对象名称（路径 + 日期 + 文件名）
            String dateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd");
            String objectName = StrUtil.isBlank(path)
                    ? dateStr + "/" + uniqueFileName
                    : path + "/" + dateStr + "/" + uniqueFileName;

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(inputStream.available());
            // 设置缓存控制
            metadata.setCacheControl("max-age=31536000");

            // 上传文件到OSS
            PutObjectResult result = ossClient.putObject(
                    ossConfig.getBucketName(),
                    objectName,
                    inputStream,
                    metadata
            );

            log.info("文件上传成功: {}, ETag: {}", objectName, result.getETag());
            return getFileUrl(objectName);

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(500, "文件上传失败: " + e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("关闭输入流失败", e);
            }
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return;
        }

        try {
            // 从URL中提取对象名称
            String objectName = extractObjectName(fileUrl);

            // 删除文件
            ossClient.deleteObject(ossConfig.getBucketName(), objectName);

            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败: {}", fileUrl, e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        // 如果配置了自定义域名，使用自定义域名
        if (StrUtil.isNotBlank(ossConfig.getCustomDomain())) {
            return ossConfig.getCustomDomain() + "/" + objectName;
        }

        // 否则使用默认的OSS域名
        // 格式：https://{bucket}.{endpoint}/{objectName}
        String endpoint = ossConfig.getEndpoint();
        // 去掉endpoint中的http://或https://前缀
        if (endpoint.startsWith("http://")) {
            endpoint = endpoint.substring(7);
        } else if (endpoint.startsWith("https://")) {
            endpoint = endpoint.substring(8);
        }

        return "https://" + ossConfig.getBucketName() + "." + endpoint + "/" + objectName;
    }

    /**
     * 生成签名URL（用于私有文件访问）
     *
     * @param objectName 对象名称
     * @param expireSeconds 过期时间（秒）
     * @return 签名URL
     */
    public String generatePresignedUrl(String objectName, int expireSeconds) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + expireSeconds * 1000L);
            URL url = ossClient.generatePresignedUrl(ossConfig.getBucketName(), objectName, expiration);
            return url.toString();
        } catch (Exception e) {
            log.error("生成签名URL失败: {}", objectName, e);
            throw new BusinessException(500, "生成访问链接失败");
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }

    /**
     * 从URL中提取对象名称
     */
    private String extractObjectName(String fileUrl) {
        // 如果是自定义域名
        if (StrUtil.isNotBlank(ossConfig.getCustomDomain()) && fileUrl.startsWith(ossConfig.getCustomDomain())) {
            return fileUrl.substring(ossConfig.getCustomDomain().length() + 1);
        }

        // 如果是OSS默认域名
        String bucketDomain = ossConfig.getBucketName() + "." + ossConfig.getEndpoint().replace("http://", "").replace("https://", "");
        if (fileUrl.contains(bucketDomain)) {
            int index = fileUrl.indexOf(bucketDomain);
            return fileUrl.substring(index + bucketDomain.length() + 1);
        }

        // 如果已经是对象名称，直接返回
        return fileUrl;
    }
}
