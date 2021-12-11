package com.cfp.app.controller;

import com.cfp.helper.http.CloudFlareApiReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 域名缓存配置 
 * @className: DomainFreeCacheController
 * @createDate: 2021-08-03 13:57:51
 */
@Controller
@RequestMapping("domain/free")
public class DomainFreeMoreController {

    @Autowired
    private CloudFlareApiReq cloudFlareApiReq;

    @RequestMapping("more")
    public String more(ModelMap modelMap, String domain, String domainId, String nsType) {
        modelMap.addAttribute("domain", domain);
        modelMap.addAttribute("domainId", domainId);
        modelMap.addAttribute("nsType", nsType);
        return "web/free/more";
    }
}
