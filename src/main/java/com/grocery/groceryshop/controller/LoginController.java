package com.grocery.groceryshop.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.base.req.LoginReq;
import com.grocery.groceryshop.entity.Commodity;
import com.grocery.groceryshop.mapper.CommodityMapper;
import com.grocery.groceryshop.vo.CommodityExcelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "/")
@Api(tags = "用户模块")
public class LoginController {

  @Resource private CommodityMapper commodityMapper;

  @GetMapping(value = "user")
  @ApiOperation(value = "用户列表")
  public CommonResult<CommonPageInfo<Commodity>> userList(
      @RequestParam(defaultValue = "1") int pageNum,
      @RequestParam(defaultValue = "10") int pageSize) {
    try {
      PageHelper.startPage(pageNum, pageSize);
      List<Commodity> list = commodityMapper.selectAll();
      return CommonResult.success(CommonPageInfo.build(list));
    } finally {
      PageHelper.clearPage();
    }
  }

  @GetMapping(value = "userOne")
  public CommonResult<Commodity> userOne() {
    Commodity info = commodityMapper.selectByPrimaryKey(1L);

    throw new CustomerException("200", "测试");
  }

  @PostMapping(value = "login")
  public CommonResult<Void> login(@Validated LoginReq req) {
    return CommonResult.success();
  }

  @GetMapping(value = "qrCode")
  public String qrCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
    QrConfig config = new QrConfig();
    config.setErrorCorrection(ErrorCorrectionLevel.H);
    BufferedImage bufferedImage = QrCodeUtil.generate("https://hutool.cn/", config);
    return "data:image/png;base64," + ImgUtil.toBase64(bufferedImage, ImgUtil.IMAGE_TYPE_PNG);
  }

  @GetMapping("/commodity/export")
  @ApiOperation("导出商品列表")
  public void exportCommodity(HttpServletResponse response) throws Exception {
    List<Commodity> list = commodityMapper.selectAll();
    List<CommodityExcelVO> voList = list.stream().map(c -> {
      CommodityExcelVO vo = new CommodityExcelVO();
      vo.setId(c.getId());
      vo.setName(c.getName());
      vo.setCreateTime(c.getCreateTime());
      vo.setUpdateTime(c.getUpdateTime());
      return vo;
    }).collect(Collectors.toList());

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");
    String fileName = URLEncoder.encode("商品列表", "UTF-8").replaceAll("\\+", "%20");
    response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    EasyExcel.write(response.getOutputStream(), CommodityExcelVO.class)
        .sheet("商品列表")
        .doWrite(voList);
  }

  @GetMapping("/getImage")
  public void getImage(HttpServletResponse response) throws Exception {
    ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(400, 100, 4, 4);
    System.out.println(captcha.getCode());
    captcha.write(response.getOutputStream());
    response.getOutputStream().close();
  }
}
