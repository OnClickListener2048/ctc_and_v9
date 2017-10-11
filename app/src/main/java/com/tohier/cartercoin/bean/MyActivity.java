package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/1/9.
 */

public class MyActivity {
    private String title;  //标题
    private String imageUrl;  //图片
    private String startDate;
    private String endDate;
    private String id;
    private String type;    //类型
    private String code;  //排行旁的接口
    private String code1;  //个人的接口
    private String ranking; //0---没有排行   1---有排行


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public MyActivity() {

    }

    public MyActivity(String imageUrl, String startDate, String endDate, String id, String type, String code, String code1) {

        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.id = id;
        this.type = type;
        this.code = code;
        this.code1 = code1;
    }
}
