package com.jinyue.cixiu.module.chatbot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.utils.RedisUtil;
import com.jinyue.cixiu.module.chatbot.dto.ChatMessageDTO;
import com.jinyue.cixiu.module.chatbot.dto.KnowledgeQueryDTO;
import com.jinyue.cixiu.module.chatbot.entity.ChatMessage;
import com.jinyue.cixiu.module.chatbot.entity.ChatSession;
import com.jinyue.cixiu.module.chatbot.mapper.ChatMessageMapper;
import com.jinyue.cixiu.module.chatbot.mapper.ChatSessionMapper;
import com.jinyue.cixiu.module.chatbot.service.ChatbotService;
import com.jinyue.cixiu.module.chatbot.service.KnowledgeService;
import com.jinyue.cixiu.module.chatbot.tools.*;
import com.jinyue.cixiu.module.chatbot.vo.ChatResponseVO;
import com.jinyue.cixiu.module.chatbot.vo.KnowledgeResponseVO;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatLanguageModel chatLanguageModel;
    private final StreamingChatLanguageModel streamingChatLanguageModel;
    private final ChatLanguageModel visionChatLanguageModel;
    private final StreamingChatLanguageModel visionStreamingChatLanguageModel;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatSessionMapper chatSessionMapper;
    private final RedisUtil redisUtil;
    private final KnowledgeService knowledgeService;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    
    private final OrderQueryTool orderQueryTool;
    private final PriceCalculatorTool priceCalculatorTool;
    private final StyleRecommendationTool styleRecommendationTool;
    private final ImageAnalysisTool imageAnalysisTool;

    private static final String SYSTEM_PROMPT = """
        你是一个专业的刺绣定制客服助手，名字叫"绣绣"。你的职责是：
        1. 介绍刺绣定制服务和产品
        2. 解答关于刺绣风格、价格、制作流程的问题
        3. 协助用户查询订单状态
        4. 推荐合适的刺绣风格
        5. 分析用户上传的刺绣图片
        
        重要提示：
        - 如果用户想要定制刺绣或生成效果图，请引导他们进入"定制流程"页面
        - 你主要负责咨询和查询，不负责生成效果图
        - 保持友好、专业的语气
        - 如果遇到不确定的问题，可以建议用户联系人工客服
        """;

    private static final String REDIS_KEY_PREFIX = "chatbot:session:";
    
    // 会话内存管理
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();

    @Override
    public ChatResponseVO chat(ChatMessageDTO dto, Long userId) {
        String sessionId = getOrCreateSessionId(dto.getSessionId(), userId);
        
        // 获取或创建会话内存
        ChatMemory chatMemory = getOrCreateChatMemory(sessionId);
        
        // 判断是否包含图片（多模态）
        boolean hasImages = dto.getImageUrls() != null && !dto.getImageUrls().isEmpty();
        
        String reply;
        if (hasImages) {
            // 使用多模态模型
            reply = chatWithVision(dto, chatMemory);
        } else {
            // 使用普通文本模型（只包含咨询相关的工具）
            Assistant assistant = AiServices.builder(Assistant.class)
                    .chatLanguageModel(chatLanguageModel)
                    .chatMemory(chatMemory)
                    .tools(orderQueryTool, priceCalculatorTool, styleRecommendationTool, imageAnalysisTool)
                    .build();
            
            reply = assistant.chat(dto.getMessage());
        }
        
        // 保存消息
        saveMessage(sessionId, "user", dto.getMessage());
        saveMessage(sessionId, "assistant", reply);
        
        // 构建响应
        return ChatResponseVO.builder()
                .sessionId(sessionId)
                .reply(reply)
                .toolsUsed(new ArrayList<>())
                .timestamp(LocalDateTime.now())
                .tokenUsage(null)
                .build();
    }

    @Override
    public Flux<String> chatStream(ChatMessageDTO dto, Long userId) {
        String sessionId = getOrCreateSessionId(dto.getSessionId(), userId);
        
        // 获取或创建会话内存
        ChatMemory chatMemory = getOrCreateChatMemory(sessionId);
        
        // 保存用户消息到数据库
        saveMessage(sessionId, "user", dto.getMessage());
        
        // 判断是否包含图片（多模态）
        boolean hasImages = dto.getImageUrls() != null && !dto.getImageUrls().isEmpty();
        
        return Flux.create(sink -> {
            StringBuilder fullResponse = new StringBuilder();
            
            try {
                if (hasImages) {
                    // 使用多模态流式模型
                    chatStreamWithVision(dto, chatMemory, sink, fullResponse, sessionId);
                } else {
                    // 使用普通文本流式模型（只包含咨询相关的工具）
                    // 注意：不要手动添加用户消息，AiServices 会自动处理
                    StreamingAssistant streamingAssistant = AiServices.builder(StreamingAssistant.class)
                            .streamingChatLanguageModel(streamingChatLanguageModel)
                            .chatMemory(chatMemory)
                            .tools(orderQueryTool, priceCalculatorTool, styleRecommendationTool, imageAnalysisTool)
                            .build();
                    
                    streamingAssistant.chat(dto.getMessage())
                            .onNext(token -> {
                                fullResponse.append(token);
                                sink.next(token);
                            })
                            .onComplete(response -> {
                                // 保存到数据库
                                saveMessage(sessionId, "assistant", fullResponse.toString());
                                sink.complete();
                            })
                            .onError(error -> {
                                log.error("流式对话出错", error);
                                sink.error(error);
                            })
                            .start();
                }
            } catch (Exception e) {
                log.error("创建流式对话失败", e);
                sink.error(e);
            }
        });
    }

    @Override
    public KnowledgeResponseVO queryKnowledge(KnowledgeQueryDTO dto) {
        // 1. 将查询文本向量化
        Embedding queryEmbedding = embeddingModel.embed(dto.getQuery()).content();
        
        // 2. 使用 EmbeddingStore 进行向量检索（使用新的 API）
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(dto.getTopK())
                .minScore(0.0)
                .build();
        
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
        
        // 3. 构建知识项
        List<KnowledgeResponseVO.KnowledgeItem> knowledgeItems = new ArrayList<>();
        for (EmbeddingMatch<TextSegment> match : searchResult.matches()) {
            knowledgeItems.add(KnowledgeResponseVO.KnowledgeItem.builder()
                    .content(match.embedded().text())
                    .score(match.score())
                    .source("知识库文档")
                    .build());
        }
        
        // 4. 如果没有找到相关内容，返回默认回答
        if (knowledgeItems.isEmpty()) {
            return KnowledgeResponseVO.builder()
                    .answer("抱歉，我在知识库中没有找到相关信息。请尝试上传相关文档或联系人工客服。")
                    .relatedKnowledge(new ArrayList<>())
                    .build();
        }
        
        // 5. 使用 AI 基于检索到的内容生成回答
        String answer = generateAnswerFromKnowledge(dto.getQuery(), knowledgeItems);
        
        return KnowledgeResponseVO.builder()
                .answer(answer)
                .relatedKnowledge(knowledgeItems)
                .build();
    }

    @Override
    public void clearSession(String sessionId) {
        redisUtil.delete(REDIS_KEY_PREFIX + sessionId);
        sessionMemories.remove(sessionId);
        log.info("清除会话: {}", sessionId);
    }

    @Override
    public String getOrCreateSessionId(String sessionId, Long userId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }
        
        String newSessionId = "session-" + UUID.randomUUID().toString();
        
        ChatSession session = ChatSession.builder()
                .sessionId(newSessionId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(0)
                .build();
        
        chatSessionMapper.insert(session);
        
        return newSessionId;
    }

    private ChatMemory getOrCreateChatMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId, key -> {
            // 创建消息窗口内存（保留最近10条消息）
            ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
            
            // 添加系统提示
            memory.add(SystemMessage.from(SYSTEM_PROMPT));
            
            // 从数据库加载历史消息
            List<ChatMessage> historyMessages = chatMessageMapper.selectList(
                    new LambdaQueryWrapper<ChatMessage>()
                            .eq(ChatMessage::getSessionId, sessionId)
                            .orderByAsc(ChatMessage::getCreatedAt)
                            .last("LIMIT 10")
            );
            
            // 添加历史消息到内存
            for (ChatMessage msg : historyMessages) {
                if ("user".equals(msg.getRole())) {
                    memory.add(UserMessage.from(msg.getContent()));
                } else if ("assistant".equals(msg.getRole())) {
                    memory.add(AiMessage.from(msg.getContent()));
                }
            }
            
            return memory;
        });
    }

    private void saveMessage(String sessionId, String role, String content) {
        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .role(role)
                .content(content)
                .tokenCount(content.length())
                .createdAt(LocalDateTime.now())
                .build();
        
        chatMessageMapper.insert(message);
    }

    private String generateAnswerFromKnowledge(String query, List<KnowledgeResponseVO.KnowledgeItem> knowledge) {
        if (knowledge.isEmpty()) {
            return "抱歉，我没有找到相关信息。请联系人工客服获取帮助。";
        }
        
        StringBuilder context = new StringBuilder("根据以下知识库信息回答用户问题：\n");
        for (KnowledgeResponseVO.KnowledgeItem item : knowledge) {
            context.append("- ").append(item.getContent()).append("\n");
        }
        context.append("\n用户问题：").append(query);
        
        // 创建临时 AI 服务
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .build();
        
        return assistant.chat(context.toString());
    }

    // AI 服务接口
    interface Assistant {
        String chat(String userMessage);
    }

    // 流式 AI 服务接口
    interface StreamingAssistant {
        TokenStream chat(String userMessage);
    }

    /**
     * 多模态对话（带图片）
     */
    private String chatWithVision(ChatMessageDTO dto, ChatMemory chatMemory) {
        // 构建包含图片的消息
        dev.langchain4j.data.message.UserMessage userMessage = buildUserMessageWithImages(
                dto.getMessage(), 
                dto.getImageUrls()
        );
        
        // 添加到会话记忆
        chatMemory.add(userMessage);
        
        // 调用多模态模型
        dev.langchain4j.model.output.Response<AiMessage> response = 
                visionChatLanguageModel.generate(chatMemory.messages());
        
        // 添加AI回复到记忆
        chatMemory.add(response.content());
        
        return response.content().text();
    }

    /**
     * 多模态流式对话
     */
    private void chatStreamWithVision(
            ChatMessageDTO dto, 
            ChatMemory chatMemory,
            reactor.core.publisher.FluxSink<String> sink,
            StringBuilder fullResponse,
            String sessionId
    ) {
        // 构建包含图片的消息
        dev.langchain4j.data.message.UserMessage userMessage = buildUserMessageWithImages(
                dto.getMessage(), 
                dto.getImageUrls()
        );
        
        // 添加到会话记忆
        chatMemory.add(userMessage);
        
        // 调用多模态流式模型
        visionStreamingChatLanguageModel.generate(
                chatMemory.messages(),
                new StreamingResponseHandler<AiMessage>() {
                    @Override
                    public void onNext(String token) {
                        fullResponse.append(token);
                        sink.next(token);
                    }

                    @Override
                    public void onComplete(Response<AiMessage> response) {
                        // 添加 AI 响应到会话记忆
                        chatMemory.add(response.content());
                        // 保存到数据库
                        saveMessage(sessionId, "assistant", fullResponse.toString());
                        sink.complete();
                    }

                    @Override
                    public void onError(Throwable error) {
                        log.error("多模态流式对话出错", error);
                        sink.error(error);
                    }
                }
        );
    }

    /**
     * 构建包含图片的用户消息
     */
    private dev.langchain4j.data.message.UserMessage buildUserMessageWithImages(
            String text, 
            List<String> imageUrls
    ) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return dev.langchain4j.data.message.UserMessage.from(text);
        }

        // 构建多模态内容
        List<dev.langchain4j.data.message.Content> contents = new ArrayList<>();
        
        // 添加文本内容
        contents.add(dev.langchain4j.data.message.TextContent.from(text));
        
        // 添加图片内容
        for (String imageUrl : imageUrls) {
            contents.add(dev.langchain4j.data.message.ImageContent.from(imageUrl));
        }
        
        return dev.langchain4j.data.message.UserMessage.from(contents);
    }
}
