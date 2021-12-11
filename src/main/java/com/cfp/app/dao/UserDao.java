package com.cfp.app.dao;

import com.cfp.app.model.User;
import com.cfp.common.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User> {

    @Query("from User u where u.isDelete=:isDelete and u.passwd=:passwd and (u.email=:userName or u.phone=:userName)")
    public User findUserByEmailOrPhoneAndPasswdAndIsDelete(@Param("userName") String userName,@Param("passwd") String passwd,@Param("isDelete") Integer isDelete);

    @Query("from User u where u.isDelete=:isDelete and (u.email=:userName or u.phone=:userName)")
    public User findUserByEmailOrPhoneAndIsNotDelete(@Param("userName") String userName,@Param("isDelete") Integer isDelete);

    public User findUserById(String userId);

    @Query("from User u where u.email=:userName or u.phone=:userName")
    public User findUserByEmailOrPhone(@Param("userName") String userName);
}
