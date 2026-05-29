package com.jinyue.cixiu.module.chatbot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinyue.cixiu.module.chatbot.entity.KnowledgeSegment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface KnowledgeSegmentMapper extends BaseMapper<KnowledgeSegment> {

    /**
     * 查询所有分段（用于向量检索）
     */
    @Select("SELECT id, document_id, content, embedding, segment_index FROM knowledge_segment")
    List<KnowledgeSegment> selectAllSegments();

    /**
     * 根据文档ID查询分段
     */
    @Select("SELECT id, document_id, content, embedding, segment_index FROM knowledge_segment WHERE document_id = #{documentId}")
    List<KnowledgeSegment> selectByDocumentId(@Param("documentId") Long documentId);
}
