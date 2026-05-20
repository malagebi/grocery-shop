package com.grocery.groceryshop.base.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("支付记录查询请求")
public class PaymentListReq extends PageReq {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("支付状态: 1-处理中 2-支付成功 3-支付失败 4-已退款")
    private Integer status;
}
