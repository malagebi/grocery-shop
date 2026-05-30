package com.grocery.groceryshop.service.impl;

import com.alibaba.fastjson.JSON;
import com.grocery.groceryshop.feign.Train12306Client;
import com.grocery.groceryshop.service.TrainTicketService;
import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.ResidualTicketResp;
import com.grocery.groceryshop.trainticket.resp.TicketListResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrainTicketServiceImpl implements TrainTicketService {

    @Resource
    private Train12306Client train12306Client;

    @Override
    public CaptchaResp getCaptchaImage64() {
        CaptchaResp captchaResp = train12306Client.getCaptchaImage64("login", "sjrand");
        log.info(JSON.toJSONString(captchaResp));
        return captchaResp;
    }

    @Override
    public List<TicketListResp> getTicketList() {
        ResidualTicketResp residualTicketResp = train12306Client.queryLeftTicket(
                "2019-09-30", "BJP", "HDP", "ADULT"
        );
        List<String> list = residualTicketResp.getData().getResult();
        Map<String, String> map = residualTicketResp.getData().getMap();
        List<TicketListResp> respList = new ArrayList<>();
        for (String str : list) {
            StringBuilder stb = new StringBuilder();
            String[] strArrays = str.split("\\|");

            TicketListResp resp = new TicketListResp();
            resp.setTrainName(strArrays[3]);
            resp.setFromStation(map.get(strArrays[6]));
            resp.setToStation(map.get(strArrays[7]));
            resp.setStrDepartureTime(strArrays[8]);
            resp.setStrArrivalsTime(strArrays[9]);
            resp.setDuration(strArrays[10]);
            resp.setSwNum(strArrays[32]);
            resp.setFwNum(strArrays[31]);
            resp.setSewNum(strArrays[30]);
            resp.setGjrwNum(strArrays[21]);
            resp.setRwNum(strArrays[23]);
            resp.setDwNum(strArrays[22]);
            resp.setYwNum(strArrays[28]);
            resp.setRzNum(strArrays[27]);
            resp.setYzNum(strArrays[29]);
            resp.setWzNum(strArrays[26]);

            stb.append(strArrays[3]).append("\t");
            stb.append(map.get(strArrays[6])).append("\t");
            stb.append(map.get(strArrays[7])).append("\t");
            stb.append(strArrays[8]).append("\t");
            stb.append(strArrays[9]).append("\t");
            stb.append(strArrays[10]).append("\t");
            stb.append(strArrays[11]).append("\t");
            stb.append(strArrays[21]).append("\t");
            stb.append(strArrays[23]).append("\t");
            stb.append(strArrays[26]).append("\t");
            stb.append(strArrays[28]).append("\t");
            stb.append(strArrays[29]).append("\t");
            stb.append(strArrays[30]).append("\t");
            stb.append(strArrays[31]).append("\t");
            stb.append(strArrays[32]).append("\t");
            log.info("[车票信息] {}", stb);
            respList.add(resp);
        }
        return respList;
    }
}
