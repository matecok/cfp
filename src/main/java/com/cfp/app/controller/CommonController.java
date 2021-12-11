package com.cfp.app.controller;

import com.cfp.app.model.LoginHistory;
import com.cfp.app.service.LoginHistoryService;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping(value = "/common",method = RequestMethod.POST)
public class CommonController {

    @Autowired
    private LoginHistoryService loginHistoryService;
    
    @RequestMapping("/theme/change")
    public LayerPage change(String type,String value){
        //data-layout-mode="dark" data-topbar="dark" data-sidebar="dark"
        if("layout".equals(type)||"all".equals(type)){
            HttpServletHelper.getSession().setAttribute("data-layout-mode",value);
        }
        if("topbar".equals(type)||"all".equals(type)){
            HttpServletHelper.getSession().setAttribute("data-sidebar",value);
        }
        if("sidebar".equals(type)||"all".equals(type)){
            HttpServletHelper.getSession().setAttribute("data-topbar",value);
        }
        return LayerPage.ok();
    }

    @RequestMapping("/load/loginhistory")
    public LayerPage loginhistory(){
        LoginHistory lh=loginHistoryService.findOneByUserIdOrderByLoginTimeDesc();
        LayerPage lp=LayerPage.ok();
        lp.setData(lh);
        return lp;
    }
}
