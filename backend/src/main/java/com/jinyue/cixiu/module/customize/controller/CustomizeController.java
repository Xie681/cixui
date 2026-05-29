package com.jinyue.cixiu.module.customize.controller;

import com.jinyue.cixiu.common.result.Result;
import com.jinyue.cixiu.module.customize.dto.AiPreviewDTO;
import com.jinyue.cixiu.module.customize.dto.CalculatePriceDTO;
import com.jinyue.cixiu.module.customize.dto.SubmitOrderDTO;
import com.jinyue.cixiu.module.customize.dto.TextSubmitOrderDTO;
import com.jinyue.cixiu.module.customize.service.CustomizeService;
import com.jinyue.cixiu.module.customize.service.PriceService;
import com.jinyue.cixiu.module.customize.vo.AiPreviewVO;
import com.jinyue.cixiu.module.customize.vo.PriceVO;
import com.jinyue.cixiu.module.customize.vo.StyleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定制控制器
 */
@Tag(name = "定制管理", description = "刺绣定制相关接口")
@RestController
@RequestMapping("/api/customize")
@RequiredArgsConstructor
public class CustomizeController {

    private final CustomizeService customizeService;
    private final PriceService priceService;

    @Operation(summary = "获取绣制风格列表")
    @GetMapping("/styles")
    public Result<List<StyleVO>> getStyles() {
        return Result.success(customizeService.getStyles());
    }

    @Operation(summary = "获取面料列表")
    @GetMapping("/fabrics")
    public Result<List<String>> getFabrics() {
        return Result.success(customizeService.getFabrics());
    }

    @Operation(summary = "获取线材列表")
    @GetMapping("/threads")
    public Result<List<String>> getThreads() {
        return Result.success(customizeService.getThreads());
    }

    @Operation(summary = "计算报价")
    @PostMapping("/calculate-price")
    public Result<PriceVO> calculatePrice(@Valid @RequestBody CalculatePriceDTO dto) {
        return Result.success(priceService.calculatePrice(dto));
    }

    @Operation(summary = "AI预览刺绣效果")
    @PostMapping("/preview-ai-result")
    public Result<AiPreviewVO> previewAiResult(@Valid @RequestBody AiPreviewDTO dto) {
        return Result.success(customizeService.previewAiResult(dto));
    }

    @Operation(summary = "提交图片定制订单")
    @PostMapping("/submit-order")
    public Result<String> submitOrder(@RequestAttribute("userId") Long userId,
                                      @Valid @RequestBody SubmitOrderDTO dto) {
        String orderNo = customizeService.submitOrder(userId, dto);
        return Result.success(orderNo);
    }

    @Operation(summary = "提交文字定制订单")
    @PostMapping("/submit-text-order")
    public Result<String> submitTextOrder(@RequestAttribute("userId") Long userId,
                                          @Valid @RequestBody TextSubmitOrderDTO dto) {
        String orderNo = customizeService.submitTextOrder(userId, dto);
        return Result.success(orderNo);
    }
}
