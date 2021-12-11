package com.cfp.common;

import com.cfp.helper.ErrorHelper;
import com.mysql.cj.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @description: 实体类字段校验类
 * @className: ValidAnnotation
 * @params {value ValidAnnotation.Type
 *  filedName 字段名称 和value连用,会拼在错误消息开头
 *  pattern 字段验证正则
 *  msg 验证失败提示语}
 * @createDate: 2021-07-23 09:03:42
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAnnotation {
    public static class Valid{
        private final static Logger logger = LoggerFactory.getLogger(Valid.class);
        public static String Valid(Object o){
            StringBuffer errorMsg=new StringBuffer();
            Field[] fields=o.getClass().getDeclaredFields();
            for(Field f : fields){
                //获取字段中包含fieldMeta的注解  
                ValidAnnotation valid = f.getAnnotation(ValidAnnotation.class);
                if(valid!=null){
                    String msg="";
                    String pattern="";
                    if(!StringUtils.isNullOrEmpty(valid.msg()) && !StringUtils.isNullOrEmpty(valid.pattern())){
                        msg=valid.msg();
                        pattern=valid.pattern();
                    }else{
                        String filedName= StringUtils.isNullOrEmpty(valid.filedName())?"":valid.filedName()+"：";
                        msg=filedName+valid.value().getMsg();
                        pattern=valid.value().getPattern();
                    }
                    try{
                        f.setAccessible(true);
                        String str= (String) f.get(o);
                        str=StringUtils.isNullOrEmpty(str)?"":str;
                        boolean result = Pattern.matches(pattern, str);
                        if(!result){
                            errorMsg.append(msg+"；");
                        }
                    }catch (IllegalAccessException e){
                        logger.error(ErrorHelper.getErrorMessage(e));
                    }
                }
            }
            if(errorMsg.length()>0){
                errorMsg.deleteCharAt(errorMsg.length()-1);
            }
            return errorMsg.toString();
        }
    }
    
    public enum Type{
        //校验类型
        REQUIRED("[\\s\\S]*.*[^\\s][\\s\\S]*","必填项"),
        NUMBER("[0-9]*","必须为数字"),
        CHINESE("[\\u4e00-\\u9fa5]{0,}","只能为汉字"),
        LETTERANDNUMBER("[A-Za-z0-9]+","只能为字母+数字组合"),
        EMAIL("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*","邮箱格式不正确"),
        PHONE("(1\\d{10}","手机号码格式不正确"),
        PASSWORD("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}","密码格式不正确，长度在6-20位之间"),
        IPV4("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))","IPv4地址格式不正确"),
        DOMAIN("(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+","域名格式不正确");


        /** 正则表达式 **/
        private String pattern ;
        /** 错误提示 **/
        private String msg;
        
        private Type(String pattern,String msg){
            this.pattern=pattern;
            this.msg=msg;
        }

        public String getPattern() {
            return pattern;
        }

        public String getMsg() {
            return msg;
        }
    }

    /** 错误消息 **/
    String msg() default "";

    /** 正则表达式 **/
    String pattern() default "";
    
    /** 字段名称 和value连用,会拼在错误消息开头 **/
    String filedName() default "";

    Type value() default Type.REQUIRED;
        
}
