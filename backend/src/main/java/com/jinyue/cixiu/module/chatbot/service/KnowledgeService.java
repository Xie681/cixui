package com.jinyue.cixiu.module.chatbot.service;

import com.jinyue.cixiu.module.chatbot.vo.KnowledgeDocumentVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface KnowledgeService {

    /**
     * 上传知识库文档
     */
    KnowledgeDocumentVO uploadDocument(MultipartFile file, String description);

    /**
     * 获取知识库文档列表
     */
    List<KnowledgeDocumentVO> listDocuments();

    /**
     * 删除知识库文档
     */
    void deleteDocument(Long documentId);

    /**
     * 向量检索相关内容
     */
    List<String> searchSimilarContent(String query, int topK);
}
