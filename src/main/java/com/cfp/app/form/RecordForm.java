package com.cfp.app.form;

public class RecordForm {
   // {"type":"A","name":"example.com","content":"127.0.0.1","ttl":120,"priority":10,"proxied":false}
    private String id;
    private String type="A";
    private String name;
    private String content;
    private Integer ttl=1;
    private Integer priority=10;
    private boolean proxied=false;

    public RecordForm(String name,String content){
        this.name=name;
        this.content=content;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isProxied() {
        return proxied;
    }

    public void setProxied(boolean proxied) {
        this.proxied = proxied;
    }
}
