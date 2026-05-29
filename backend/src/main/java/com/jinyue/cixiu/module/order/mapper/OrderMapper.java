package com.jinyue.cixiu.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jinyue.cixiu.module.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
