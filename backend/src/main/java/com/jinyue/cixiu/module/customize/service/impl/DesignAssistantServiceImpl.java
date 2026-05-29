package com.jinyue.cixiu.module.customize.service.impl;

import com.jinyue.cixiu.module.chatbot.tools.RequirementExtractionTool;
import com.jinyue.cixiu.module.customize.dto.DesignChatDTO;
import com.jinyue.cixiu.module.customize.service.DesignAssistantService;
import com.jinyue.cixiu.module.customize.vo.DesignChatVO;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设计助手服务实现
 * 专注于定制流程中的需求理解和整理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DesignAssistantServiceImpl implements DesignAssistantService {

    private final ChatLanguageModel chatLanguageModel;
    private final RequirementExtractionTool requirementExtractionTool;

    private static final String SYSTEM_PROMPT = """
        你是一个专业的刺绣设计助手，名字叫"绣绣"。你的职责是：
        1. 理解用户的刺绣定制需求
        2. 提取关键信息（图案主题、风格、颜色、尺寸等）
        3. 帮助用户完善需求细节
        4. 整理成结构化的需求信息
        
        工作流程：
        1. 当用户描述需求时，使用 extractRequirements 工具提取结构化信息
        2. 如果信息不完整，友好地询问缺失的信息（如尺寸、颜色偏好等）
        3. 信息完整后，总结需求并告知用户可以提交订单
        4. 不要生成效果图，用户会通过其他方式上传参考图或由设计师制作
        
        注意事项：
        - 保持友好、专业的语气
        - 主动引导用户提供必要信息
        - 不要重复询问已知信息
        - 需求整理完成后，提示用户可以提交订单
        - 告知用户可以上传参考图片，或由专业设计师根据需求制作效果图
        """;

    // 会话内存管理
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();
    private final Map<String, DesignContext> designContexts = new ConcurrentHashMap<>();

    @Override
    public DesignChatVO chat(DesignChatDTO dto, Long userId) {
        String sessionId = getOrCreateSessionId(dto.getSessionId());

        // 获取或创建会话内存
        ChatMemory chatMemory = getOrCreateChatMemory(sessionId);

        // 获取设计上下文
        DesignContext context = designContexts.computeIfAbsent(sessionId, k -> new DesignContext());

        // 创建 AI 服务（只包含需求提取工具，不包含图片生成）
        DesignAssistant assistant = AiServices.builder(DesignAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .tools(requirementExtractionTool)  // 只保留需求提取工具
                .build();

        // 调用 AI
        String reply = assistant.chat(dto.getMessage());

        // 提取需求信息（如果有）
        DesignChatVO.RequirementInfo requirement = extractRequirement(reply, context);

        // 提取价格和工期
        String estimatedPrice = extractEstimatedPrice(reply);
        String estimatedTime = extractEstimatedTime(reply);

        // 构建响应（不包含效果图URL）
        return DesignChatVO.builder()
                .sessionId(sessionId)
                .reply(reply)
                .designImageUrl(null)  // 不生成效果图
                .requirement(requirement)
                .estimatedPrice(estimatedPrice)
                .estimatedTime(estimatedTime)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public void clearSession(String sessionId) {
        sessionMemories.remove(sessionId);
        designContexts.remove(sessionId);
        log.info("清除设计会话: {}", sessionId);
    }

    /**
     * 获取或创建会话ID
     */
    private String getOrCreateSessionId(String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }
        return "design-session-" + UUID.randomUUID().toString();
    }

    /**
     * 获取或创建会话内存
     */
    private ChatMemory getOrCreateChatMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId, key -> {
            ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
            memory.add(SystemMessage.from(SYSTEM_PROMPT));
            return memory;
        });
    }

    /**
     * 从回复中提取需求信息
     */
    private DesignChatVO.RequirementInfo extractRequirement(String reply, DesignContext context) {
        // 提取主题
        String theme = extractField(reply, "图案主题[：:](.*?)\\n");
        if (theme != null) context.setTheme(theme);

        // 提取风格
        String style = extractField(reply, "刺绣风格[：:](.*?)\\n");
        if (style != null) context.setStyle(style);

        // 提取颜色
        String colors = extractField(reply, "颜色要求[：:](.*?)\\n");
        if (colors != null) context.setColors(colors);

        // 提取尺寸
        String size = extractField(reply, "尺寸要求[：:](.*?)\\n");
        if (size != null) context.setSize(size);

        // 提取其他细节
        String details = extractField(reply, "其他细节[：:](.*?)\\n");
        if (details != null) context.setAdditionalDetails(details);

        // 如果有任何信息，返回需求对象
        if (context.hasAnyInfo()) {
            return DesignChatVO.RequirementInfo.builder()
                    .theme(context.getTheme())
                    .style(context.getStyle())
                    .colors(context.getColors())
                    .size(context.getSize())
                    .additionalDetails(context.getAdditionalDetails())
                    .build();
        }

        return null;
    }

    /**
     * 提取字段值
     */
    private String extractField(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    /**
     * 提取预估价格
     */
    private String extractEstimatedPrice(String reply) {
        return extractField(reply, "预估价格[：:](.*?)\\n");
    }

    /**
     * 提取预估工期
     */
    private String extractEstimatedTime(String reply) {
        return extractField(reply, "预估工期[：:](.*?)\\n");
    }

    /**
     * 设计上下文（保存当前设计的状态）
     */
    private static class DesignContext {
        private String theme;
        private String style;
        private String colors;
        private String size;
        private String additionalDetails;

        public boolean hasAnyInfo() {
            return theme != null || style != null || colors != null || 
                   size != null || additionalDetails != null;
        }

        // Getters and Setters
        public String getTheme() { return theme; }
        public void setTheme(String theme) { this.theme = theme; }
        public String getStyle() { return style; }
        public void setStyle(String style) { this.style = style; }
        public String getColors() { return colors; }
        public void setColors(String colors) { this.colors = colors; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getAdditionalDetails() { return additionalDetails; }
        public void setAdditionalDetails(String additionalDetails) { this.additionalDetails = additionalDetails; }
    }

    /**
     * 设计助手 AI 服务接口
     */
    interface DesignAssistant {
        String chat(String userMessage);
    }
}
