package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/6/16.
 */

public class RevisionAssetsBean {

    private String id;
    private String name;
    private String type;
    private String gold;
    private String silver;
    private String price;
    private String reductionPrice;
    private String qty;
    private String beginDate;
    private String endDate;
    private String status;
    private String cou;
    private String pre;
    private String pic;
    private String pro;
    private boolean isStartThread;
    private long istart;

    public RevisionAssetsBean(String id, String name, String type, String gold, String silver, String price, String reductionPrice, String qty, String beginDate, String endDate, String status, String cou, String pre, String pic,String pro,boolean isStartThread,long istart) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.gold = gold;
        this.silver = silver;
        this.price = price;
        this.reductionPrice = reductionPrice;
        this.qty = qty;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.status = status;
        this.cou = cou;
        this.pre = pre;
        this.pic = pic;
        this.pro = pro;
        this.isStartThread = isStartThread;
        this.istart = istart;
    }

    public long getIstart() {
        return istart;
    }

    public void setIstart(long istart) {
        this.istart = istart;
    }

    public boolean isStartThread() {
        return isStartThread;
    }

    public void setStartThread(boolean startThread) {
        isStartThread = startThread;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getGold() {
        return gold;
    }

    public String getSilver() {
        return silver;
    }

    public String getPrice() {
        return price;
    }

    public String getReductionPrice() {
        return reductionPrice;
    }

    public String getQty() {
        return qty;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getCou() {
        return cou;
    }

    public String getPre() {
        return pre;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setReductionPrice(String reductionPrice) {
        this.reductionPrice = reductionPrice;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCou(String cou) {
        this.cou = cou;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }
}
