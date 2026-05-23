package com.grocery.groceryshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("扫码状态轮询结果")
public class QrPollVO {

    @ApiModelProperty("状态: WAITING / SCANNED / CONFIRMED / EXPIRED")
    private String status;

    @ApiModelProperty("登录凭证（仅 CONFIRMED 时返回）")
    private String authToken;

    @ApiModelProperty("已登录用户 ID（仅 CONFIRMED 时返回）")
    private Long userId;
}
