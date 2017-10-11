package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/4/25.
 */

public class Assets {
    private String id;
    private String introduce;
    private String name;
    private String value;
    private String price;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Assets(String id, String introduce, String name, String value, String price) {

        this.id = id;
        this.introduce = introduce;
        this.name = name;
        this.value = value;
        this.price = price;
    }
}
