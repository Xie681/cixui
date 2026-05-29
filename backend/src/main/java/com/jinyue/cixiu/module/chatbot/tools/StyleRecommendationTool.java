package com.jinyue.cixiu.module.chatbot.tools;

import com.jinyue.cixiu.common.enums.EmbroideryStyle;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StyleRecommendationTool {

    @Tool("推荐刺绣风格，根据用户偏好和使用场合推荐合适的刺绣风格")
    public String recommendStyle(String preference, String occasion) {
        log.info("AI调用风格推荐工具，偏好: {}, 场合: {}", preference, occasion);
        
        try {
            StringBuilder result = new StringBuilder("刺绣风格推荐：\n\n");
            
            Arrays.stream(EmbroideryStyle.values()).forEach(style -> {
                result.append(String.format(
                    "【%s - %s】\n适合场合：%s\n\n",
                    style.name(),
                    style.getDescription(),
                    getSuitableOccasion(style)
                ));
            });
            
            return result.toString();
        } catch (Exception e) {
            log.error("风格推荐失败", e);
            return "风格推荐失败: " + e.getMessage();
        }
    }

    private String getSuitableOccasion(EmbroideryStyle style) {
        return switch (style) {
            case SU -> "适合精致典雅的场合，如婚礼、正式宴会";
            case XIANG -> "适合传统节日、文化活动";
            case SHU -> "适合日常穿着、休闲场合";
            case YUE -> "适合商务场合、礼品赠送";
        };
    }
}
