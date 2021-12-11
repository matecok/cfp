package com.cfp.app.dao;

import com.cfp.app.model.CloudFlareAccount;
import com.cfp.common.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudFlareAccountDao extends BaseDao<CloudFlareAccount> {
    
    @Query("FROM CloudFlareAccount cfa WHERE cfa.userId=:userId")
    public CloudFlareAccount selectOneByUserId(@Param("userId") String userId);

    @Query("FROM CloudFlareAccount cfa WHERE cfa.cloudflareEmail=:cloudflareEmail")
    CloudFlareAccount selectOneByCloudflareEmail(@Param("cloudflareEmail") String cloudflareEmail);
}
