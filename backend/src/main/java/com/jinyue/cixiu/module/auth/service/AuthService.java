package com.jinyue.cixiu.module.auth.service;

import com.jinyue.cixiu.module.auth.dto.*;
import com.jinyue.cixiu.module.auth.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param dto 注册信息
     * @return 登录信息
     */
    LoginVO register(RegisterDTO dto);

    /**
     * 密码登录
     *
     * @param dto 登录信息
     * @return 登录信息
     */
    LoginVO login(LoginDTO dto);

    /**
     * 验证码登录
     *
     * @param dto 验证码登录信息
     * @return 登录信息
     */
    LoginVO codeLogin(CodeLoginDTO dto);

    /**
     * 发送验证码
     *
     * @param dto 发送验证码信息
     * @return 验证码（用于测试环境显示）
     */
    String sendCode(SendCodeDTO dto);

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的登录信息
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 登出
     *
     * @param userId 用户ID
     */
    void logout(Long userId);
}
