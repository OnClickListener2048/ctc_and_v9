package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/3/10.
 */

public class NewMiningInfo {

    private String id;
    private String endDate;
    private String bouns;

    public NewMiningInfo(String id, String endDate, String bouns) {
        this.id = id;
        this.endDate = endDate;
        this.bouns = bouns;
    }

    public String getId() {
        return id;
    }

    public String getBouns() {
        return bouns;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setBouns(String bouns) {
        this.bouns = bouns;
    }
}
