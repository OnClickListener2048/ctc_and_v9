package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class WinningRecordBean {

    private String detail;
    private String time;

    public WinningRecordBean(String detail, String time) {
        this.detail = detail;
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
