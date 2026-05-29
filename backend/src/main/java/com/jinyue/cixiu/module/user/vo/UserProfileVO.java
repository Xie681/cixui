package com.jinyue.cixiu.module.user.vo;

import com.jinyue.cixiu.common.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 */
@Data
public class UserProfileVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色
     */
    private UserRole role;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
