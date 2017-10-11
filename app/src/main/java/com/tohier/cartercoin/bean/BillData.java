package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/22.
 */

public class BillData {

    private String nickname;
    private String headImg;
    private String money;
    private String date;
    private boolean income;
    private String moneyType;

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public BillData(String nickname, String headImg, String money, String date) {
        this.nickname = nickname;
        this.headImg = headImg;
        this.money = money;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public String getMoney() {
        return money;
    }

    public String getDate() {
        return date;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }
}
