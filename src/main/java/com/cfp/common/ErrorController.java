package com.cfp.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @date 2020/01/08
 * describe:
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    /**
     * 404页面
     */
    @RequestMapping(value = "/404")
    public String error_404(Model model) {
        Date dateofyear=new Date();
        dateofyear.setYear(dateofyear.getYear()+1);
        model.addAttribute("dateofyear",dateofyear);
        return "error/404";
    }
    /**
     * 403页面
     */
    @RequestMapping(value = "/403")
    public String error_403(Model model) {
        return "error/403";
    }

    /**
     * 500页面
     */
    @RequestMapping(value = "/500")
    public String error_500(Model model) {
        return "error/500";
    }
}
