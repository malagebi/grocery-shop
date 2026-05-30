package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.base.req.PayReq;
import com.grocery.groceryshop.base.req.PaymentListReq;
import com.grocery.groceryshop.service.PaymentService;
import com.grocery.groceryshop.vo.PaymentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/payment")
@Tag(name = "支付模块")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @PostMapping("/pay")
    @Operation(summary ="发起支付（异步模拟网关回调，1-3秒后更新状态）")
    public CommonResult<PaymentVO> pay(@Validated @RequestBody PayReq req) {
        return CommonResult.success(paymentService.pay(req));
    }

    @PostMapping("/refund/{paymentNo}")
    @Operation(summary ="申请退款")
    public CommonResult<PaymentVO> refund(@PathVariable("paymentNo") String paymentNo) {
        return CommonResult.success(paymentService.refund(paymentNo));
    }

    @GetMapping("/{paymentNo}")
    @Operation(summary ="查询支付记录")
    public CommonResult<PaymentVO> get(@PathVariable("paymentNo") String paymentNo) {
        return CommonResult.success(paymentService.getByPaymentNo(paymentNo));
    }

    @PostMapping("/list")
    @Operation(summary ="支付记录分页查询")
    public CommonResult<CommonPageInfo<PaymentVO>> list(@RequestBody PaymentListReq req) {
        return CommonResult.success(paymentService.listPayments(req));
    }
}
