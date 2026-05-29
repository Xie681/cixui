package com.jinyue.cixiu.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.common.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@ConditionalOnProperty(name = "file.storage.type", havingValue = "local")
public class LocalFileStorageService implements FileStorageService {

    private final Path rootPath;
    private final String publicUrl;

    public LocalFileStorageService(
            @Value("${file.storage.local.path:/app/uploads}") String rootPath,
            @Value("${file.storage.local.public-url:/uploads}") String publicUrl) {
        this.rootPath = Paths.get(rootPath).toAbsolutePath().normalize();
        this.publicUrl = normalizePublicUrl(publicUrl);
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "文件不能为空");
        }

        try {
            return uploadFile(file.getInputStream(), file.getOriginalFilename(), path, file.getContentType());
        } catch (IOException e) {
            log.error("读取文件失败", e);
            throw new BusinessException(500, "文件上传失败");
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String path, String contentType) {
        String objectName = buildObjectName(fileName, path);
        Path target = rootPath.resolve(objectName).normalize();

        if (!target.startsWith(rootPath)) {
            throw new BusinessException(400, "无效的文件路径");
        }

        try (InputStream in = inputStream) {
            Files.createDirectories(target.getParent());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("本地文件上传成功: {}", target);
            return getFileUrl(objectName);
        } catch (IOException e) {
            log.error("本地文件上传失败", e);
            throw new BusinessException(500, "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return;
        }

        String objectName = extractObjectName(fileUrl);
        Path target = rootPath.resolve(objectName).normalize();

        if (!target.startsWith(rootPath)) {
            log.warn("跳过无效本地文件删除路径: {}", fileUrl);
            return;
        }

        try {
            Files.deleteIfExists(target);
            log.info("本地文件删除成功: {}", target);
        } catch (IOException e) {
            log.warn("本地文件删除失败: {}", target, e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        String normalized = objectName.replace("\\", "/");
        return publicUrl + "/" + normalized;
    }

    private String buildObjectName(String fileName, String path) {
        String extension = getFileExtension(fileName);
        String uniqueFileName = IdUtil.simpleUUID() + extension;
        String dateStr = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String safePath = sanitizePath(path);

        return StrUtil.isBlank(safePath)
                ? dateStr + "/" + uniqueFileName
                : safePath + "/" + dateStr + "/" + uniqueFileName;
    }

    private String getFileExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex > 0 ? fileName.substring(dotIndex) : "";
    }

    private String sanitizePath(String path) {
        if (StrUtil.isBlank(path)) {
            return "";
        }
        return path.replace("\\", "/")
                .replaceAll("^/+", "")
                .replaceAll("/+$", "");
    }

    private String extractObjectName(String fileUrl) {
        String value = fileUrl.replace("\\", "/");
        if (value.startsWith(publicUrl + "/")) {
            value = value.substring(publicUrl.length() + 1);
        } else if (value.startsWith(publicUrl)) {
            value = value.substring(publicUrl.length());
        }
        return value.replaceAll("^/+", "");
    }

    private String normalizePublicUrl(String value) {
        String normalized = StrUtil.blankToDefault(value, "/uploads").replaceAll("/+$", "");
        return normalized.isEmpty() ? "/uploads" : normalized;
    }
}
