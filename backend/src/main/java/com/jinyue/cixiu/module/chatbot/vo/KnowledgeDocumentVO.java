package com.jinyue.cixiu.module.chatbot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "知识库文档响应")
public class KnowledgeDocumentVO {

    @Schema(description = "文档ID")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "状态：processing-处理中, completed-已完成, failed-失败")
    private String status;

    @Schema(description = "分段数量")
    private Integer segmentCount;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
