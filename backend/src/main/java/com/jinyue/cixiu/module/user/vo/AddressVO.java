package com.jinyue.cixiu.module.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收货地址VO
 */
@Data
public class AddressVO {

    /**
     * 地址ID
     */
    private Long id;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 完整地址（省市区+详细地址）
     */
    private String fullAddress;

    /**
     * 是否默认地址
     */
    private Boolean isDefault;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
