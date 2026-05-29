package com.jinyue.cixiu.module.user.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.user.dto.AddAddressDTO;
import com.jinyue.cixiu.module.user.dto.UpdateAddressDTO;
import com.jinyue.cixiu.module.user.dto.UpdateProfileDTO;
import com.jinyue.cixiu.module.user.service.AddressService;
import com.jinyue.cixiu.module.user.service.UserService;
import com.jinyue.cixiu.module.user.vo.AddressVO;
import com.jinyue.cixiu.module.user.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户中心控制器
 */
@Tag(name = "用户中心", description = "用户个人中心相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile(@RequestAttribute("userId") Long userId) {
        return Result.success(userService.getProfile(userId));
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestAttribute("userId") Long userId,
                                      @Valid @RequestBody UpdateProfileDTO dto) {
        userService.updateProfile(userId, dto);
        return Result.success();
    }

    @Operation(summary = "获取收货地址列表")
    @GetMapping("/addresses")
    public Result<List<AddressVO>> listAddresses(@RequestAttribute("userId") Long userId) {
        return Result.success(addressService.listAddresses(userId));
    }

    @Operation(summary = "获取地址详情")
    @GetMapping("/addresses/{id}")
    public Result<AddressVO> getAddress(@RequestAttribute("userId") Long userId,
                                        @PathVariable Long id) {
        return Result.success(addressService.getAddress(userId, id));
    }

    @Operation(summary = "添加收货地址")
    @PostMapping("/addresses")
    public Result<Long> addAddress(@RequestAttribute("userId") Long userId,
                                   @Valid @RequestBody AddAddressDTO dto) {
        Long addressId = addressService.addAddress(userId, dto);
        return Result.success(addressId);
    }

    @Operation(summary = "更新收货地址")
    @PutMapping("/addresses/{id}")
    public Result<Void> updateAddress(@RequestAttribute("userId") Long userId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody UpdateAddressDTO dto) {
        dto.setId(id);
        addressService.updateAddress(userId, dto);
        return Result.success();
    }

    @Operation(summary = "删除收货地址")
    @DeleteMapping("/addresses/{id}")
    public Result<Void> deleteAddress(@RequestAttribute("userId") Long userId,
                                      @PathVariable Long id) {
        addressService.deleteAddress(userId, id);
        return Result.success();
    }

    @Operation(summary = "设置默认地址")
    @PutMapping("/addresses/{id}/default")
    public Result<Void> setDefaultAddress(@RequestAttribute("userId") Long userId,
                                          @PathVariable Long id) {
        addressService.setDefaultAddress(userId, id);
        return Result.success();
    }
}
