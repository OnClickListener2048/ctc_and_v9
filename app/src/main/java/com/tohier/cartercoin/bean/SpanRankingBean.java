package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/6/4.
 */

public class SpanRankingBean {

    private String name;
    private String pic;
    private String count;
    private String ranking;

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getName() {
        return name;
    }

    public String getRanking() {
        return ranking;
    }

    public String getPic() {
        return pic;
    }

    public String getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public SpanRankingBean(String name, String pic, String count, String ranking) {
        this.name = name;
        this.pic = pic;
        this.count = count;
        this.ranking = ranking;
    }
}
