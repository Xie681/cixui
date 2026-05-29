package com.jinyue.cixiu.module.customize.service;

import com.jinyue.cixiu.module.customize.dto.DesignChatDTO;
import com.jinyue.cixiu.module.customize.vo.DesignChatVO;

/**
 * 设计助手服务接口
 * 专门用于定制流程中的需求理解和效果图生成
 */
public interface DesignAssistantService {

    /**
     * 设计对话（理解需求并生成效果图）
     *
     * @param dto 对话请求
     * @param userId 用户ID
     * @return 对话响应（包含效果图）
     */
    DesignChatVO chat(DesignChatDTO dto, Long userId);

    /**
     * 清除设计会话
     *
     * @param sessionId 会话ID
     */
    void clearSession(String sessionId);
}
