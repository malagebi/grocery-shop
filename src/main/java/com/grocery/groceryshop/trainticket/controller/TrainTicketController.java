package com.grocery.groceryshop.trainticket.controller;

import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.service.TrainTicketService;
import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.TicketListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/trainTicket")
public class TrainTicketController {

    @Autowired
    private TrainTicketService trainTicketService;

    @GetMapping("/captcha-image64")
    public CommonResult<CaptchaResp> getCaptchaCode() {
        return CommonResult.success(trainTicketService.getCaptchaImage64());
    }

    @GetMapping(value = "/ticketSearch")
    public CommonResult<List<TicketListResp>> ticketSearch() {
        return CommonResult.success(trainTicketService.getTicketList());
    }
}
