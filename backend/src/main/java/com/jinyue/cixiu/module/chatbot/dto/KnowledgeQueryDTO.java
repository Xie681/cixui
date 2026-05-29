package com.jinyue.cixiu.module.chatbot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "知识库查询请求")
public class KnowledgeQueryDTO {

    @NotBlank(message = "查询内容不能为空")
    @Schema(description = "查询问题", example = "刺绣定制的价格是多少？")
    private String query;

    @Schema(description = "返回结果数量", example = "3")
    private Integer topK = 3;
}
