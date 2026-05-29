package com.jinyue.cixiu.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 添加收货地址DTO
 */
@Data
public class AddAddressDTO {

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 省
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 市
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区
     */
    @NotBlank(message = "区县不能为空")
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 255, message = "详细地址长度不能超过255个字符")
    private String detail;

    /**
     * 是否默认地址
     */
    private Boolean isDefault;
}
