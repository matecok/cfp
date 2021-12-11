package com.cfp.helper.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;


public class ApiHelper {

	public static class StremCache {
		public InputStream content;
		public Header[] header;
	}

	/***
	 * api请求方法，根据返回类型定义，会有String或者数据流
	 * */
	public static Object processorStream(String url, String param, String contentType) {
		Object response = null;
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Connection", "Keep-Alive");
            StringEntity stringentity = new StringEntity(param, ContentType.create(contentType, "UTF-8"));//"application/json"  "text/json"
            httppost.setEntity(stringentity);
            httpresponse = httpclient.execute(httppost);//关键一步
            HttpEntity entity = httpresponse.getEntity();
            String type="";
            if(entity.getContentType()!=null) {
                type=entity.getContentType().getValue();
            }
            if(type.toLowerCase().contains("application/octet-stream")) {
                response = httpresponse;
            }else {
                response = EntityUtils.toString(entity,"utf-8");//加上utf-8参数防止中文乱码
            }
        } catch (Exception e) {
            JSONObject sysStatus=new JSONObject();
            System.out.println("com.yaic.app.datasvr.helper.ApiHelper=="+e.getMessage());
            JSONObject error=new JSONObject();
            error.put("message",e.getMessage());
            error.put("path","com.yaic.app.datasvr.helper.ApiHelper");
            error.put("status","500");
            sysStatus.put("meta",error);
            return sysStatus;
        }
        return response;
	}
}
