package com.grocery.groceryshop.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("订单分页查询请求")
public class OrderListReq extends PageReq {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单状态: 1-待支付 2-已支付 3-已发货 4-已完成 5-已取消")
    private Integer status;
}
