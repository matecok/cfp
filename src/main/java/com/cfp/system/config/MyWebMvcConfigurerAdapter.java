package com.cfp.system.config;

import com.cfp.system.interceptor.EachRequestInterception;
import com.cfp.system.interceptor.LoginInterception;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurationSupport {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //中文乱码解决
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(0, stringConverter);
    }

    @Bean
    public HandlerInterceptor getUserInterceptor(){
        return new EachRequestInterception();
    }


    @Bean
    public HandlerInterceptor getLoginInterceptor(){
        return new LoginInterception();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**")//后台
                .excludePathPatterns("/css/**","/fonts/**","/images/**","/js/**","/libs/**")//排除静态资源
                .excludePathPatterns("/login","/login/info","/register","/registerData","/forgotPasswd"
                ,"/resetPassword");//不需要登陆的
        registry.addInterceptor(new EncondingInterceptor()).addPathPatterns("/**");//字符编码修改拦截器
        registry.addInterceptor(getUserInterceptor()).addPathPatterns("/**");//注入所有拦截器
        super.addInterceptors(registry);
    }
  
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
    }

    
}