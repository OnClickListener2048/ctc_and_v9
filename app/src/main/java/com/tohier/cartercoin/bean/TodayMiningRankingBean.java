package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/3/13.
 */

public class TodayMiningRankingBean {

    private String pic;
    private String nickname;
    private String miningCount;

    public TodayMiningRankingBean(String pic, String nickname, String miningCount) {
        this.pic = pic;
        this.nickname = nickname;
        this.miningCount = miningCount;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMiningCount(String miningCount) {
        this.miningCount = miningCount;
    }

    public String getMiningCount() {
        return miningCount;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPic() {
        return pic;
    }
}
