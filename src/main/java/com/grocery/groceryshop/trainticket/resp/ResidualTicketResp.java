package com.grocery.groceryshop.trainticket.resp;

import lombok.Data;

@Data
public class ResidualTicketResp {

  private ResidualTicketDataResp data;
  private Integer httpstatus;
  private String messages;
  private Boolean status;
}
