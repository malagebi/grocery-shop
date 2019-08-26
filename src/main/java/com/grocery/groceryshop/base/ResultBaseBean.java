package com.grocery.groceryshop.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResultBaseBean<T> {


    /**
     * 自定义操作信息
     */
    private String message;

    /**
     * 处理结果代码
     */
    private String code;

    private Collection<T> data;


    public static ResultBaseBean success() {
        ResultBaseBean result = new ResultBaseBean();
        return result;
    }

    public static <T> ResultBaseBean<T> success( Collection<T> data) {
        ResultBaseBean result = new ResultBaseBean();
        result.setCode("ok");
        result.setMessage("成功");
        result.setData(data);
        return result;
    }
    public static ResultBaseBean error() {
        ResultBaseBean result = new ResultBaseBean();
        return result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }


}
