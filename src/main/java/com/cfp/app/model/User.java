package com.cfp.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @date 2019/10/28
 * describe:
 */
@Entity
@Table(name = "USER")
public class User {

    @Id //主键
    @Column(name="id",length = 64)
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "assigned")
    private String id;
    @Column(name="nike_name",length = 20)
    private String nikeName;//昵称
    @Column(name="headimg",length = 255)
    private String headimg;//头像路径
    @Column(name="passwd",length = 32)
    private String passwd;//密码【加密后】
    @Column(name="phone",length = 20)
    private String phone;//手机
    @Column(name="email",length = 100)
    private String email;//邮箱
    @Column(name="score")
    private Integer score;//用户积分
    @Column(name="is_delete")
    private Integer isDelete;//是否删除 默认0 否     1已删除
    @Column(name="country_id",length = 32)
    private String countryId;//国家id
    @Column(name="province_id",length = 32)
    private String provinceId;//省id
    @Column(name="city",length = 100)
    private String city;//城市名称
    @Column(name="area_code")
    private Integer areaCode;//区号默认
    @Column(name="rule",length = 255)
    private String rule; //人员权限  admin管理员
    @Column(name="created_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdDate;//创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
