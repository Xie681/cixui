package com.jinyue.cixiu.module.user.service;

import com.jinyue.cixiu.module.user.dto.AddAddressDTO;
import com.jinyue.cixiu.module.user.dto.UpdateAddressDTO;
import com.jinyue.cixiu.module.user.vo.AddressVO;

import java.util.List;

/**
 * 收货地址服务接口
 */
public interface AddressService {

    /**
     * 获取用户的所有收货地址
     *
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressVO> listAddresses(Long userId);

    /**
     * 获取地址详情
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 地址详情
     */
    AddressVO getAddress(Long userId, Long addressId);

    /**
     * 添加收货地址
     *
     * @param userId 用户ID
     * @param dto    地址信息
     * @return 地址ID
     */
    Long addAddress(Long userId, AddAddressDTO dto);

    /**
     * 更新收货地址
     *
     * @param userId 用户ID
     * @param dto    地址信息
     */
    void updateAddress(Long userId, UpdateAddressDTO dto);

    /**
     * 删除收货地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    void deleteAddress(Long userId, Long addressId);

    /**
     * 设置默认地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     */
    void setDefaultAddress(Long userId, Long addressId);
}
