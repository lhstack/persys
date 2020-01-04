package com.lhstack.entity.layui;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LayuiResut<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
    public static <T> LayuiResut<T> buildSuccess(T data){
        return new LayuiResut<T>().setData(data).setCode(200);
    }
    public static <T> LayuiResut<T> buildError(String message,Integer code){
        return new LayuiResut<T>().setCode(code).setMsg(message);
    }
    public static <T> LayuiResut<T> buildError(T data,String message,Integer code){
        return new LayuiResut<T>().setCode(code).setMsg(message).setData(data);
    }
}
