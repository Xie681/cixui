package com.jinyue.cixiu.module.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新收货地址DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateAddressDTO extends AddAddressDTO {

    /**
     * 地址ID
     */
    @NotNull(message = "地址ID不能为空")
    private Long id;
}
