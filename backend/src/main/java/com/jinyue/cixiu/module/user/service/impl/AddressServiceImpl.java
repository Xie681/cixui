package com.jinyue.cixiu.module.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jinyue.cixiu.common.exception.BusinessException;
import com.jinyue.cixiu.module.user.dto.AddAddressDTO;
import com.jinyue.cixiu.module.user.dto.UpdateAddressDTO;
import com.jinyue.cixiu.module.user.entity.UserAddress;
import com.jinyue.cixiu.module.user.mapper.UserAddressMapper;
import com.jinyue.cixiu.module.user.service.AddressService;
import com.jinyue.cixiu.module.user.vo.AddressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserAddressMapper addressMapper;

    @Override
    public List<AddressVO> listAddresses(Long userId) {
        List<UserAddress> addresses = addressMapper.selectList(
                new LambdaQueryWrapper<UserAddress>()
                        .eq(UserAddress::getUserId, userId)
                        .orderByDesc(UserAddress::getIsDefault)
                        .orderByDesc(UserAddress::getCreatedAt)
        );

        return addresses.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressVO getAddress(Long userId, Long addressId) {
        UserAddress address = getAddressEntity(userId, addressId);
        return convertToVO(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAddress(Long userId, AddAddressDTO dto) {
        UserAddress address = BeanUtil.copyProperties(dto, UserAddress.class);
        address.setUserId(userId);
        address.setCreatedAt(LocalDateTime.now());
        address.setIsDefault(dto.getIsDefault() != null && dto.getIsDefault() ? 1 : 0);

        // 如果设置为默认地址，先取消其他默认地址
        if (address.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        addressMapper.insert(address);
        log.info("用户 {} 添加收货地址成功", userId);
        return address.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long userId, UpdateAddressDTO dto) {
        UserAddress existAddress = getAddressEntity(userId, dto.getId());

        // 更新地址信息
        BeanUtil.copyProperties(dto, existAddress, "id", "userId");
        existAddress.setIsDefault(dto.getIsDefault() != null && dto.getIsDefault() ? 1 : 0);

        // 如果设置为默认地址，先取消其他默认地址
        if (existAddress.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }

        addressMapper.updateById(existAddress);
        log.info("用户 {} 更新收货地址 {} 成功", userId, dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = getAddressEntity(userId, addressId);
        addressMapper.deleteById(addressId);
        log.info("用户 {} 删除收货地址 {} 成功", userId, addressId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = getAddressEntity(userId, addressId);

        // 取消其他默认地址
        clearDefaultAddress(userId);

        // 设置当前地址为默认
        address.setIsDefault(1);
        addressMapper.updateById(address);

        log.info("用户 {} 设置默认地址 {} 成功", userId, addressId);
    }

    /**
     * 获取地址实体（验证权限）
     */
    private UserAddress getAddressEntity(Long userId, Long addressId) {
        UserAddress address = addressMapper.selectById(addressId);
        if (address == null) {
            throw new BusinessException(404, "地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作此地址");
        }
        return address;
    }

    /**
     * 取消用户的所有默认地址
     */
    private void clearDefaultAddress(Long userId) {
        addressMapper.update(null, new LambdaUpdateWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1)
                .set(UserAddress::getIsDefault, 0));
    }

    /**
     * 转换为VO
     */
    private AddressVO convertToVO(UserAddress address) {
        AddressVO vo = BeanUtil.copyProperties(address, AddressVO.class);
        vo.setIsDefault(address.getIsDefault() == 1);
        vo.setFullAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetail());
        return vo;
    }
}
