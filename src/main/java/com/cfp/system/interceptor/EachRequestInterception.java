package com.cfp.system.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class EachRequestInterception implements HandlerInterceptor {
	private static Logger log= LoggerFactory.getLogger(EachRequestInterception.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	        throws Exception {
		if(handler instanceof HandlerMethod){//controller
			HandlerMethod hm=(HandlerMethod)handler;
			Method method=hm.getMethod();
			String className=hm.getBeanType().getName();
			String methodName=method.getName();
			log= LoggerFactory.getLogger(className);
			log.info(methodName+"开始执行");//方法名
		}else if(handler instanceof ResourceHttpRequestHandler){//静态资源
			ResourceHttpRequestHandler hm=(ResourceHttpRequestHandler)handler;
			log.info("静态资源访问");
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
		//记录日志
		if(handler instanceof HandlerMethod){//controller
			HandlerMethod hm=(HandlerMethod)handler;
			Method method=hm.getMethod();
			String className=hm.getBeanType().getName();
			String methodName=method.getName();
			log= LoggerFactory.getLogger(className);
			log.info(methodName+"执行完毕");//方法名
		}

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
