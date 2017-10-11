package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/3/31.
 */

public class AlipayData {

    private String id;
    private String name;
    private String account;

    public AlipayData(String id, String name, String account) {
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
