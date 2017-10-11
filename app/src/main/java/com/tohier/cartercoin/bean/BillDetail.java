package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/1/5.
 */

public class BillDetail {

    private String money;
    private String after;
    private String date;
    private String type;
    private String paycount;

    public String getMoney() {
        return money;
    }

    public String getAfter() {
        return after;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getPaycount() {
        return paycount;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaycount(String paycount) {
        this.paycount = paycount;
    }

    public BillDetail(String money, String after, String date, String type, String paycount) {
        this.money = money;
        this.after = after;
        this.date = date;
        this.type = type;
        this.paycount = paycount;
    }

    public BillDetail(String money, String date, String type) {
        this.money = money;
        this.date = date;
        this.type = type;
    }
}
