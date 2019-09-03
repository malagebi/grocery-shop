package com.grocery.groceryshop.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserAuth {
  private Long id;

  private Long userId;

  private String loginType;

  private String openId;

  private String accessToken;

  private Date createTime;

  private Date updateTime;
}
