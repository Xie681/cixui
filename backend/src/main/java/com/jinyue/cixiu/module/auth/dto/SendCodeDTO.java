package com.jinyue.cixiu.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 发送验证码DTO
 */
@Data
public class SendCodeDTO {

    /**
     * 接收方（手机号或邮箱）
     */
    @NotBlank(message = "接收方不能为空")
    private String receiver;

    /**
     * 验证码类型：1注册 2登录 3重置密码
     */
    @NotNull(message = "验证码类型不能为空")
    private Integer type;
}
