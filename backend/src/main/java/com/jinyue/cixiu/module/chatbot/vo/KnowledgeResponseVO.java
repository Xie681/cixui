package com.jinyue.cixiu.module.chatbot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "知识库查询响应")
public class KnowledgeResponseVO {

    @Schema(description = "回答内容")
    private String answer;

    @Schema(description = "相关知识片段")
    private List<KnowledgeItem> relatedKnowledge;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KnowledgeItem {
        @Schema(description = "知识内容")
        private String content;

        @Schema(description = "相似度分数")
        private Double score;

        @Schema(description = "来源")
        private String source;
    }
}
