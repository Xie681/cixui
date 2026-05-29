package com.jinyue.cixiu.module.chatbot.tools;

import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 图片分析工具
 * 用于分析刺绣图片的风格、复杂度等信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImageAnalysisTool {

    @Tool("分析刺绣图片的风格、复杂度和适合的刺绣类型")
    public String analyzeEmbroideryImage(String imageDescription) {
        log.info("分析刺绣图片: {}", imageDescription);
        
        // 这里可以调用实际的图片分析服务
        // 目前返回基于描述的建议
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("根据图片分析结果：\n\n");
        
        // 判断复杂度
        if (imageDescription.contains("复杂") || imageDescription.contains("细节多")) {
            analysis.append("【复杂度】：高\n");
            analysis.append("【建议针数】：15000-30000针\n");
            analysis.append("【预计工期】：7-10天\n");
        } else if (imageDescription.contains("简单") || imageDescription.contains("简约")) {
            analysis.append("【复杂度】：低\n");
            analysis.append("【建议针数】：5000-10000针\n");
            analysis.append("【预计工期】：3-5天\n");
        } else {
            analysis.append("【复杂度】：中等\n");
            analysis.append("【建议针数】：10000-15000针\n");
            analysis.append("【预计工期】：5-7天\n");
        }
        
        // 判断风格
        if (imageDescription.contains("花卉") || imageDescription.contains("花朵")) {
            analysis.append("【推荐风格】：苏绣（擅长花卉刺绣，色彩柔和）\n");
            analysis.append("【推荐针法】：平针、套针、施针\n");
        } else if (imageDescription.contains("动物") || imageDescription.contains("人物")) {
            analysis.append("【推荐风格】：湘绣（擅长动物和人物，立体感强）\n");
            analysis.append("【推荐针法】：掺针、游针、毛针\n");
        } else if (imageDescription.contains("山水") || imageDescription.contains("风景")) {
            analysis.append("【推荐风格】：蜀绣（擅长山水风景，层次分明）\n");
            analysis.append("【推荐针法】：晕针、纱针、盖针\n");
        } else {
            analysis.append("【推荐风格】：粤绣（色彩鲜艳，适合多种题材）\n");
            analysis.append("【推荐针法】：垫针、勒针、咬针\n");
        }
        
        // 颜色建议
        if (imageDescription.contains("彩色") || imageDescription.contains("多色")) {
            analysis.append("【色数建议】：8-12色\n");
            analysis.append("【线材建议】：真丝线（色彩丰富，光泽度好）\n");
        } else {
            analysis.append("【色数建议】：3-6色\n");
            analysis.append("【线材建议】：棉线（经济实惠，适合简单图案）\n");
        }
        
        // 面料建议
        analysis.append("【面料建议】：真丝缎（高档，适合精细刺绣）或棉麻布（经济实用）\n");
        
        return analysis.toString();
    }

    @Tool("估算刺绣图片的制作价格范围")
    public String estimateImagePrice(String complexity, int estimatedStitches, int colorCount) {
        log.info("估算价格 - 复杂度: {}, 针数: {}, 色数: {}", complexity, estimatedStitches, colorCount);
        
        // 基础价
        double basePrice = 100.0;
        
        // 针数费（每1000针2元）
        double stitchFee = (estimatedStitches / 1000.0) * 2.0;
        
        // 色数费（每色5元）
        double colorFee = colorCount * 5.0;
        
        // 复杂度系数
        double complexityFactor = 1.0;
        if ("高".equals(complexity) || "复杂".equals(complexity)) {
            complexityFactor = 1.5;
        } else if ("低".equals(complexity) || "简单".equals(complexity)) {
            complexityFactor = 0.8;
        }
        
        double totalPrice = (basePrice + stitchFee + colorFee) * complexityFactor;
        
        return String.format(
                "根据图片分析，预估价格范围：\n\n" +
                "【基础价】：%.2f元\n" +
                "【针数费】：%.2f元（约%d针）\n" +
                "【色数费】：%.2f元（%d色）\n" +
                "【复杂度系数】：%.1f倍\n" +
                "【预估总价】：%.2f - %.2f元\n\n" +
                "注：实际价格需要上传图片后由AI精确计算，以上仅为参考价格。",
                basePrice, stitchFee, estimatedStitches, colorFee, colorCount,
                complexityFactor, totalPrice * 0.9, totalPrice * 1.1
        );
    }
}
