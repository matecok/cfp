package com.cfp.app.model;

import com.cfp.common.ValidAnnotation;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "CLOUDFLARE_ACCOUNT")
public class CloudFlareAccount {
    /**主键**/
    @Id
    @GeneratedValue(generator = "id")
    @GenericGenerator(name = "id", strategy = "assigned")
    @Column(name="id",length = 64)
    private String id;


    @Column(name = "user_id", length = 64)
    private String userId;

    @ValidAnnotation(value = ValidAnnotation.Type.EMAIL,filedName = "CloudFlare")
    @Column(name = "cloudflare_email", length = 255)
    private String cloudflareEmail;

    @ValidAnnotation(value = ValidAnnotation.Type.REQUIRED,filedName = "密码")
    @Column(name = "cloudflare_pass", length = 255)
    private String cloudflarePass;
    
    @Transient
    private String userKey;
    
    @Transient
    private String userApiKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCloudflareEmail() {
        return cloudflareEmail;
    }

    public void setCloudflareEmail(String cloudflareEmail) {
        this.cloudflareEmail = cloudflareEmail;
    }

    public String getCloudflarePass() {
        return cloudflarePass;
    }

    public void setCloudflarePass(String cloudflarePass) {
        this.cloudflarePass = cloudflarePass;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserApiKey() {
        return userApiKey;
    }

    public void setUserApiKey(String userApiKey) {
        this.userApiKey = userApiKey;
    }
    
    public String valid(){
        return ValidAnnotation.Valid.Valid(this);
    }
}
