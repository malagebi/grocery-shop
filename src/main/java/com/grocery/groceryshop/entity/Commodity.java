package com.grocery.groceryshop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class Commodity {
    private Long id;

    private String name;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}