package com.jinyue.cixiu.module.auth.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.auth.dto.*;
import com.jinyue.cixiu.module.auth.service.AuthService;
import com.jinyue.cixiu.module.auth.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "用户认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }

    @Operation(summary = "密码登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @Operation(summary = "验证码登录")
    @PostMapping("/code-login")
    public Result<LoginVO> codeLogin(@Valid @RequestBody CodeLoginDTO dto) {
        return Result.success(authService.codeLogin(dto));
    }

    @Operation(summary = "发送验证码")
    @PostMapping("/send-code")
    public Result<String> sendCode(@Valid @RequestBody SendCodeDTO dto) {
        String code = authService.sendCode(dto);
        return Result.success(code);
    }

    @Operation(summary = "刷新Token")
    @PostMapping("/refresh-token")
    public Result<LoginVO> refreshToken(@RequestParam String refreshToken) {
        return Result.success(authService.refreshToken(refreshToken));
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute("userId") Long userId) {
        authService.logout(userId);
        return Result.success();
    }
}
