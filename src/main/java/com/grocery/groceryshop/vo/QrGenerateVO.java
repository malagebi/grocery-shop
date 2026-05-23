package com.grocery.groceryshop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二维码生成结果")
public class QrGenerateVO {

    @ApiModelProperty("扫码 token（PC 端凭此轮询状态）")
    private String token;

    @ApiModelProperty("Base64 二维码图片，可直接用于 <img src=''>")
    private String qrCodeImage;
}
