package com.grocery.groceryshop.trainticket.controller;

import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.service.TrainTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainTicket")
public class TrainTicketController {

    @Autowired
    private TrainTicketService trainTicketService;

    @GetMapping("/captcha-image64")
    public CommonResult getCaptchaCode() {
        return CommonResult.success(trainTicketService.getCaptchaImage64());
    }

    @GetMapping(value = "/ticketSearch")
    public CommonResult ticketSearch() {
        return CommonResult.success(trainTicketService.getTicketList());
    }
}
