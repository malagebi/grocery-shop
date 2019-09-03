package com.grocery.groceryshop.base.req;

import javax.validation.constraints.NotNull;

public class LoginReq {

  @NotNull(message = "不允许为空")
  private String name;

  @NotNull(message = "密码不允许为空")
  private String password;
}
