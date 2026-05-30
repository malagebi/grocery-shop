package com.grocery.groceryshop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "扫码状态轮询结果")
public class QrPollVO {

    @Schema(description ="状态: WAITING / SCANNED / CONFIRMED / EXPIRED")
    private String status;

    @Schema(description ="登录凭证（仅 CONFIRMED 时返回）")
    private String authToken;

    @Schema(description ="已登录用户 ID（仅 CONFIRMED 时返回）")
    private Long userId;
}
