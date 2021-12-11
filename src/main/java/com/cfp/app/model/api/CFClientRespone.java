package com.cfp.app.model.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class CFClientRespone<T> {
    private boolean success;
    private String result;
    private String messages;
    private String errors;
    private resultInfo result_info;
    
    public static class resultInfo{
        private Integer page;
        private Integer per_page;
        private Integer total_pages;
        private Integer count;
        private Integer total_count;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getPer_page() {
            return per_page;
        }

        public void setPer_page(Integer per_page) {
            this.per_page = per_page;
        }

        public Integer getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(Integer total_pages) {
            this.total_pages = total_pages;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getTotal_count() {
            return total_count;
        }

        public void setTotal_count(Integer total_count) {
            this.total_count = total_count;
        }
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getResultArray(Class<T> clazz) {
        return JSONArray.parseArray(this.result,clazz);
    }

    public T getResultObject(Class<T> clazz) {
        return JSONObject.parseObject(this.result,clazz);
    }

    public String getResultString() {
        return this.result;
    }
    public JSONObject getResultJSONObject() {
        return JSONObject.parseObject(this.result);
    }
    
    public void setResult(String result) {
        this.result = result;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public resultInfo getResult_info() {
        return result_info;
    }

    public void setResult_info(resultInfo result_info) {
        this.result_info = result_info;
    }
}
