package com.cfp.helper;

import com.mysql.cj.core.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class LayerPage {

    private Integer code=200;//状态码
    private boolean flag=true;//状态信息
    private String msg="SUCCESS";//解析提示文本
    private Long iTotalDisplayRecords;//解析数据长度
    private Page page;//spring page
    private Object data;//解析数据
    
    public static LayerPage ok(String msg){
        return status(true,msg);
    }
    public static LayerPage ok(){
        return status(true,"SUCCESS");
    }
    
    public static LayerPage error(String msg){
        return status(false,msg);
    }
    public static LayerPage error(){
        return status(false,"ERROR");
    }
    
    private static LayerPage status(boolean flag,String msg){
        LayerPage layerPage=new LayerPage();
        layerPage.setFlag(flag);
        layerPage.setMsg(StringUtils.isNullOrEmpty(msg)?"":msg);
        return layerPage;
    }

    public LayerPage(){}
    public LayerPage(Page page){
        this.page=page;
        this.iTotalDisplayRecords=page.getTotalElements();
        this.data=page.getContent();
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Object getData() {
        return data;
    }

    public Long getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Long iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static Sort orderType(String orderType,String... field){
        Sort.Direction sort=orderType==null?Sort.Direction.ASC:orderType.toUpperCase().equals(Sort.Direction.ASC)?
                Sort.Direction.ASC:orderType.toUpperCase().equals(Sort.Direction.DESC)?Sort.Direction.DESC:Sort.Direction.ASC;
        return Sort.by(sort,field);
    }
}
