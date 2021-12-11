package com.cfp.app.model.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class CloudFlareRespone<T> {

    private JSONObject request;
    private String response;
    private String result;//success error
    private String msg;

    public JSONObject getRequest() {
        return request;
    }

    public T getResponseObject(Class<T> t) {
        return JSON.parseObject(this.response, t);
    }

    public List<T> getResponseArray(Class<T> t) {
        return JSON.parseArray(this.response, t);
    }
    
    public String getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponse() {
        return response;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
