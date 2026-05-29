package com.jinyue.cixiu.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinyue.cixiu.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
