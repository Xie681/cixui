package com.jinyue.cixiu.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证码登录DTO
 */
@Data
public class CodeLoginDTO {

    /**
     * 账号（手机号或邮箱）
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
