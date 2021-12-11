package com.cfp.app.controller;

import com.cfp.app.model.CloudFlareAccount;
import com.cfp.app.service.CloudFlareAccountService;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import com.cfp.helper.UuidHelper;
import com.cfp.helper.http.CloudFlareApiReq;
import com.mysql.cj.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CloudFlareController {
    
    @Autowired
    private CloudFlareAccountService cloudFlareAccountService;
    @Autowired
    private CloudFlareApiReq cloudFlareApiReq;
    
    
    @RequestMapping("/bindCloudFlareAccount")
    public String bindCloudFlareAccount(){
        return "web/cloudflare/bindAccount";
    }
    
    @ResponseBody
    @RequestMapping("bindCloudFlareAccount/info")
    public LayerPage bindCloudFlareAccountInfo(CloudFlareAccount cloudFlareAccount){
        if(!StringUtils.isNullOrEmpty(cloudFlareAccount.valid())){
            return LayerPage.error(cloudFlareAccount.valid());
        }
        CloudFlareAccount cfa=cloudFlareAccountService.selectOneByUserId(HttpServletHelper.getUser().getId());
        if(null==cfa){
            cfa=cloudFlareAccount;
            cfa.setId(UuidHelper.id());
            cfa.setUserId(HttpServletHelper.getUser().getId());
        }else{
            cfa.setCloudflareEmail(cloudFlareAccount.getCloudflareEmail());
            cfa.setCloudflarePass(cloudFlareAccount.getCloudflarePass());
        }
        cloudFlareAccountService.save(cfa);
        HttpServletHelper.setCfAccount(cfa);
        cloudFlareApiReq.getUserKey();
        return LayerPage.ok();
    }
}
