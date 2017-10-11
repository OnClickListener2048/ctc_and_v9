package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ShareBonus {

    private  String money; //福利金额
    private String time; //福利时间
    private String α_me;  //持币量
    private String α_all; //流通量
    private String rate;//占比
    private String moneyAll;//盈利总额

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getΑ_me() {
        return α_me;
    }

    public void setΑ_me(String α_me) {
        this.α_me = α_me;
    }

    public String getΑ_all() {
        return α_all;
    }

    public void setΑ_all(String α_all) {
        this.α_all = α_all;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMoneyAll() {
        return moneyAll;
    }

    public void setMoneyAll(String moneyAll) {
        this.moneyAll = moneyAll;
    }

    public ShareBonus(String money, String time, String α_me, String α_all, String rate, String moneyAll) {

        this.money = money;
        this.time = time;
        this.α_me = α_me;
        this.α_all = α_all;
        this.rate = rate;
        this.moneyAll = moneyAll;
    }
}
