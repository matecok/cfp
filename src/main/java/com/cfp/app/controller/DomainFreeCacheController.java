package com.cfp.app.controller;

import com.cfp.helper.LayerPage;
import com.cfp.helper.http.CloudFlareApiReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 域名缓存配置 
 * @className: DomainFreeCacheController
 * @createDate: 2021-08-03 13:57:51
 */
@Controller
@RequestMapping("domain/free")
public class DomainFreeCacheController {

    @Autowired
    private CloudFlareApiReq cloudFlareApiReq;

    @RequestMapping("cache")
    public String cache(ModelMap modelMap, String domain, String domainId, String nsType) {
        modelMap.addAttribute("domain", domain);
        modelMap.addAttribute("domainId", domainId);
        modelMap.addAttribute("nsType", nsType);
        return "web/free/cache";
    }

    @ResponseBody
    @RequestMapping("getCacheLevel")
    public LayerPage getCacheLevel(String domainId) {
        LayerPage data = cloudFlareApiReq.getCacheLevel(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateCacheLevel")
    public LayerPage updateCacheLevel(String domainId, String value) {
        cloudFlareApiReq.updateCacheLevel(domainId, value);
        return LayerPage.ok();
    }


    @ResponseBody
    @RequestMapping("getCacheTime")
    public LayerPage getCacheTime(String domainId) {
        LayerPage data = cloudFlareApiReq.getCacheTime(domainId);
        return data;
    }

    @ResponseBody
    @RequestMapping("updateCacheTime")
    public LayerPage updateCacheTime(String domainId, String value) {
        cloudFlareApiReq.updateCacheTime(domainId, value);
        return LayerPage.ok();
    }


    @ResponseBody
    @RequestMapping("getPubSetting")
    public LayerPage getPubSetting(String domainId, String settingType) {
        LayerPage data = cloudFlareApiReq.getPubSetting(domainId, settingType);
        return data;
    }

    @ResponseBody
    @RequestMapping("updatePubSetting")
    public LayerPage updatePubSetting(String domainId, String value, String settingType) {
        cloudFlareApiReq.updatePubSetting(domainId, value, settingType);
        return LayerPage.ok();
    }

    @ResponseBody
    @RequestMapping("flushCache")
    public LayerPage flushCache(String domainId, boolean isUrls, String urls) {
        cloudFlareApiReq.flushCache(domainId, isUrls, urls);
        return LayerPage.ok();
    }
}
