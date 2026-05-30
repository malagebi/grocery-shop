package com.grocery.groceryshop.base.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付记录查询请求")
public class PaymentListReq extends PageReq {

    @Schema(description ="用户ID")
    private Long userId;

    @Schema(description ="支付状态: 1-处理中 2-支付成功 3-支付失败 4-已退款")
    private Integer status;
}
