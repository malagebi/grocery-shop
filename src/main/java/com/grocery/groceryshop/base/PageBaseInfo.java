package com.grocery.groceryshop.base;

import com.github.pagehelper.Page;
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



    public static <T> PageBaseInfo<T> build(Page<T> page) {
        PageBaseInfo result = new PageBaseInfo<>();
        BeanUtils.copyProperties(page.toPageInfo(), result);
        return result;
    }



}
