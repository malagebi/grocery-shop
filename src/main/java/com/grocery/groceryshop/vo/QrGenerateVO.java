package com.grocery.groceryshop.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "二维码生成结果")
public class QrGenerateVO {

    @Schema(description ="扫码 token（PC 端凭此轮询状态）")
    private String token;

    @Schema(description ="Base64 二维码图片，可直接用于 <img src=''>")
    private String qrCodeImage;
}
