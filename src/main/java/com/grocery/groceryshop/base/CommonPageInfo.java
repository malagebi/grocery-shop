package com.grocery.groceryshop.base;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel(value = "分页信息")
public class CommonPageInfo<T> {
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



    public static <T> CommonPageInfo<T> build(List<T> list) {
        PageInfo page = new PageInfo(list);
        CommonPageInfo result = new CommonPageInfo<>();
        BeanUtils.copyProperties(page, result);
        return result;
    }



}
