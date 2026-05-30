package com.grocery.groceryshop.base;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@Schema(description = "分页信息")
public class CommonPageInfo<T> {
    @Schema(description = "当前页号")
    private int pageNum;

    @Schema(description = "每页的数量")
    private int pageSize;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "总页数")
    private int pages;

    @Schema(description = "结果集")
    private List<T> list;



    public static <T> CommonPageInfo<T> build(List<T> list) {
        PageInfo page = new PageInfo(list);
        CommonPageInfo result = new CommonPageInfo<>();
        BeanUtils.copyProperties(page, result);
        return result;
    }



}
