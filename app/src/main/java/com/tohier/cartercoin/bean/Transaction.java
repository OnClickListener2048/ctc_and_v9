package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/11/11.
 */

public class Transaction {
    private String nodeal; //未成交
    private String deal;  //已成交
    private String state; //买卖状态
    private String time;  //成交时间
    private String price; //价格
    private String count;  //数量
    private String cumulative; // 累计
    private String per; //比重

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public Transaction(String state, String time, String price, String count, String cumulative) {
        this.state = state;
        this.time = time;
        this.price = price;
        this.count = count;
        this.cumulative = cumulative;
    }

    public Transaction() {

    }

    public Transaction(String nodeal, String deal, String state, String price, String count, String cumulative) {
        this.nodeal = nodeal;
        this.deal = deal;
        this.state = state;
        this.price = price;
        this.count = count;
        this.cumulative = cumulative;
    }

    public Transaction(String nodeal, String deal, String time, String state, String price, String count, String cumulative) {

        this.nodeal = nodeal;
        this.deal = deal;
        this.time = time;
        this.state = state;
        this.price = price;
        this.count = count;
        this.cumulative = cumulative;
    }

    public String getNodeal() {

        return nodeal;
    }

    public void setNodeal(String nodeal) {
        this.nodeal = nodeal;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCumulative() {
        return cumulative;
    }

    public void setCumulative(String cumulative) {
        this.cumulative = cumulative;
    }
}
