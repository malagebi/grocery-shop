package com.grocery.groceryshop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentRecord {

    private Long id;

    /** 支付单号 */
    private String paymentNo;

    /** 关联订单号 */
    private String orderNo;

    private Long userId;

    private BigDecimal amount;

    /** 支付方式: 1-微信 2-支付宝 3-余额 */
    private Integer payMethod;

    /** 支付状态: 1-处理中 2-支付成功 3-支付失败 4-已退款 */
    private Integer status;

    /** 模拟网关流水号，支付成功后填充 */
    private String gatewayTradeNo;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
