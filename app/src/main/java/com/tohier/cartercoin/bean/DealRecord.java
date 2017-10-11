package com.tohier.cartercoin.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/30.
 */

public class DealRecord implements Serializable{
    private String money;   //下单金额
    private String profit;  //盈利情况
    private String orderNum;  //订单编号
    private String type;    //币种
    private String state;   //买入方向
    private String cycle;  //周期
    private String orderTime; //下单时间
    private String expireTime; //到期时间
    private String orderPrice;  //下单价格
    private String expirePrice;  //到期价格
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    private String result; //交易结果
    private String payAccount;


    public DealRecord(String money, String profit, String orderNum, String type, String state, String cycle, String orderTime, String orderPrice, String expireTime, String expirePrice, String result) {
        this.money = money;
        this.profit = profit;
        this.orderNum = orderNum;
        this.type = type;
        this.state = state;
        this.cycle = cycle;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.expireTime = expireTime;
        this.expirePrice = expirePrice;
        this.result = result;
    }

    public DealRecord() {
    }

    public DealRecord(String type, String state, String expireTime, String money, String profit) {
        this.type = type;
        this.state = state;
        this.expireTime = expireTime;
        this.money = money;
        this.profit = profit;
    }


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getExpirePrice() {
        return expirePrice;
    }

    public void setExpirePrice(String expirePrice) {
        this.expirePrice = expirePrice;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
