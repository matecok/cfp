package com.cfp.app.service;

import com.cfp.app.dao.CloudFlareAccountDao;
import com.cfp.app.model.CloudFlareAccount;
import com.cfp.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CloudFlareAccountService extends BaseService<CloudFlareAccount> {
    @Autowired
    private CloudFlareAccountDao cloudFlareAccountDao;

    public CloudFlareAccount selectOneByUserId(String userId) {
        return cloudFlareAccountDao.selectOneByUserId(userId);
    }

    public CloudFlareAccount selectOneByCloudflareEmail(String cloudflareEmail) {
        return cloudFlareAccountDao.selectOneByCloudflareEmail(cloudflareEmail);
    }
}
