package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/10/24.
 */

public class YouHuiQuan {
    private String id;
    private String name;
    private String money;
    private String status;
    private String createDate;
    private String endDate;
    private String remark;
    private String min;
    private String sta;
    private String mon;
    private String type;

    public YouHuiQuan(String id, String name, String money, String status, String createDate, String endDate, String remark, String min, String sta, String mon, String type) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.status = status;
        this.createDate = createDate;
        this.endDate = endDate;
        this.remark = remark;
        this.min = min;
        this.sta = sta;
        this.mon = mon;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public YouHuiQuan(String id, String name, String money, String status, String createDate, String endDate, String remark, String sta, String mon) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.status = status;
        this.createDate = createDate;
        this.endDate = endDate;
        this.remark = remark;
        this.sta = sta;
        this.mon = mon;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public YouHuiQuan(String id, String name, String money, String status, String createDate, String endDate, String remark) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.status = status;
        this.createDate = createDate;
        this.endDate = endDate;
        this.remark = remark;
    }

    public YouHuiQuan(String sta, String id, String name, String money, String status, String createDate, String endDate, String remark) {
        this.sta = sta;
        this.id = id;
        this.name = name;
        this.money = money;
        this.status = status;
        this.createDate = createDate;
        this.endDate = endDate;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
