package com.cfp.helper.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cfp.app.form.RecordForm;
import com.cfp.app.model.CloudFlareAccount;
import com.cfp.app.model.api.*;
import com.cfp.app.service.CloudFlareAccountService;
import com.cfp.common.Contants;
import com.cfp.helper.CloudFlareHelper;
import com.cfp.helper.ErrorHelper;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import com.cfp.helper.exception.BusinessException;
import com.mysql.cj.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CloudFlareApiReq {
    private final static Logger logger = LoggerFactory.getLogger(CloudFlareApiReq.class);
    private final static String contentType="application/x-www-form-urlencoded";

    @Autowired
    private CloudFlareAccountService cloudFlareAccountService;
    
    @Value("${cloudflare.partners.apiKey}")
    private String apiKey;



    public List<RecordForm> changeSSLTypeIsTxt(String domain) {
        List<RecordForm> recordForms=new ArrayList<>();
        String zoneId=getZoneId(domain);
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/certificate_packs?status=all";
        TriHttpRequest req=requestClient(url);
        try{
            req.addGetValue("per_page","100");
            JSONArray sslPacks=JSONObject.parseObject(req.get().getReponse()).getJSONArray("result");
            for (int i = 0; i <sslPacks.size() ; i++) {
                JSONObject sslPack=sslPacks.getJSONObject(i);
                String packId=sslPack.getString("id");
                url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/verification/"+packId;
                req=requestClient(url);
                req.setBody("{\"validation_method\":\"txt\"}");
                JSONObject result=JSONObject.parseObject(req.patch()).getJSONObject("result");
                String name=result.getJSONObject("verification_info").getString("txt_name");
                String type=result.getString("validation_method");
                String value=result.getJSONObject("verification_info").getString("txt_value");
                name=name.replace("."+domain,"").replace(domain,"");
                if(StringUtils.isNullOrEmpty(name)){
                    name="@";
                }
                RecordForm recordForm=new RecordForm(name,value);
                recordForm.setType(type);
                recordForms.add(recordForm);
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return recordForms;
    }
    /** cfp请求公共方法 **/
    private TriHttpRequest requestHost(String act){
        CloudFlareAccount cfa = getUserKey();
        String url="https://api.cloudflare.com/host-gw.html";
        TriHttpRequest req=null;
        try{
            req=new TriHttpRequest(url);
            req.setTimeout(10000);
            req.setContentType(contentType);
            req.addPostValue("act",act);
            req.addPostValue("host_key",apiKey);
            req.addPostValue("user_key",cfa.getUserKey());
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
        }
        return req;
    }

    /** 客户端请求公共方法 **/
    private TriHttpRequest requestClient(String url){
        TriHttpRequest req=null;
        try{
            CloudFlareAccount cfa = getUserKey();
            req=new TriHttpRequest(url);
            req.setTimeout(10000);
            req.setContentType(contentType);
            req.setHeader("X-Auth-Email",cfa.getCloudflareEmail());
            req.setHeader("X-Auth-Key",cfa.getUserApiKey());
            req.setHeader("Content-Type","application/json");
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
        }
        return req;
    }

    /** 获取cloudflare用户信息 **/
    public CloudFlareAccount getUserKey() {
        CloudFlareAccount response = HttpServletHelper.getCfAccount();
        if(response==null){
            throw new BusinessException("没有绑定CloudFlare账号");
        }
        if(!StringUtils.isNullOrEmpty(response.getUserKey())&&!StringUtils.isNullOrEmpty(response.getUserApiKey())){
            return response;
        }
        try{
            String url="https://api.cloudflare.com/host-gw.html";
            TriHttpRequest req=new TriHttpRequest(url);
            req.setTimeout(10000);
            req.setContentType(contentType);
            req.addPostValue("act",Contants.CloudFlareAct.GETUSERKEY);
            req.addPostValue("host_key",apiKey);
            req.addPostValue("cloudflare_email",response.getCloudflareEmail());
            req.addPostValue("cloudflare_pass",response.getCloudflarePass());
            CloudFlareRespone<UserAuthRes> res = CloudFlareHelper.response(req.post().getReponse());
            if(Contants.SUCCESS.equals(res.getResult())){
                UserAuthRes uar=res.getResponseObject(UserAuthRes.class);
                response.setUserKey(uar.getUser_key());
                response.setUserApiKey(uar.getUser_api_key());
                HttpServletHelper.setCfAccount(response);
            }else{
                cloudFlareAccountService.delete(response);
                HttpServletHelper.setCfAccount(null);
                throw new BusinessException(res.getMsg());
            }
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
        }
        return response;
    }
    
    public void addDoMain(String domain){
        try{
            TriHttpRequest req= requestHost(Contants.CloudFlareAct.ZONESET);
            req.addPostValue("zone_name",domain);
            //req.addPostValue("resolve_to","example.com");
            //req.addPostValue("subdomains","www");
            CloudFlareRespone response=CloudFlareHelper.response(req.post().getReponse());
            if(Contants.ERROR.equals(response.getResult())){
                throw new BusinessException(response.getMsg());
            }else{
                throw new BusinessException("20211121", JSON.parseObject(response.getResponse()).getString("msg"));
            }
            //deleteRecords(domain);
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("API连接失败!");
        }
    }



    public void deleteDomain(String zoneId) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId;
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> response=CloudFlareHelper.clientResponse(req.delete().getReponse());
            if(!response.isSuccess()){
                logger.error(JSONObject.toJSONString(response));
                throw new BusinessException("删除域名失败");
            }
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            throw new BusinessException("删除域名失败");
        }
    }

    public void deleteRecords(String domain) {
        String zoneId=getZoneId(domain);
        LayerPage domains=getAllRecords(100,1,zoneId);
        List<DnsRecords> records=(List<DnsRecords>)domains.getData();
        for (int i = 0; i <records.size() ; i++) {
            deleteRecord(zoneId,records.get(i).getId());
        }
    }
    
    public void deleteRecord(String zoneId,String recordId){
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/dns_records/"+recordId;
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> response=CloudFlareHelper.clientResponse(req.delete().getReponse());
            if(!response.isSuccess()){
                logger.error(JSONObject.toJSONString(response));
                throw new BusinessException("删除解析记录失败");
            }
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            throw new BusinessException("删除解析记录失败");
        }
    }
    
    public String getZoneId(String domain){
        String url="https://api.cloudflare.com/client/v4/zones?name="+domain;
        TriHttpRequest req=requestClient(url);
        try{
            return JSONObject.parseObject(req.get().getReponse()).getJSONArray("result").getJSONObject(0).getString("id");
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
        }
        throw new BusinessException("获取域名信息失败");
    }
    
    public LayerPage getAllRecords(Integer pageSize, Integer pageNum,String zoneId){
        List<DnsRecords> data=new ArrayList<>();
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/dns_records";
        TriHttpRequest req=requestClient(url);
        req.addGetValue("per_page",pageSize);
        req.addGetValue("page",pageNum);
        try{
            CFClientRespone<DnsRecords> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            data=res.getResultArray(DnsRecords.class);
            lp.setData(data);
            lp.setiTotalDisplayRecords(res.getResult_info().getTotal_count()+0L);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("查询解析列表失败");
        }
        return lp;
    }
    
    public void addRecord(RecordForm recordForm,String zoneId) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/dns_records";
        TriHttpRequest req=requestClient(url);
        try{
            req.addGetValue("per_page","100");
            req.setBody(JSONObject.toJSONString(recordForm));
            CFClientRespone<String> response=CloudFlareHelper.clientResponse(req.post().getReponse());
            if(!response.isSuccess()){
                logger.error(JSONObject.toJSONString(response));
                throw new BusinessException(JSONArray.parseArray(response.getErrors()).getJSONObject(0)
                        .getJSONArray("error_chain").getJSONObject(0).getString("message")+"添加记录失败");
            }
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("添加记录失败");
        }
    }


    public void updateRecords(DnsRecords dnsRecords) {
        String url="https://api.cloudflare.com/client/v4/zones/"+dnsRecords.getZone_id()+"/dns_records/"+dnsRecords.getId();
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody(JSONObject.toJSONString(dnsRecords));
            CFClientRespone<String> response=CloudFlareHelper.clientResponse(req.put().getReponse()); 
            if(!response.isSuccess()){
                throw new BusinessException("修改记录失败");
            }
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改记录失败");
        }
    }
    
    public LayerPage getDomainList(Integer pageSize, Integer pageNum, Map<String,String> params){
        List<DomainList> data=new ArrayList<>();
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones";
        TriHttpRequest req=requestClient(url);
        req.addGetValue("per_page",pageSize);
        req.addGetValue("page",pageNum);
        if(params.containsKey("search")&&!StringUtils.isNullOrEmpty(params.get("search"))){
            req.addGetValue("name",params.get("search"));
        }
        try{
            CFClientRespone<DomainList> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            data=res.getResultArray(DomainList.class);
            if(data==null){
                data=new ArrayList<>();
            }
            lp.setData(data);
            lp.setiTotalDisplayRecords(res.getResult_info().getTotal_count()+0L);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("查询域名列表失败");
        }
        return lp;
    }

    public LayerPage getAllSslPackages(String zoneId) {
        LayerPage lp=LayerPage.ok();
        List<SslPackages> data=new ArrayList<>();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/certificate_packs?status=all";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<SslPackages> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            data=res.getResultArray(SslPackages.class);
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取证书列表出错");
        }
        return lp;
    }

    public void changeValidationMethod(String zoneId,String packId,String type) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/verification/"+packId;
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"validation_method\":\""+type+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getErrors());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改校验方法出错");
        }
    }

    public LayerPage getGlobalSslState(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/universal/settings";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取全局状态失败");
        }
        return lp;
    }

    public void updateGlobalSslState(String zoneId, boolean state) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/ssl/universal/settings";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"enabled\":"+state+"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改全局状态失败");
        }
    }

    public LayerPage getAlwaysHttpsState(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/always_use_https";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取全局状态失败");
        }
        return lp;
    }

    public void updateAlwaysHttpsState(String zoneId, boolean state) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/always_use_https";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":\""+(state?"on":"off")+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改强制HTTPS失败");
        }
    }


    public LayerPage getHttpToHttpsState(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/automatic_https_rewrites";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取Https301状态失败");
        }
        return lp;
    }

    public void updateHttpToHttpsState(String zoneId, boolean state) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/automatic_https_rewrites";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":\""+(state?"on":"off")+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改Https301失败");
        }
    }


    public LayerPage getSslSetting(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/ssl";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取SSL设置失败");
        }
        return lp;
    }

    public void updateSslSetting(String zoneId, String type) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/ssl";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":\""+type+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改SSL设置失败");
        }
    }


    public LayerPage getCacheLevel(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/cache_level";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取缓存级别失败");
        }
        return lp;
    }

    public void updateCacheLevel(String zoneId, String value) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/cache_level";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":\""+value+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改缓存级别失败");
        }
    }


    public LayerPage getCacheTime(String zoneId) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/browser_cache_ttl";
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取缓存时间失败");
        }
        return lp;
    }

    public void updateCacheTime(String zoneId, String value) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/browser_cache_ttl";
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":"+value+"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改缓存时间失败");
        }
    }



    public LayerPage getPubSetting(String zoneId,String settingType) {
        LayerPage lp=LayerPage.ok();
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/"+settingType;
        TriHttpRequest req=requestClient(url);
        try{
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.get().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
            JSONObject data=res.getResultJSONObject();
            lp.setData(data);
        }catch (SocketTimeoutException e){
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("获取"+settingType+"失败");
        }
        return lp;
    }

    public void updatePubSetting(String zoneId, String value,String settingType) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/settings/"+settingType;
        TriHttpRequest req=requestClient(url);
        try{
            req.setBody("{\"value\":\""+value+"\"}");
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.patch());
            if(!res.isSuccess()){
                throw new BusinessException(res.getMessages());
            }
        }catch (URISyntaxException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("修改"+settingType+"失败");
        }
    }

    public void flushCache(String zoneId, boolean isUrls, String urls) {
        String url="https://api.cloudflare.com/client/v4/zones/"+zoneId+"/purge_cache";
        TriHttpRequest req=requestClient(url);
        try{
            if(isUrls){
                String[] files=urls.split("\n");
                String fileUrls="";
                for (int i = 0; i <files.length ; i++) {
                    fileUrls+="\""+files[i]+"\",";
                }
                if(fileUrls.length()>2){
                    fileUrls=fileUrls.substring(0,fileUrls.length()-1);
                }
                req.setBody("{\"files\":["+fileUrls+"]}");
            }else{
                req.setBody("{\"purge_everything\":true}");
            }
            CFClientRespone<String> res = CloudFlareHelper.clientResponse(req.post().getReponse());
            if(!res.isSuccess()){
                throw new BusinessException(JSONArray.parseArray(res.getErrors()).getJSONObject(0).getString("message"));
            }
        }catch (SocketTimeoutException e){
            logger.error(ErrorHelper.getErrorMessage(e));
            throw new BusinessException("连接CloudFlare超时");
        }catch (IOException e){
            throw new BusinessException("清除缓存失败");
        }
    }
}
