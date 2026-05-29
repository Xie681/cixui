package com.jinyue.cixiu.module.chatbot.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.chatbot.service.KnowledgeService;
import com.jinyue.cixiu.module.chatbot.vo.KnowledgeDocumentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
@Tag(name = "知识库管理", description = "知识库文档上传和管理接口")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping("/upload")
    @Operation(summary = "上传知识库文档", description = "支持 txt, pdf, doc, docx, md 格式")
    public Result<KnowledgeDocumentVO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description
    ) {
        KnowledgeDocumentVO result = knowledgeService.uploadDocument(file, description);
        return Result.success(result);
    }

    @GetMapping("/documents")
    @Operation(summary = "获取知识库文档列表", description = "查看所有已上传的文档")
    public Result<List<KnowledgeDocumentVO>> listDocuments() {
        List<KnowledgeDocumentVO> documents = knowledgeService.listDocuments();
        return Result.success(documents);
    }

    @DeleteMapping("/documents/{id}")
    @Operation(summary = "删除知识库文档", description = "删除指定文档及其所有分段")
    public Result<Void> deleteDocument(@PathVariable Long id) {
        knowledgeService.deleteDocument(id);
        return Result.success();
    }
}
