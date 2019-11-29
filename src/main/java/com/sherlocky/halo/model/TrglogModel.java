package com.sherlocky.halo.model;

/**
 * 触发器日志类
 * @author: zhangcx
 * @date: 2019/1/26 17:57
 */
public class TrglogModel {
    private Integer id;
    private String tablename;
    private String operatingType;
    private String guid;

    public TrglogModel() {
    }

    public TrglogModel(Integer id, String tablename, String operatingType, String guid) {
        this.id = id;
        this.tablename = tablename;
        this.operatingType = operatingType;
        this.guid = guid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
