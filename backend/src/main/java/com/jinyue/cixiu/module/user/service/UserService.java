package com.jinyue.cixiu.module.user.service;

import com.jinyue.cixiu.module.user.dto.UpdateProfileDTO;
import com.jinyue.cixiu.module.user.entity.User;
import com.jinyue.cixiu.module.user.vo.UserProfileVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据ID获取用户
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    User getUserById(Long userId);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息VO
     */
    UserProfileVO getProfile(Long userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param dto    更新信息
     */
    void updateProfile(Long userId, UpdateProfileDTO dto);
}
