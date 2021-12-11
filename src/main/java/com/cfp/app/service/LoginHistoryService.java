package com.cfp.app.service;

import com.cfp.app.dao.LoginHistoryDao;
import com.cfp.app.model.LoginHistory;
import com.cfp.common.service.BaseService;
import com.cfp.helper.ErrorHelper;
import com.cfp.helper.HttpServletHelper;
import com.cfp.helper.UuidHelper;
import com.cfp.helper.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description: 登录历史 
 * @className: LoginHistoryService
 * @createDate: 2021-08-03 09:48:17
 */
@Service
public class LoginHistoryService extends BaseService<LoginHistory> {

    private final static Logger logger = LoggerFactory.getLogger(LoginHistoryService.class);
    @Autowired
    private LoginHistoryDao loginHistoryDao;
    
    public void addLoginHistory(){
        try{
            String ip = HttpServletHelper.getRequest().getHeader("X-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
                ip = HttpServletHelper.getRequest().getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
                ip = HttpServletHelper.getRequest().getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
                ip = HttpServletHelper.getRequest().getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
                ip = HttpServletHelper.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
                ip = HttpServletHelper.getRequest().getRemoteAddr();
            }
            LoginHistory lh=new LoginHistory();
            lh.setLoginIp(ip);
            lh.setLoginTime(new Date());
            lh.setId(UuidHelper.id());
            lh.setUserId(HttpServletHelper.getUser().getId());
            this.save(lh);
        }catch (Exception e){
            logger.error(ErrorHelper.getErrorMessage(e));
        }
    }

    public LoginHistory findOneByUserIdOrderByLoginTimeDesc() {
        try{
            //默认0页，当前登录记录，所以取第二页 获取上次登录ip
            Pageable pageable=PageRequest.of(1,1);
            List<LoginHistory> list=loginHistoryDao.findOneByUserIdOrderByLoginTimeDesc(HttpServletHelper.getUser().getId(),pageable);
            if(list.size()>0){
                return list.get(0);
            }
            return null;
        }catch (Exception e){
            throw new BusinessException("加载登录历史出错");
        }
    }
}
