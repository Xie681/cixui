package com.jinyue.cixiu.module.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "上传知识库文档请求")
public class UploadKnowledgeDTO {

    @Schema(description = "文档文件", required = true)
    private MultipartFile file;

    @Schema(description = "文档描述")
    private String description;
}
