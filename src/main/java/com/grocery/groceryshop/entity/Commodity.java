package com.grocery.groceryshop.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Commodity {
    private Long id;

    private String name;

    private Date createTime;

    private Date updateTime;


}