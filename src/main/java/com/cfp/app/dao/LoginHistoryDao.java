package com.cfp.app.dao;

import com.cfp.app.model.LoginHistory;
import com.cfp.common.dao.BaseDao;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryDao extends BaseDao<LoginHistory> {
    
    public List<LoginHistory> findOneByUserIdOrderByLoginTimeDesc(String id, Pageable pageable);
}
