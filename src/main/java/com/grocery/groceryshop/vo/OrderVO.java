package com.grocery.groceryshop.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("订单信息")
public class OrderVO {

    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品ID")
    private Long commodityId;

    @ApiModelProperty("商品名称")
    private String commodityName;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("订单状态码: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;

    @ApiModelProperty("订单状态描述")
    private String statusDesc;

    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
