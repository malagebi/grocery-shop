package com.grocery.groceryshop.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {

    private Long id;

    private String orderNo;

    private Long userId;

    private Long commodityId;

    private String commodityName;

    private Integer quantity;

    private BigDecimal totalAmount;

    /** 订单状态: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消 */
    private Integer status;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
