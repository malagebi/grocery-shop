package com.grocery.groceryshop.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Users {
  private Long id;

  private String name;

  private String phone;

  private String password;

  private String avatarUrl;

  private Date createTime;

  private Date updateTime;
}
