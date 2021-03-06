package com.grocery.groceryshop.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.github.pagehelper.PageHelper;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.base.req.LoginReq;
import com.grocery.groceryshop.entity.Commodity;
import com.grocery.groceryshop.mapper.CommodityMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.List;

@RestController(value = "/")
@Api(tags = "用户模块")
public class LoginController {

  @Resource private CommodityMapper commodityMapper;

  @GetMapping(value = "user")
  @ApiOperation(value = "用户列表")
  public CommonResult userList(HttpServletRequest request, HttpServletResponse response) {
    PageHelper.startPage(1, 5);
    List<Commodity> list = commodityMapper.selectAll();
    CommonPageInfo baseInfo = CommonPageInfo.build(list);
    return CommonResult.success(baseInfo);
  }

  @GetMapping(value = "userOne")
  public CommonResult<Commodity> userOne() {
    Commodity info = commodityMapper.selectByPrimaryKey(1L);

    throw new CustomerException("200", "测试");
  }

  @PostMapping(value = "login")
  public CommonResult login(@Validated LoginReq req) {
    return CommonResult.success();
  }

  @GetMapping(value = "qrCode")
  public String qrCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
    QrConfig config = new QrConfig();
    config.setErrorCorrection(ErrorCorrectionLevel.H);
    BufferedImage bufferedImage = QrCodeUtil.generate("https://hutool.cn/", config);
    return "data:image/png;base64," + ImgUtil.toBase64(bufferedImage, ImgUtil.IMAGE_TYPE_PNG);
  }

  @GetMapping("/getImage")
  public void getImage(HttpServletResponse response) throws Exception {
    ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(400, 100, 4, 4);
    System.out.println(captcha.getCode());
    captcha.write(response.getOutputStream());
    response.getOutputStream().close();
  }
}
