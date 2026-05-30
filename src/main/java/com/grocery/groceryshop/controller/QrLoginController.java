package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.service.QrLoginService;
import com.grocery.groceryshop.vo.QrGenerateVO;
import com.grocery.groceryshop.vo.QrPollVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/qr")
@Tag(name = "扫码登录")
public class QrLoginController {

    @Resource
    private QrLoginService qrLoginService;

    @GetMapping("/generate")
    @Operation(summary ="生成登录二维码（PC端调用）")
    public CommonResult<QrGenerateVO> generate() throws Exception {
        return CommonResult.success(qrLoginService.generate());
    }

    @GetMapping("/poll")
    @Operation(summary ="轮询扫码状态（PC端每隔 1-2s 调用）")
    public CommonResult<QrPollVO> poll(@RequestParam String token) {
        return CommonResult.success(qrLoginService.poll(token));
    }

    @PostMapping("/scan")
    @Operation(summary ="扫码（手机端扫到 token 后调用）")
    public CommonResult<Void> scan(@RequestParam String token, @RequestParam Long userId) {
        qrLoginService.scan(token, userId);
        return CommonResult.success();
    }

    @PostMapping("/confirm")
    @Operation(summary ="确认登录（手机端用户点击确认后调用）")
    public CommonResult<String> confirm(@RequestParam String token, @RequestParam Long userId) {
        String authToken = qrLoginService.confirm(token, userId);
        return CommonResult.success(authToken);
    }
}
