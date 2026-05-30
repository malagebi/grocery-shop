package com.grocery.groceryshop.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(description = "订单信息")
public class OrderVO {

    @Schema(description ="订单ID")
    private Long id;

    @Schema(description ="订单号")
    private String orderNo;

    @Schema(description ="用户ID")
    private Long userId;

    @Schema(description ="商品ID")
    private Long commodityId;

    @Schema(description ="商品名称")
    private String commodityName;

    @Schema(description ="购买数量")
    private Integer quantity;

    @Schema(description ="订单总金额")
    private BigDecimal totalAmount;

    @Schema(description ="订单状态码: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;

    @Schema(description ="订单状态描述")
    private String statusDesc;

    @Schema(description ="创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description ="更新时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
