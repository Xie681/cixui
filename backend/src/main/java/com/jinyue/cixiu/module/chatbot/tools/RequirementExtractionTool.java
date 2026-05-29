package com.jinyue.cixiu.module.chatbot.tools;

import com.alibaba.fastjson2.JSON;
import dev.langchain4j.agent.tool.Tool;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求提取工具
 * 用于从用户的自然语言描述中提取结构化的刺绣定制需求
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequirementExtractionTool {

    @Tool("从用户的描述中提取刺绣定制需求，包括图案主题、风格、颜色、尺寸等信息")
    public String extractRequirements(
            String userDescription,
            String theme,
            String style,
            String colors,
            String size,
            String additionalDetails
    ) {
        log.info("提取刺绣需求 - 用户描述: {}", userDescription);
        
        // 构建结构化需求
        EmbroideryRequirement requirement = new EmbroideryRequirement();
        requirement.setOriginalDescription(userDescription);
        requirement.setTheme(theme != null ? theme : "未指定");
        requirement.setStyle(style != null ? style : "未指定");
        requirement.setColors(colors != null ? colors : "未指定");
        requirement.setSize(size != null ? size : "未指定");
        requirement.setAdditionalDetails(additionalDetails != null ? additionalDetails : "无");
        
        // 生成整理后的需求描述
        StringBuilder summary = new StringBuilder();
        summary.append("【刺绣定制需求整理】\n\n");
        summary.append("📝 原始描述：").append(userDescription).append("\n\n");
        summary.append("🎨 图案主题：").append(requirement.getTheme()).append("\n");
        summary.append("🖌️ 刺绣风格：").append(requirement.getStyle()).append("\n");
        summary.append("🌈 颜色要求：").append(requirement.getColors()).append("\n");
        summary.append("📏 尺寸要求：").append(requirement.getSize()).append("\n");
        summary.append("💡 其他细节：").append(requirement.getAdditionalDetails()).append("\n\n");
        
        // 生成AI绘图提示词
        String imagePrompt = generateImagePrompt(requirement);
        summary.append("🤖 AI绘图提示词：\n").append(imagePrompt).append("\n\n");
        
        // 给出建议
        summary.append("💰 预估价格：").append(estimatePrice(requirement)).append("\n");
        summary.append("⏱️ 预估工期：").append(estimateTime(requirement)).append("\n\n");
        summary.append("✅ 需求已整理完成，正在为您生成AI效果图...");
        
        log.info("需求提取完成: {}", JSON.toJSONString(requirement));
        
        return summary.toString();
    }

    /**
     * 生成AI绘图提示词
     */
    private String generateImagePrompt(EmbroideryRequirement requirement) {
        StringBuilder prompt = new StringBuilder();
        
        // 基础描述
        prompt.append("Chinese embroidery design, ");
        
        // 主题
        if (!"未指定".equals(requirement.getTheme())) {
            prompt.append(requirement.getTheme()).append(", ");
        }
        
        // 风格
        if (!"未指定".equals(requirement.getStyle())) {
            String styleEn = mapStyleToEnglish(requirement.getStyle());
            prompt.append(styleEn).append(" style, ");
        }
        
        // 颜色
        if (!"未指定".equals(requirement.getColors())) {
            prompt.append("colors: ").append(requirement.getColors()).append(", ");
        }
        
        // 通用描述
        prompt.append("intricate details, traditional Chinese art, ");
        prompt.append("high quality, artistic, elegant, ");
        prompt.append("embroidery texture, silk thread effect");
        
        return prompt.toString();
    }

    /**
     * 映射中文风格到英文
     */
    private String mapStyleToEnglish(String chineseStyle) {
        if (chineseStyle.contains("苏绣")) {
            return "Suzhou embroidery, delicate and elegant";
        } else if (chineseStyle.contains("湘绣")) {
            return "Hunan embroidery, vivid and realistic";
        } else if (chineseStyle.contains("蜀绣")) {
            return "Sichuan embroidery, bright and colorful";
        } else if (chineseStyle.contains("粤绣")) {
            return "Guangdong embroidery, rich and magnificent";
        } else if (chineseStyle.contains("现代")) {
            return "modern";
        } else if (chineseStyle.contains("简约")) {
            return "minimalist";
        } else if (chineseStyle.contains("古典")) {
            return "classical";
        } else {
            return "traditional";
        }
    }

    /**
     * 估算价格
     */
    private String estimatePrice(EmbroideryRequirement requirement) {
        int basePrice = 150;
        
        // 根据复杂度调整
        if (requirement.getAdditionalDetails().contains("复杂") || 
            requirement.getAdditionalDetails().contains("精细")) {
            basePrice += 100;
        }
        
        // 根据尺寸调整
        if (requirement.getSize().contains("大") || requirement.getSize().matches(".*[5-9]\\d+.*")) {
            basePrice += 50;
        }
        
        return basePrice + "-" + (basePrice + 100) + "元";
    }

    /**
     * 估算工期
     */
    private String estimateTime(EmbroideryRequirement requirement) {
        int days = 7;
        
        // 根据复杂度调整
        if (requirement.getAdditionalDetails().contains("复杂") || 
            requirement.getAdditionalDetails().contains("精细")) {
            days += 3;
        }
        
        // 根据尺寸调整
        if (requirement.getSize().contains("大") || requirement.getSize().matches(".*[5-9]\\d+.*")) {
            days += 2;
        }
        
        return days + "-" + (days + 3) + "天";
    }

    /**
     * 刺绣需求结构化数据
     */
    @Data
    public static class EmbroideryRequirement {
        private String originalDescription;  // 原始描述
        private String theme;                 // 图案主题
        private String style;                 // 刺绣风格
        private String colors;                // 颜色要求
        private String size;                  // 尺寸要求
        private String additionalDetails;     // 其他细节
    }
}
