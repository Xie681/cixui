package com.jinyue.cixiu.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 存储路径（不含文件名）
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String path);

    /**
     * 上传文件（从输入流）
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @param path        存储路径
     * @param contentType 内容类型
     * @return 文件访问URL
     */
    String uploadFile(InputStream inputStream, String fileName, String path, String contentType);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl);

    /**
     * 获取文件访问URL
     *
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    String getFileUrl(String objectName);
}
