package com.cfp.app.model.api;

public class UserAuthRes {
    private String cloudflare_email;
    private String user_key;
    private String user_api_key;
    private String unique_id;

    public String getCloudflare_email() {
        return cloudflare_email;
    }

    public void setCloudflare_email(String cloudflare_email) {
        this.cloudflare_email = cloudflare_email;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_api_key() {
        return user_api_key;
    }

    public void setUser_api_key(String user_api_key) {
        this.user_api_key = user_api_key;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
}
