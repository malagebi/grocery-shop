package com.grocery.groceryshop.controller;

import com.github.pagehelper.Page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.grocery.groceryshop.base.PageBaseInfo;
import com.grocery.groceryshop.base.ResultBaseBean;
import com.grocery.groceryshop.entity.Commodity;
import com.grocery.groceryshop.mapper.CommodityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController(value = "/")
public class LoginController {

    @Resource
    private CommodityMapper  commodityMapper;
    @GetMapping(value = "user")
    public PageBaseInfo<Commodity>  userList(){
        Commodity data=new Commodity();
        data.setName("张三");
        data.setCreateTime(new Date());
        data.setUpdateTime(new Date());
        commodityMapper.insert(data);
        PageHelper.startPage(1, 10);
        List<Commodity> list=  commodityMapper.selectAll();
        return PageBaseInfo.build(list);
    }
    @GetMapping(value = "userOne")
    public ResultBaseBean<Object> userOne(){

        List list=new ArrayList<Integer>();
        return ResultBaseBean.success(list);
    }
}
