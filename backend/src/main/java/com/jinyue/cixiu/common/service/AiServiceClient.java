package com.jinyue.cixiu.common.service;

import cn.hutool.core.codec.Base64;
import com.jinyue.cixiu.common.exception.BusinessException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * AI服务客户端
 * 负责与Python AI服务进行通信
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceClient {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${ai.service.timeout:30000}")
    private Integer timeout;

    @Value("${file.storage.local.internal-url:http://frontend}")
    private String localFileInternalUrl;

    private final RestTemplate restTemplate;
    private final FileStorageService fileStorageService;

    /**
     * 处理图片转刺绣
     *
     * @param imageUrl 原图URL（阿里云OSS URL）
     * @param style    刺绣风格
     * @return AI处理结果（包含结果图OSS URL、针数、颜色数）
     */
    public AiProcessResult processImageToEmbroidery(String imageUrl, String style) {
        try {
            String url = aiServiceUrl + "/api/image-to-embroidery";

            // 1. 调用AI服务
            Map<String, Object> request = new HashMap<>();
            request.put("image_url", resolveAiImageUrl(imageUrl));
            request.put("style", style);

            log.info("调用AI服务: URL={}, Style={}", imageUrl, style);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> result = response.getBody();

            if (result == null || !Boolean.TRUE.equals(result.get("success"))) {
                String error = result != null ? (String) result.get("error") : "未知错误";
                throw new BusinessException(500, "AI处理失败: " + error);
            }

            // 2. 获取base64结果
            String base64Data = (String) result.get("result_base64");
            if (base64Data == null) {
                throw new BusinessException(500, "AI服务未返回处理结果");
            }

            // 3. 将base64转为字节数组
            byte[] imageBytes = Base64.decode(base64Data);

            // 4. 上传到OSS
            String originalFilename = (String) result.get("original_filename");
            if (originalFilename == null) {
                originalFilename = "embroidery_result_" + System.currentTimeMillis() + ".png";
            }

            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            String resultUrl = fileStorageService.uploadFile(
                    inputStream,
                    originalFilename,
                    "designs",  // 存储到 designs 目录
                    "image/png"
            );

            log.info("AI处理结果已上传至OSS: {}", resultUrl);

            // 5. 上传成功后，异步清理AI服务器上的临时文件
            cleanupAiResultFile(originalFilename);

            // 5. 构造返回结果
            AiProcessResult processResult = new AiProcessResult();
            processResult.setSuccess(true);
            processResult.setResultUrl(resultUrl);
            processResult.setStitchCount((Integer) result.get("stitch_count"));
            processResult.setColorCount((Integer) result.get("color_count"));

            return processResult;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用AI服务异常", e);
            throw new BusinessException(500, "AI服务调用失败: " + e.getMessage());
        }
    }

    /**
     * 清理AI服务器上的结果文件
     *
     * @param filename 文件名
     */
    private void cleanupAiResultFile(String filename) {
        try {
            String url = aiServiceUrl + "/api/cleanup";
            Map<String, Object> request = new HashMap<>();
            request.put("filename", filename);

            log.info("请求清理AI服务器文件: {}", filename);

            // 异步调用，不阻塞主流程
            new Thread(() -> {
                try {
                    ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
                    Map<String, Object> result = response.getBody();

                    if (result != null && Boolean.TRUE.equals(result.get("success"))) {
                        log.info("✅ AI服务器文件已清理: {}", filename);
                    } else {
                        log.warn("⚠️ AI服务器文件清理失败: {}", result != null ? result.get("error") : "未知错误");
                    }
                } catch (Exception e) {
                    log.warn("清理AI服务器文件时出错（不影响主流程）: {}", e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            // 清理失败不影响主流程
            log.warn("请求清理AI文件失败（不影响主流程）: {}", e.getMessage());
        }
    }

    private String resolveAiImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            return imageUrl;
        }

        String baseUrl = localFileInternalUrl == null ? "" : localFileInternalUrl.replaceAll("/+$", "");
        String path = imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl;
        return baseUrl + path;
    }

    /**
     * 处理文字转刺绣
     *
     * @param text  文字内容
     * @param font  字体
     * @param size  字号
     * @param color 颜色
     * @param style 刺绣风格
     * @return AI处理结果
     */
    public AiProcessResult processTextToEmbroidery(String text, String font, Integer size, String color, String style) {
        try {
            String url = aiServiceUrl + "/api/text-to-embroidery";

            Map<String, Object> request = new HashMap<>();
            request.put("text", text);
            request.put("font", font != null ? font : "Arial");
            request.put("size", size != null ? size : 48);
            request.put("color", color != null ? color : "black");
            request.put("style", style);

            log.info("调用AI服务（文字转刺绣）: Text={}, Font={}, Size={}, Color={}", text, font, size, color);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> result = response.getBody();

            if (result == null || !Boolean.TRUE.equals(result.get("success"))) {
                String error = result != null ? (String) result.get("error") : "未知错误";
                throw new BusinessException(500, "AI处理失败: " + error);
            }

            // 3. 获取AI返回的图片数据（base64编码）
            String base64Data = (String) result.get("result_base64");
            if (base64Data == null || base64Data.isEmpty()) {
                throw new BusinessException(500, "AI服务未返回结果图片");
            }

            // 解码base64图片（hutool的Base64使用decode方法）
            byte[] imageBytes = Base64.decode(base64Data);

            // 4. 上传到OSS
            String originalFilename = (String) result.get("original_filename");
            if (originalFilename == null) {
                originalFilename = "text_embroidery_" + System.currentTimeMillis() + ".png";
            }

            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            String resultUrl = fileStorageService.uploadFile(
                    inputStream,
                    originalFilename,
                    "designs",  // 存储到 designs 目录
                    "image/png"
            );

            log.info("文字转刺绣结果已上传至OSS: {}", resultUrl);

            // 5. 上传成功后，异步清理AI服务器上的临时文件
            cleanupAiResultFile(originalFilename);

            // 6. 构造返回结果
            AiProcessResult processResult = new AiProcessResult();
            processResult.setSuccess(true);
            processResult.setResultUrl(resultUrl);
            processResult.setStitchCount((Integer) result.get("stitch_count"));
            processResult.setColorCount((Integer) result.get("color_count"));

            log.info("文字转刺绣成功: ResultUrl={}, StitchCount={}, ColorCount={}",
                    processResult.getResultUrl(), processResult.getStitchCount(), processResult.getColorCount());

            return processResult;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用AI服务异常（文字转刺绣）", e);
            throw new BusinessException(500, "AI服务调用失败: " + e.getMessage());
        }
    }

    /**
     * AI处理结果
     */
    @Data
    public static class AiProcessResult {
        /**
         * 处理是否成功
         */
        private Boolean success;

        /**
         * 结果图OSS URL
         */
        private String resultUrl;

        /**
         * 估算针数
         */
        private Integer stitchCount;

        /**
         * 估算颜色数
         */
        private Integer colorCount;
    }
}
