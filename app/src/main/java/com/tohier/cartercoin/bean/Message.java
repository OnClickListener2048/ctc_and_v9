package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/25.
 */

public class Message {

    private String title;
    private String time;
    private String desc;

    public Message(String title, String time, String desc) {
        this.title = title;
        this.time = time;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDesc() {
        return desc;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
