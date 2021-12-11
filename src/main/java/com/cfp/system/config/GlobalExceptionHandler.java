package com.cfp.system.config;

import com.cfp.helper.LayerPage;
import com.cfp.helper.exception.BusinessException;
import com.mysql.cj.core.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public LayerPage BusinessExceptionHandler(BusinessException e){
        if(!StringUtils.isNullOrEmpty(e.getCode())&&e.getCode().equals("20211121")){
            return LayerPage.ok(e.getMessage());
        }
        return LayerPage.error(e.getMessage());
    }
}
