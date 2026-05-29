package com.jinyue.cixiu.module.chatbot.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.common.service.FileStorageService;
import com.jinyue.cixiu.module.chatbot.entity.KnowledgeDocument;
import com.jinyue.cixiu.module.chatbot.entity.KnowledgeSegment;
import com.jinyue.cixiu.module.chatbot.mapper.KnowledgeDocumentMapper;
import com.jinyue.cixiu.module.chatbot.mapper.KnowledgeSegmentMapper;
import com.jinyue.cixiu.module.chatbot.service.KnowledgeService;
import com.jinyue.cixiu.module.chatbot.vo.KnowledgeDocumentVO;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final KnowledgeDocumentMapper documentMapper;
    private final KnowledgeSegmentMapper segmentMapper;
    private final FileStorageService fileStorageService;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KnowledgeDocumentVO uploadDocument(MultipartFile file, String description) {
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        String fileType = getFileExtension(fileName);
        
        // 验证文件类型
        if (!isSupportedFileType(fileType)) {
            throw new BusinessException("不支持的文件类型，仅支持: txt, pdf, doc, docx, md");
        }

        try {
            // 1. 上传文件到 OSS
            String fileUrl = fileStorageService.uploadFile(file, "knowledge");
            
            // 2. 创建文档记录
            KnowledgeDocument document = KnowledgeDocument.builder()
                    .fileName(fileName)
                    .fileType(fileType)
                    .fileUrl(fileUrl)
                    .fileSize(file.getSize())
                    .status("processing")
                    .segmentCount(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .deleted(0)
                    .build();
            
            documentMapper.insert(document);
            
            // 3. 异步处理文档（解析、分段、向量化）
            processDocumentAsync(document.getId(), file);
            
            return convertToVO(document);
            
        } catch (Exception e) {
            log.error("上传知识库文档失败", e);
            throw new BusinessException("上传文档失败: " + e.getMessage());
        }
    }

    @Override
    public List<KnowledgeDocumentVO> listDocuments() {
        List<KnowledgeDocument> documents = documentMapper.selectList(
                new LambdaQueryWrapper<KnowledgeDocument>()
                        .eq(KnowledgeDocument::getDeleted, 0)
                        .orderByDesc(KnowledgeDocument::getCreatedAt)
        );
        
        return documents.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long documentId) {
        KnowledgeDocument document = documentMapper.selectById(documentId);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }
        
        // 删除文档记录
        document.setDeleted(1);
        document.setUpdatedAt(LocalDateTime.now());
        documentMapper.updateById(document);
        
        // 删除相关分段
        segmentMapper.delete(new LambdaQueryWrapper<KnowledgeSegment>()
                .eq(KnowledgeSegment::getDocumentId, documentId));
        
        log.info("删除知识库文档: {}", documentId);
    }

    @Override
    public List<String> searchSimilarContent(String query, int topK) {
        // 1. 将查询文本向量化
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        
        // 2. 使用 EmbeddingStore 进行向量检索（使用新的 API）
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .minScore(0.0)
                .build();
        
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
        
        // 3. 提取匹配的文本内容
        return searchResult.matches().stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.toList());
    }

    /**
     * 异步处理文档
     */
    private void processDocumentAsync(Long documentId, MultipartFile file) {
        new Thread(() -> {
            try {
                processDocument(documentId, file);
            } catch (Exception e) {
                log.error("处理文档失败: {}", documentId, e);
                updateDocumentStatus(documentId, "failed");
            }
        }).start();
    }

    /**
     * 处理文档：解析、分段、向量化
     */
    private void processDocument(Long documentId, MultipartFile file) throws IOException {
        log.info("开始处理文档: {}", documentId);
        
        // 1. 解析文档
        Document document = parseDocument(file);
        
        // 2. 分段
        DocumentSplitter splitter = DocumentSplitters.recursive(
                500,  // 每段最大字符数
                50    // 段落重叠字符数
        );
        List<TextSegment> segments = splitter.split(document);
        
        log.info("文档分段完成，共 {} 段", segments.size());
        
        // 3. 为每个分段生成向量并添加到 EmbeddingStore
        List<Embedding> embeddings = new ArrayList<>();
        List<TextSegment> textSegments = new ArrayList<>();
        
        for (int i = 0; i < segments.size(); i++) {
            TextSegment segment = segments.get(i);
            
            // 生成向量
            Embedding embedding = embeddingModel.embed(segment.text()).content();
            embeddings.add(embedding);
            textSegments.add(segment);
            
            // 同时保存到数据库（用于管理和查询）
            KnowledgeSegment knowledgeSegment = KnowledgeSegment.builder()
                    .documentId(documentId)
                    .content(segment.text())
                    .embedding("")  // 向量存储在 Elasticsearch
                    .segmentIndex(i)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            segmentMapper.insert(knowledgeSegment);
        }
        
        // 4. 批量添加到 EmbeddingStore（Elasticsearch）
        embeddingStore.addAll(embeddings, textSegments);
        
        log.info("向量存储完成，共 {} 段", textSegments.size());
        
        // 5. 更新文档状态
        KnowledgeDocument doc = documentMapper.selectById(documentId);
        doc.setStatus("completed");
        doc.setSegmentCount(segments.size());
        doc.setUpdatedAt(LocalDateTime.now());
        documentMapper.updateById(doc);
        
        log.info("文档处理完成: {}", documentId);
    }

    /**
     * 解析文档
     */
    private Document parseDocument(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = getFileExtension(fileName);
        
        try (InputStream inputStream = file.getInputStream()) {
            if ("txt".equalsIgnoreCase(fileType) || "md".equalsIgnoreCase(fileType)) {
                // 文本文件直接解析
                return new TextDocumentParser().parse(inputStream);
            } else {
                // 其他文件使用 Apache Tika 解析
                return new ApacheTikaDocumentParser().parse(inputStream);
            }
        }
    }



    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 检查是否支持的文件类型
     */
    private boolean isSupportedFileType(String fileType) {
        return List.of("txt", "pdf", "doc", "docx", "md").contains(fileType.toLowerCase());
    }

    /**
     * 更新文档状态
     */
    private void updateDocumentStatus(Long documentId, String status) {
        KnowledgeDocument document = documentMapper.selectById(documentId);
        if (document != null) {
            document.setStatus(status);
            document.setUpdatedAt(LocalDateTime.now());
            documentMapper.updateById(document);
        }
    }

    /**
     * 转换为 VO
     */
    private KnowledgeDocumentVO convertToVO(KnowledgeDocument document) {
        return KnowledgeDocumentVO.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .status(document.getStatus())
                .segmentCount(document.getSegmentCount())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
