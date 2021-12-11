package com.cfp.common.service;

import com.cfp.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BaseService<T> {
    @Autowired
    private BaseDao<T> baseDao;

    public List<T> findAll(){
        return baseDao.findAll();
    }

    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable){
        return baseDao.findAll(example,pageable);
    }

    public <S extends T> void save(S model){
        baseDao.save(model);
    }
    public <S extends T> void delete(S model){
        baseDao.delete(model);
    }
}
