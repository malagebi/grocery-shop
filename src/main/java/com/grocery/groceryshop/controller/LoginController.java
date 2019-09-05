package com.grocery.groceryshop.controller;

import com.github.pagehelper.PageHelper;
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
    // return CommonResult.success(info);
    throw new CustomerException("200", "测试");
  }

  @PostMapping(value = "login")
  public CommonResult login(@Validated LoginReq req) {
    return CommonResult.success();
  }

  public static void main(String[] args) {}
}
