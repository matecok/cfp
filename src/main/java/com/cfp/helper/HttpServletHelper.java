package com.cfp.helper;

import com.cfp.app.model.CloudFlareAccount;
import com.cfp.app.model.User;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @date 2019/10/28
 * describe:
 */
public class HttpServletHelper {

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    public static User getUser(){
        return (User)getSession().getAttribute("user");
    }
    public static User setUser(User user){
        getSession().setAttribute("user",user);
        return user;
    }
    public static CloudFlareAccount getCfAccount(){
        return (CloudFlareAccount)getSession().getAttribute("cfa");
    }
    public static CloudFlareAccount setCfAccount(CloudFlareAccount cfa){
        getSession().setAttribute("cfa",cfa);
        return cfa;
    }
}
