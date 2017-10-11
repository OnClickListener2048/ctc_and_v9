package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/5/16.
 */

public class Novice {

    private String title;//标题
    private String desc;//描述
    private String strength;//奖励体力
    private String state;//是否完成 0---未完成  else  完成

    public Novice(String title, String desc, String strength, String state) {
        this.title = title;
        this.desc = desc;
        this.strength = strength;
        this.state = state;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



}
