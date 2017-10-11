package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/11/11.
 */

public class Entrust {
    private String id; //id
    private String rownum; //序号
    private String state; //买卖状态
    private String entrustCount; //委托数量
    private String entrustPricre; //委托价格
    private String cjCount; //成交数量
    private String noCjCount; //尚未成交数量
    private String time;  //时间
    private String total; //累计

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRownum() {
        return rownum;
    }

    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    public Entrust(String id, String rownum , String state, String entrustCount, String entrustPricre, String cjCount, String noCjCount, String time) {
        this.id = id;
        this.rownum = rownum;
        this.state = state;
        this.entrustCount = entrustCount;
        this.entrustPricre = entrustPricre;
        this.cjCount = cjCount;
        this.noCjCount = noCjCount;
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEntrustCount() {
        return entrustCount;
    }

    public void setEntrustCount(String entrustCount) {
        this.entrustCount = entrustCount;
    }

    public String getEntrustPricre() {
        return entrustPricre;
    }

    public void setEntrustPricre(String entrustPricre) {
        this.entrustPricre = entrustPricre;
    }

    public String getCjCount() {
        return cjCount;
    }

    public void setCjCount(String cjCount) {
        this.cjCount = cjCount;
    }

    public String getNoCjCount() {
        return noCjCount;
    }

    public void setNoCjCount(String noCjCount) {
        this.noCjCount = noCjCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
