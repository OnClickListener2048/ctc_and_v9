package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/26.
 */

public class Record {
    private String id ;
    private String time;
    private String money;
    private String type;
    private String state;

    public Record(String time, String money, String type, String state) {
        this.time = time;
        this.money = money;
        this.type = type;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
}
