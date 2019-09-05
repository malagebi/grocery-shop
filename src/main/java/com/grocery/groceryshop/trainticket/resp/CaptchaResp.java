package com.grocery.groceryshop.trainticket.resp;

import lombok.Data;

@Data
public class CaptchaResp {

  private String image;
  private String result_message;
  private String result_code;
}
