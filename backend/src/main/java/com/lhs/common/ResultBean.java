package com.lhs.common;

import java.util.HashMap;
import java.util.Map;

public class ResultBean {
    private String code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static ResultBean ok() {
        ResultBean result = new ResultBean();
        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public static ResultBean error() {
        ResultBean result = new ResultBean();
        result.setCode("500");
        result.setMessage("失败");
        return result;
    }

    public ResultBean message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultBean data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ResultBean data(Object value) {
        this.data.put("data", value);
        return this;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
}