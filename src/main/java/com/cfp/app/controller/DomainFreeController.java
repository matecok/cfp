package com.cfp.app.controller;

import com.cfp.app.form.RecordForm;
import com.cfp.app.model.api.DnsRecords;
import com.cfp.common.ValidAnnotation;
import com.cfp.helper.LayerPage;
import com.cfp.helper.http.CloudFlareApiReq;
import com.mysql.cj.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @description: 域名服务
 * @className: DomainFreeController
 * @createDate: 2021-07-22 09:59:21
 */
@Controller
@RequestMapping("domain/free")
public class DomainFreeController {

    @Autowired
    private CloudFlareApiReq cloudFlareApiReq;
    
    @RequestMapping("addDomain")
    public String addDomain(){

        return "web/free/add/domain";
    }
    @ResponseBody
    @RequestMapping(value = "addDomain/{domain}",method = RequestMethod.POST)
    public LayerPage insertDomain(String domain){
        if(!Pattern.matches(ValidAnnotation.Type.DOMAIN.getPattern(), StringUtils.isNullOrEmpty(domain)?"":domain)){
            return LayerPage.error("域名格式错误");
        }
        cloudFlareApiReq.addDoMain(domain);
        return LayerPage.ok();
    }
    
    @RequestMapping("domainList")
    public String domainList(){
        return "web/free/domainList";
    }
    
    @ResponseBody
    @RequestMapping(value = "getDomainList",method = RequestMethod.POST)
    public LayerPage getDomainList(Integer pageSize,Integer pageNum,String search){
        Map<String,String> params=new HashMap<>();
        params.put("search",search);
        LayerPage data=cloudFlareApiReq.getDomainList(pageSize,pageNum,params);
        return data;
    }

    @ResponseBody
    @RequestMapping("deleteDomain")
    public LayerPage deleteDomain(String domainId){
        cloudFlareApiReq.deleteDomain(domainId);
        return LayerPage.ok();
    }
    
    @RequestMapping("dns")
    public String dnsPage(ModelMap modelMap,String domainId, String domain,String nsType){
        modelMap.addAttribute("domain",domain);
        modelMap.addAttribute("domainId",domainId);
        modelMap.addAttribute("nsType",nsType);
        return "web/free/dns";
    }
    
    @ResponseBody
    @RequestMapping("getDnsRecords")
    public LayerPage getDnsRecords(Integer pageSize,Integer pageNum,String domainId){
        LayerPage data=cloudFlareApiReq.getAllRecords(pageSize,pageNum,domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateDnsRecord")
    public LayerPage updateDnsRecord(DnsRecords dnsRecords){
        cloudFlareApiReq.updateRecords(dnsRecords);
        return LayerPage.ok();
    }
    @ResponseBody
    @RequestMapping("deleteDnsRecord")
    public LayerPage deleteDnsRecord(DnsRecords dnsRecords){
        cloudFlareApiReq.deleteRecord(dnsRecords.getZone_id(),dnsRecords.getId());
        return LayerPage.ok();
    }
    @ResponseBody
    @RequestMapping("addDnsRecord")
    public LayerPage addDnsRecord(RecordForm recordForm,String zone_id){
        cloudFlareApiReq.addRecord(recordForm,zone_id);
        return LayerPage.ok();
    }


    @RequestMapping("ssl")
    public String sslPage(ModelMap modelMap,String domainId, String domain,String nsType){
        modelMap.addAttribute("domain",domain);
        modelMap.addAttribute("domainId",domainId);
        modelMap.addAttribute("nsType",nsType);
        return "web/free/ssl";
    }
    @ResponseBody
    @RequestMapping("sslSetting")
    public LayerPage sslSetting(String domainId){
        LayerPage data=cloudFlareApiReq.getAllSslPackages(domainId);
        return data;
    }
    @ResponseBody
    @RequestMapping("changeValidationMethod")
    public LayerPage changeValidationMethod(String domainId,String packId,String type){
        cloudFlareApiReq.changeValidationMethod(domainId,packId,type);
        return LayerPage.ok();
    }
    @ResponseBody
    @RequestMapping("getGlobalSslState")
    public LayerPage getGlobalSslState(String domainId){
        LayerPage data=cloudFlareApiReq.getGlobalSslState(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateGlobalSslState")
    public LayerPage updateGlobalSslState(String domainId,boolean state){
        cloudFlareApiReq.updateGlobalSslState(domainId,state);
        return LayerPage.ok();
    }

    @ResponseBody
    @RequestMapping("getAlwaysHttpsState")
    public LayerPage getAlwaysHttpsState(String domainId){
        LayerPage data=cloudFlareApiReq.getAlwaysHttpsState(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateAlwaysHttpsState")
    public LayerPage updateAlwaysHttpsState(String domainId,boolean state){
        cloudFlareApiReq.updateAlwaysHttpsState(domainId,state);
        return LayerPage.ok();
    }

    @ResponseBody
    @RequestMapping("getHttpToHttpsState")
    public LayerPage getHttpToHttpsState(String domainId){
        LayerPage data=cloudFlareApiReq.getHttpToHttpsState(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateHttpToHttpsState")
    public LayerPage updateHttpToHttpsState(String domainId,boolean state){
        cloudFlareApiReq.updateHttpToHttpsState(domainId,state);
        return LayerPage.ok();
    }
    
    @ResponseBody
    @RequestMapping("getSslSetting")
    public LayerPage getSslSetting(String domainId){
        LayerPage data=cloudFlareApiReq.getSslSetting(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateSslSetting")
    public LayerPage updateSslSetting(String domainId,String type){
        cloudFlareApiReq.updateSslSetting(domainId,type);
        return LayerPage.ok();
    }
}
