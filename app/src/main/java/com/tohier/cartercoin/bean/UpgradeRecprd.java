package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/30.
 */

public class UpgradeRecprd {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String level;
    private String time;
    private String payMode;
    private String Price,alipayPrice,weChatPrice;
    private String state;

    public String getPayMode() {
        return payMode;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAlipayPrice() {
        return alipayPrice;
    }

    public void setAlipayPrice(String alipayPrice) {
        this.alipayPrice = alipayPrice;
    }

    public String getWeChatPrice() {
        return weChatPrice;
    }

    public void setWeChatPrice(String weChatPrice) {
        this.weChatPrice = weChatPrice;
    }

    public UpgradeRecprd() {
    }

    public UpgradeRecprd(String level, String time, String payMode, String price, String weChatPrice, String alipayPrice, String state) {

        this.level = level;
        this.time = time;
        this.payMode = payMode;
        Price = price;
        this.weChatPrice = weChatPrice;
        this.alipayPrice = alipayPrice;
        this.state = state;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }



    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
