package com.cfp.app.controller;

import com.cfp.app.model.CloudFlareAccount;
import com.cfp.app.model.User;
import com.cfp.app.service.CloudFlareAccountService;
import com.cfp.app.service.LoginHistoryService;
import com.cfp.app.service.UserService;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import com.cfp.helper.PasswdHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @date 2019/10/15
 * describe:
 */
@Controller
public class LoginController {
    
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CloudFlareAccountService cloudFlareAccountService;
    @Autowired
    private LoginHistoryService loginHistoryService;

    @RequestMapping("/login")
    public String login(ModelMap modelMap,String redirect) throws UnsupportedEncodingException {
        User u= (User) HttpServletHelper.getSession().getAttribute("user");
        if(u!=null){
            return "redirect:/";
        }
        if(redirect==null){
            redirect="";
        }
        modelMap.addAttribute("redirect", URLDecoder.decode(redirect, "UTF-8" ));
        return "web/user/login";
    }

    @ResponseBody
    @RequestMapping("/signout")
    public LayerPage signout(){
        HttpServletHelper.getSession().invalidate();
        return LayerPage.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/login/info",method = RequestMethod.POST)
    public LayerPage loginInfo(String username, String password){
        User u= (User) HttpServletHelper.getSession().getAttribute("user");
        if(u!=null){
            return LayerPage.error("已经登陆，请勿重复操作！");
        }
        password= PasswdHelper.md5Password(password);
        User user=userService.findUserByEmailOrPhoneAndPasswdAndIsNotDelete(username,password);
        if(user==null){
            return LayerPage.error("用户名或密码错误！");
        }else{
            HttpServletHelper.getSession().setAttribute("user",user);
            loginHistoryService.addLoginHistory();
            CloudFlareAccount cfa=cloudFlareAccountService.selectOneByUserId(HttpServletHelper.getUser().getId());
            HttpServletHelper.setCfAccount(cfa);
            return LayerPage.ok();
        }
    }
}
