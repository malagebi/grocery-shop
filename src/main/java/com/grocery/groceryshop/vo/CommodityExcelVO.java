package com.grocery.groceryshop.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CommodityExcelVO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("商品名称")
    private String name;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    private Date createTime;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("更新时间")
    private Date updateTime;
}
