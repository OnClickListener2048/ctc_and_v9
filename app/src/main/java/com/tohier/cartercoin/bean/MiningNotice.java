package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MiningNotice {

    private String pic;
    private String contents;

    public MiningNotice(String pic, String contents) {
        this.pic = pic;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
