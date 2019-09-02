package com.grocery.groceryshop.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class PageBaseInfo<T> {
    @ApiModelProperty(value = "当前页号")
    private int pageNum;

    @ApiModelProperty(value = "每页的数量")
    private int pageSize;

    @ApiModelProperty(value = "总记录数")
    private long total;

    @ApiModelProperty(value = "总页数")
    private int pages;

    @ApiModelProperty(value = "结果集")
    private List<T> list;



    public static <T> PageBaseInfo<T> build(List<T> list) {
        PageInfo page = new PageInfo(list);
        PageBaseInfo result = new PageBaseInfo<>();
        BeanUtils.copyProperties(page, result);
        return result;
    }



}
