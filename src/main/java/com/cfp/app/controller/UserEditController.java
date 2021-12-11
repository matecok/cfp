package com.cfp.app.controller;

import com.cfp.app.model.User;
import com.cfp.app.service.UserService;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import com.cfp.helper.PasswdHelper;
import com.mysql.cj.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 用户信息编辑 登陆后才可操作的方法
 * @className: UseViewController
 * @createDate: 2020-06-01 11:46:18
 */
@Controller
@RequestMapping("/user/edit")
public class UserEditController {

    @Autowired
    private UserService userService;

    /**
     * @description: 打开修改密码页面
     * @createDate: 2020-06-01 17:04:38
     * @param
     * @return java.lang.String
     */
    @RequestMapping(value = "/changePasswordPage")
    public String changePasswordPage(){
        return "web/user/updatePwd";
    }

    /**
     * @description: 修改密码
     * @createDate: 2020-06-01 17:04:50
     * @param oldPassword
     * @param newPassword
     * @return java.lang.Object
     */
    @ResponseBody
    @RequestMapping(value = "/changePassword")
    public LayerPage changePassword(String oldPassword , String newPassword){
        if(StringUtils.isNullOrEmpty(oldPassword)){
            return LayerPage.error("请填写旧密码！");
        }
        if(StringUtils.isNullOrEmpty(newPassword)){
            return LayerPage.error("请填写新密码！");
        }
        if(!HttpServletHelper.getUser().getPasswd().equals(PasswdHelper.md5Password(oldPassword))){
            return LayerPage.error("旧密码与系统中不匹配");
        }
        if(newPassword.length()<7){
            return LayerPage.error("密码长度必须大于6位！");
        }
        newPassword= PasswdHelper.md5Password(newPassword);
        if(HttpServletHelper.getUser().getPasswd().equals(newPassword)){
            return LayerPage.error("新密码与旧密码相同，无需修改");
        }
        User user=HttpServletHelper.getUser();
        user.setPasswd(newPassword);
        userService.save(user);
        HttpServletHelper.setUser(user);
        return LayerPage.ok();
    }

    /**
     * @description: 修改个人资料
     * @createDate: 2020-06-04 17:14:31
     * @param user
     * @return java.lang.Object
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserInfo",method = RequestMethod.POST)
    public Object saveUserInfo(User user){
        User u=HttpServletHelper.getUser();
        u.setNikeName(user.getNikeName());
        u.setHeadimg(user.getHeadimg());
        u.setPhone(user.getPhone());
        u.setEmail(user.getEmail());

        user=userService.findUserByEmailOrPhone(user.getPhone());
        if(user==null){
            user=userService.findUserByEmailOrPhone(user.getEmail());
            if(user==null||user.getEmail().equals(u.getEmail())){
                userService.save(u);
                HttpServletHelper.setUser(u);
            }else{
                //邮箱已经注册
                return LayerPage.error("该邮箱号已经注册");
            }
        }else if(user.getPhone().equals(u.getPhone())){
            userService.save(u);
            HttpServletHelper.setUser(u);
        }else{
            //手机号已注册
            return LayerPage.error("该手机号已经注册");
        }
        return LayerPage.ok();
    }

}
