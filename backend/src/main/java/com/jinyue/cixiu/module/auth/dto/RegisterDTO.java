package com.jinyue.cixiu.module.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户注册DTO
 */
@Data
public class RegisterDTO {

    /**
     * 手机号（手机号或邮箱必填其一）
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱（手机号或邮箱必填其一）
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    private String nickname;
}
