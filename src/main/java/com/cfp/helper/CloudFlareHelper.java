package com.cfp.helper;

import com.alibaba.fastjson.JSONObject;
import com.cfp.app.model.api.CFClientRespone;
import com.cfp.app.model.api.CloudFlareRespone;

public class CloudFlareHelper {
    
    public static CloudFlareRespone response(String res){
        return JSONObject.parseObject(res,CloudFlareRespone.class);
    }

    public static CFClientRespone clientResponse(String res){
        return JSONObject.parseObject(res, CFClientRespone.class);
    }
}
