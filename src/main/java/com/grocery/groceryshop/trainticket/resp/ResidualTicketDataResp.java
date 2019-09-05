package com.grocery.groceryshop.trainticket.resp;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResidualTicketDataResp {
  private String flag;
  private Map<String, String> map;
  private List<String> result;
}
