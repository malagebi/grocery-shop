package com.grocery.groceryshop.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("支付记录信息")
public class PaymentVO {

    @ApiModelProperty("支付记录ID")
    private Long id;

    @ApiModelProperty("支付单号")
    private String paymentNo;

    @ApiModelProperty("关联订单号")
    private String orderNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付方式码: 1-微信 2-支付宝 3-余额")
    private Integer payMethod;

    @ApiModelProperty("支付方式描述")
    private String payMethodDesc;

    @ApiModelProperty("支付状态码: 1-处理中 2-支付成功 3-支付失败 4-已退款")
    private Integer status;

    @ApiModelProperty("支付状态描述")
    private String statusDesc;

    @ApiModelProperty("网关流水号")
    private String gatewayTradeNo;

    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
