package com.cfp.app.controller;

import com.cfp.app.service.UserService;
import com.cfp.helper.LayerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @date 2019/10/15
 * describe:
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(){
        return "web/user/regist";
    }

    /**
     * @description: TODO : 注册信息传入
     * @createDate: 2020-04-17 10:54:47
     * @param secret
     * @param nickName
     * @param password
     * @param country
     * @param code
     * @return java.lang.Object
     */
    @ResponseBody
    @RequestMapping(value = "/registerData",method = RequestMethod.POST)
    public LayerPage registerData(String secret, String nickName, String password, String country, String code){
        return userService.registerUser(secret,nickName,password,country,code);
    }


    @RequestMapping("/forgotPasswd")
    public String forgotPasswd(){
        return "web/user/forgotpasswd";
    }
    
    @ResponseBody
    @RequestMapping("/resetPassword")
    public LayerPage resetPassword(String uid,String passwd){
        return LayerPage.ok();
    }
}
