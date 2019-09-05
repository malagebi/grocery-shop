package com.grocery.groceryshop.trainticket.req;

import lombok.Data;

@Data
public class ResidualTicketReq {
  private String leftTicketDTO;
  private String train_date;
  // 始发站
  private String from_station;
  private String to_station;
  // 购票类型=成人
  private String purpose_codes;
}
