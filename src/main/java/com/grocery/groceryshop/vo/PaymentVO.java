package com.grocery.groceryshop.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "支付记录信息")
public class PaymentVO {

    @Schema(description ="支付记录ID")
    private Long id;

    @Schema(description ="支付单号")
    private String paymentNo;

    @Schema(description ="关联订单号")
    private String orderNo;

    @Schema(description ="用户ID")
    private Long userId;

    @Schema(description ="支付金额")
    private BigDecimal amount;

    @Schema(description ="支付方式码: 1-微信 2-支付宝 3-余额")
    private Integer payMethod;

    @Schema(description ="支付方式描述")
    private String payMethodDesc;

    @Schema(description ="支付状态码: 1-处理中 2-支付成功 3-支付失败 4-已退款")
    private Integer status;

    @Schema(description ="支付状态描述")
    private String statusDesc;

    @Schema(description ="网关流水号")
    private String gatewayTradeNo;

    @Schema(description ="创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description ="更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
