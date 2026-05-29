package com.jinyue.cixiu.module.chatbot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("knowledge_segment")
public class KnowledgeSegment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long documentId;

    private String content;

    private String embedding;

    private Integer segmentIndex;

    private LocalDateTime createdAt;
}
