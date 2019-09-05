package com.grocery.groceryshop.service.impl;

import com.alibaba.fastjson.JSON;
import com.grocery.groceryshop.service.TrainTicketService;
import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.ResidualTicketResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrainTicketServiceImpl implements TrainTicketService {

  private String captchaUrl =
      "https://kyfw.12306.cn/passport/captcha/captcha-image64?module=login&rand=sjrand&1567648347409&_=1567648304874";
  private String ticketQueryUrl = "https://kyfw.12306.cn/otn/leftTicket/queryT?";
  @Resource private RestTemplate restTemplate;

  @Override
  public CaptchaResp getCaptchaImage64() {
    CaptchaResp captchaResp = restTemplate.getForObject(captchaUrl, CaptchaResp.class);
    log.info(JSON.toJSONString(captchaResp));
    return captchaResp;
  }

  @Override
  public ResidualTicketResp getTicketList() {
    ticketQueryUrl +=
        "leftTicketDTO.train_date=2019-09-30&leftTicketDTO.from_station=BJP&leftTicketDTO.to_station=HDP&purpose_codes=ADULT";
    ResidualTicketResp residualTicketResp =
        restTemplate.getForObject(ticketQueryUrl, ResidualTicketResp.class);
    List<String> list = residualTicketResp.getData().getResult();
    Map<String, String> map = residualTicketResp.getData().getMap();
    for (String str : list) {
      StringBuilder stb = new StringBuilder();
      String[] strArrays = str.split("\\|");
      stb.append(strArrays[3] + "\t"); // 车次
      stb.append(map.get(strArrays[6]) + "\t"); // 出发地
      stb.append(map.get(strArrays[7]) + "\t"); // 目的地
      stb.append(strArrays[8] + "\t"); // 出发时间
      stb.append(strArrays[9] + "\t"); // 到达时间
      stb.append(strArrays[10] + "\t"); // 历时
      stb.append(strArrays[11] + "\t"); // Y
      stb.append(strArrays[21] + "\t"); // 高级软卧
      stb.append(strArrays[23] + "\t"); // 软卧
      stb.append(strArrays[26] + "\t"); // 无座
      stb.append(strArrays[28] + "\t"); // 硬卧
      stb.append(strArrays[29] + "\t"); // 硬座
      stb.append(strArrays[30] + "\t"); // 二等座
      stb.append(strArrays[31] + "\t"); // 一等座
      stb.append(strArrays[32] + "\t"); // 商务，特等座
      System.out.println(stb.toString());
    }

    return residualTicketResp;
  }
}
