package com.cfp.app.service;

import com.cfp.app.dao.UserDao;
import com.cfp.app.model.User;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.LayerPage;
import com.cfp.helper.PasswdHelper;
import com.mysql.cj.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @date 2020/03/12
 * describe:
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    /**
     * @description:  根据用户邮箱或者手机以及密码查找用户
     * @createDate: 2020-04-17 11:18:53
     * @param username
     * @param passwd
     * @return com.cfp.forum.admin.model.User
     */
    public User findUserByEmailOrPhoneAndPasswdAndIsNotDelete(String username, String passwd) {
        return userDao.findUserByEmailOrPhoneAndPasswdAndIsDelete(username,passwd,0);
    }

    public long getAllEnabledUserAll() {
        return userDao.count();
    }


    /**
     * @description: TODO : 用户注册信息
     * @createDate: 2020-04-17 11:04:33
     * @param secret
     * @param nickName
     * @param password
     * @param country
     * @param code
     * @return java.lang.Object
     */
    @Transactional
    public LayerPage registerUser(String secret, String nickName, String password, String country, String code) {
        User u=new User();
        if(!StringUtils.isNullOrEmpty(secret)){
            if(StringUtils.isNullOrEmpty(nickName)){
                return LayerPage.error("请填写昵称！");
            }
            if(StringUtils.isNullOrEmpty(password)){
                return LayerPage.error("请填写密码！");
            }
            if(password.length()<7){
                return LayerPage.error("密码长度必须大于6位！");
            }
            if(CheckHelper.checkEmail(secret)){
                u.setEmail(secret);
            }else if(CheckHelper.checkMobilePhone(secret)){
                u.setPhone(secret);
            }else{
                return LayerPage.error("手机号或邮箱格式错误！");
            }
            String userName="";
            if(u.getEmail()!=null){
                userName=u.getEmail();
            }else{
                userName=u.getPhone();
            }
            //判断用户是否已经注册
            User tmpU=this.findUserByEmailOrPhoneAndIsNotDelete(userName);
            if(tmpU!=null){
                return LayerPage.error("该用户已注册！");
            }
            // TODO: 判断验证码。本期预留
            u.setId(UUID.randomUUID().toString().replace("-",""));
            u.setCountryId(country);
            u.setNikeName(nickName);
            u.setPasswd(PasswdHelper.md5Password(password));
            int number = new Random().nextInt(9)+1;//从系统获取随机头像
            String path= HttpServletHelper.getRequest().getContextPath();
            u.setHeadimg(path+"/images/users/avatar-"+number+".jpg");
            u.setIsDelete(0);
            u.setScore(0);
            u.setCreatedDate(new Date());
            userDao.save(u);
            return LayerPage.ok();
        }else{
            return LayerPage.error("请填写手机号或邮箱！");
        }
    }

    /**
     * @description: 根据用户邮箱或者电话查找数据库是否存在过
     * @createDate: 2020-04-17 11:18:25
     * @param userName
     * @return com.cfp.forum.admin.model.User
     */
    private User findUserByEmailOrPhoneAndIsNotDelete(String userName) {
        return userDao.findUserByEmailOrPhoneAndIsNotDelete(userName,0);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User findUserById(String userId) {
        return userDao.findUserById(userId);
    }

    public User findUserByEmailOrPhone(String userName) {
        return userDao.findUserByEmailOrPhone(userName);
    }
    public List<User> findAll(){
        return userDao.findAll();
    }
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return userDao.findAll(example,pageable);
    }
}
