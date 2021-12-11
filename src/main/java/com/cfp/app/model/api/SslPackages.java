package com.cfp.app.model.api;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class SslPackages {
    private String id;
    private List<String> hosts;
    private String status;
    private String validation_method;
    private JSONArray validation_records;
    private JSONArray certificates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValidation_method() {
        return validation_method;
    }

    public void setValidation_method(String validation_method) {
        this.validation_method = validation_method;
    }

    public JSONArray getValidation_records() {
        return validation_records;
    }

    public void setValidation_records(JSONArray validation_records) {
        this.validation_records = validation_records;
    }

    public JSONArray getCertificates() {
        return certificates;
    }

    public void setCertificates(JSONArray certificates) {
        this.certificates = certificates;
    }
}
