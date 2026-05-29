package com.jinyue.cixiu.common.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.common.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Slf4j
@Tag(name = "文件管理", description = "文件上传相关接口")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "上传图片")
    @PostMapping("/upload/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.badRequest("只能上传图片文件");
        }

        // 验证文件大小（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.badRequest("图片大小不能超过10MB");
        }

        // 上传文件
        String fileUrl = fileStorageService.uploadFile(file, "images");

        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("fileName", file.getOriginalFilename());

        return Result.success(result);
    }

    @Operation(summary = "上传头像")
    @PostMapping("/upload/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.badRequest("只能上传图片文件");
        }

        // 验证文件大小（2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.badRequest("头像大小不能超过2MB");
        }

        // 上传文件
        String fileUrl = fileStorageService.uploadFile(file, "avatars");

        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);

        return Result.success(result);
    }

    @Operation(summary = "上传设计文件")
    @PostMapping("/upload/design")
    public Result<Map<String, String>> uploadDesign(@RequestParam("file") MultipartFile file) {
        // 验证文件大小（50MB）
        if (file.getSize() > 50 * 1024 * 1024) {
            return Result.badRequest("文件大小不能超过50MB");
        }

        // 上传文件
        String fileUrl = fileStorageService.uploadFile(file, "designs");

        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("fileName", file.getOriginalFilename());

        return Result.success(result);
    }
}
