package com.cfp.app.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YangZhen
 * @date 2020/03/10
 * describe:
 */
public class CheckHelper {

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkMobilePhone(String phone) {
        boolean flag=false;
        try {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            if (phone.length() != 11) {
                flag= false;
            } else {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phone);
                flag = m.matches();
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
