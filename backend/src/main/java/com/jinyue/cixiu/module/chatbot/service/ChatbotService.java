package com.jinyue.cixiu.module.chatbot.service;

import com.jinyue.cixiu.module.chatbot.dto.ChatMessageDTO;
import com.jinyue.cixiu.module.chatbot.dto.KnowledgeQueryDTO;
import com.jinyue.cixiu.module.chatbot.vo.ChatResponseVO;
import com.jinyue.cixiu.module.chatbot.vo.KnowledgeResponseVO;
import reactor.core.publisher.Flux;

public interface ChatbotService {

    /**
     * 多轮对话
     */
    ChatResponseVO chat(ChatMessageDTO dto, Long userId);

    /**
     * 流式对话
     */
    Flux<String> chatStream(ChatMessageDTO dto, Long userId);

    /**
     * 知识库问答
     */
    KnowledgeResponseVO queryKnowledge(KnowledgeQueryDTO dto);

    /**
     * 清除会话历史
     */
    void clearSession(String sessionId);

    /**
     * 获取或创建会话ID
     */
    String getOrCreateSessionId(String sessionId, Long userId);
}
