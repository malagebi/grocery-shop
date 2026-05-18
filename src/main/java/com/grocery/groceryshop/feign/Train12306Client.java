package com.grocery.groceryshop.feign;

import com.grocery.groceryshop.config.FeignConfig;
import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.ResidualTicketResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "train12306", url = "https://kyfw.12306.cn", configuration = FeignConfig.class)
public interface Train12306Client {

    @GetMapping("/passport/captcha/captcha-image64")
    CaptchaResp getCaptchaImage64(
            @RequestParam("module") String module,
            @RequestParam("rand") String rand
    );

    @GetMapping("/otn/leftTicket/queryT")
    ResidualTicketResp queryLeftTicket(
            @RequestParam("leftTicketDTO.train_date") String trainDate,
            @RequestParam("leftTicketDTO.from_station") String fromStation,
            @RequestParam("leftTicketDTO.to_station") String toStation,
            @RequestParam("purpose_codes") String purposeCodes
    );
}
