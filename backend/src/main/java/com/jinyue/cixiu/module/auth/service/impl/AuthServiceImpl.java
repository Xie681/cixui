package com.jinyue.cixiu.module.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.constant.RedisKeyConstant;
import com.jinyue.cixiu.common.enums.UserRole;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.common.utils.JwtUtil;
import com.jinyue.cixiu.common.utils.RedisUtil;
import com.jinyue.cixiu.module.auth.dto.*;
import com.jinyue.cixiu.module.auth.service.AuthService;
import com.jinyue.cixiu.module.auth.vo.LoginVO;
import com.jinyue.cixiu.module.user.entity.User;
import com.jinyue.cixiu.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterDTO dto) {
        // 验证验证码
        validateCode(dto.getPhone() != null ? dto.getPhone() : dto.getEmail(), dto.getCode());

        // 检查账号是否已存在
        if (dto.getPhone() != null) {
            User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, dto.getPhone()));
            if (existUser != null) {
                throw new BusinessException(400, "手机号已注册");
            }
        }

        if (dto.getEmail() != null) {
            User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, dto.getEmail()));
            if (existUser != null) {
                throw new BusinessException(400, "邮箱已注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StrUtil.isNotBlank(dto.getNickname()) ? dto.getNickname() : "用户" + RandomUtil.randomString(8));
        user.setRole(UserRole.USER);
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        // 生成Token并返回
        return generateLoginVO(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // 查询用户
        User user = getUserByAccount(dto.getAccount());
        if (user == null) {
            throw new BusinessException(400, "账号不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        return generateLoginVO(user);
    }

    @Override
    public LoginVO codeLogin(CodeLoginDTO dto) {
        // 验证验证码
        validateCode(dto.getAccount(), dto.getCode());

        // 查询用户
        User user = getUserByAccount(dto.getAccount());
        if (user == null) {
            throw new BusinessException(400, "账号不存在");
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        return generateLoginVO(user);
    }

    @Override
    public String sendCode(SendCodeDTO dto) {
        String receiver = dto.getReceiver();
        Integer type = dto.getType();

        // 检查发送频率（60秒内不能重复发送）
        String lockKey = RedisKeyConstant.CODE_LOCK_PREFIX + receiver;
        if (redisUtil.hasKey(lockKey)) {
            throw new BusinessException(400, "验证码发送过于频繁，请稍后再试");
        }

        // 生成6位验证码
        String code = RandomUtil.randomNumbers(6);

        // 保存验证码到Redis（5分钟有效期）
        String codeKey = RedisKeyConstant.CODE_PREFIX + receiver;
        redisUtil.set(codeKey, code, 5, TimeUnit.MINUTES);

        // 设置发送锁（60秒）
        redisUtil.set(lockKey, "1", 60, TimeUnit.SECONDS);

        // TODO: 实际发送验证码（短信或邮件）
        // 这里暂时只记录日志
        log.info("发送验证码到 {}: {}", receiver, code);

        // 返回验证码（用于测试环境显示）
        return code;
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        // 验证refreshToken
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(401, "刷新令牌无效");
        }

        // 从token中获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);

        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(400, "用户不存在");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(403, "账号已被禁用");
        }

        return generateLoginVO(user);
    }

    @Override
    public void logout(Long userId) {
        // 清除Redis中的用户信息
        String userKey = RedisKeyConstant.USER_PREFIX + userId;
        redisUtil.delete(userKey);

        log.info("用户 {} 已登出", userId);
    }

    /**
     * 根据账号查询用户（支持手机号和邮箱）
     */
    private User getUserByAccount(String account) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, account)
                .or()
                .eq(User::getEmail, account)
                .last("LIMIT 1"));
    }

    /**
     * 验证验证码
     */
    private void validateCode(String receiver, String code) {
        String codeKey = RedisKeyConstant.CODE_PREFIX + receiver;
        String cachedCode = (String) redisUtil.get(codeKey);

        if (StrUtil.isBlank(cachedCode)) {
            throw new BusinessException(400, "验证码已过期");
        }

        if (!cachedCode.equals(code)) {
            throw new BusinessException(400, "验证码错误");
        }

        // 验证成功后删除验证码
        redisUtil.delete(codeKey);
    }

    /**
     * 生成登录响应
     */
    private LoginVO generateLoginVO(User user) {
        // 生成访问令牌
        String accessToken = jwtUtil.generateToken(user.getId());

        // 生成刷新令牌（有效期为访问令牌的2倍）
        String refreshToken = jwtUtil.generateToken(user.getId(), jwtExpiration * 2);

        // 保存用户信息到Redis
        String userKey = RedisKeyConstant.USER_PREFIX + user.getId();
        redisUtil.set(userKey, user, jwtExpiration, TimeUnit.MILLISECONDS);

        return LoginVO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000)
                .userId(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }
}
