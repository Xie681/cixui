package com.jinyue.cixiu.module.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.module.user.dto.UpdateProfileDTO;
import com.jinyue.cixiu.module.user.entity.User;
import com.jinyue.cixiu.module.user.mapper.UserMapper;
import com.jinyue.cixiu.module.user.service.UserService;
import com.jinyue.cixiu.module.user.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return user;
    }

    @Override
    public UserProfileVO getProfile(Long userId) {
        User user = getUserById(userId);
        return BeanUtil.copyProperties(user, UserProfileVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, UpdateProfileDTO dto) {
        User user = getUserById(userId);

        // 检查手机号是否被其他用户使用
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getPhone())) {
            User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, dto.getPhone())
                    .ne(User::getId, userId));
            if (existUser != null) {
                throw new BusinessException(400, "手机号已被其他用户使用");
            }
        }

        // 检查邮箱是否被其他用户使用
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, dto.getEmail())
                    .ne(User::getId, userId));
            if (existUser != null) {
                throw new BusinessException(400, "邮箱已被其他用户使用");
            }
        }

        // 更新用户信息
        BeanUtil.copyProperties(dto, user, "id");
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("用户 {} 更新个人信息成功", userId);
    }
}
