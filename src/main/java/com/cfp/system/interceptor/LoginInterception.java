package com.cfp.system.interceptor;

import com.cfp.app.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Component
public class LoginInterception implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		User user= (User) request.getSession().getAttribute("user");
		if(user==null){
			StringBuffer afterURL=request.getRequestURL();
			String queryString=request.getQueryString();
			if(queryString!=null){
				afterURL.append("?"+queryString);
			}
			String redirect=URLEncoder.encode( afterURL.toString(), "UTF-8" );
			//登录判断验证
			response.sendRedirect("/login?redirect="+redirect);
			return false;
		}
	    return true;
	 }
	/**
	 * description: preHandle 返回true才执行  在这个方法中你可以对ModelAndView进行操作
	 * create time: 2019/10/15 14:36
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}
	/**
	  * description: preHandle 返回true才执行  DispatcherServlet进行视图的渲染之后
	  * create time: 2019/10/15 14:36
	  */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}
}
