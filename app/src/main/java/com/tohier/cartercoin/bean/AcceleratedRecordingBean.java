package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/5/5.
 */

public class AcceleratedRecordingBean  {

    private String address;
    private String name;
    private String type;
    private String cou;
    private String date;

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCou() {
        return cou;
    }

    public String getDate() {
        return date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCou(String cou) {
        this.cou = cou;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AcceleratedRecordingBean(String address, String name, String type, String cou, String date) {
        this.address = address;
        this.name = name;
        this.type = type;
        this.cou = cou;
        this.date = date;
    }
}
