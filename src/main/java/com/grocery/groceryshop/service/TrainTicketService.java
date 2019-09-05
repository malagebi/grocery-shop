package com.grocery.groceryshop.service;

import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.TicketListResp;

import java.util.List;

public interface TrainTicketService {

  CaptchaResp getCaptchaImage64();

  List<TicketListResp> getTicketList();
}
