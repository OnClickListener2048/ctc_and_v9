package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Order {
    private String id;
    private String orderNum; //订单号
    private String state;   //订单状态
    private String goodsPic; //商品图片
    private String goodsName; //商品名称
    private String goodsPrice; //商品单价
    private String count;     //商品数量
    private String expressNum; //快递编号
    private String expressName;//快递名称
    private String totalPrice; //总价格

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    private  String goodsId;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order(String orderNum, String state, String goodsPic, String goodsName, String goodsPrice, String count, String expressNum, String expressName, String totalPrice) {
        this.orderNum = orderNum;
        this.state = state;
        this.goodsPic = goodsPic;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.count = count;
        this.expressNum = expressNum;
        this.expressName = expressName;
        this.totalPrice = totalPrice;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
