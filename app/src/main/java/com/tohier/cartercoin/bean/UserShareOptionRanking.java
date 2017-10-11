package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/8.
 */

public class UserShareOptionRanking {

    private String id;
    private String headUrl;
    private String ranking;
    private String count;
    private String nick;


    public UserShareOptionRanking(String headUrl, String ranking, String count,String nick) {
        this.headUrl = headUrl;
        this.ranking = ranking;
        this.count = count;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public UserShareOptionRanking() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
