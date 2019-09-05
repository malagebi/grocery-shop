package com.grocery.groceryshop.service;

import com.grocery.groceryshop.trainticket.resp.CaptchaResp;
import com.grocery.groceryshop.trainticket.resp.ResidualTicketResp;

public interface TrainTicketService {

  CaptchaResp getCaptchaImage64();

  ResidualTicketResp getTicketList();
}
