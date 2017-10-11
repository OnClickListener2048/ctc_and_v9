package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/5/17.
 */

public class PrizeBean {

    private String id;
    private String name;
    private String money;
    private String pic;
    private String type;

    public PrizeBean(String id, String name, String money, String pic, String type) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.pic = pic;
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMoney() {
        return money;
    }

    public String getPic() {
        return pic;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
