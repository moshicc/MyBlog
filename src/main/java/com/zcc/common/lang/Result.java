package com.zcc.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zcc
 * @date 2020/6/5 14:37
 * @description 异步统一返回的结果封装类
 */
@Data
public class Result implements Serializable {
    private int code;//200正常、非200表示异常
    private String msg;
    private Object data;

    public static Result succ(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 操作成功
     * @param data
     * @return
     */
    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    /**
     * 操作失败
     * @param msg
     * @return
     */
    public static Result fail(String msg){
        return fail(400,msg,null);
    }

    /**
     * 操作失败
     * @param msg
     * @param data
     * @return
     */
    public static Result fail(String msg,Object data){
        return fail(400,msg,data);
    }


}
