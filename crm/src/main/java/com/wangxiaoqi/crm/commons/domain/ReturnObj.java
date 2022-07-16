package com.wangxiaoqi.crm.commons.domain;


public class ReturnObj {
    private String code; //标记处理是否成功：1代表成功，0代表失败
    private String message; //返回处理失败的提示信息
    private Object retDate; //其他信息

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetDate() {
        return retDate;
    }

    public void setRetDate(Object retDate) {
        this.retDate = retDate;
    }

    public ReturnObj() {
    }

    public ReturnObj(String code, String message, Object retDate) {
        this.code = code;
        this.message = message;
        this.retDate = retDate;
    }
}
