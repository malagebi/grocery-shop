package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.PageBaseInfo;
import com.grocery.groceryshop.base.ResultBaseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "/")
public class LoginController {

    @GetMapping(value = "user")
    public PageBaseInfo<Object>  userList(){
        List list=new ArrayList<Integer>();
        return new PageBaseInfo(list);
    }
    @GetMapping(value = "userOne")
    public ResultBaseBean<Object> userOne(){
        List list=new ArrayList<Integer>();
        return ResultBaseBean.success(list);
    }
}
