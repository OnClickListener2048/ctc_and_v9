package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/12/20.
 */

public class ContributionData {

    private String allContribution;
    private String contributionToday;
    private String name;
    private String phone;
    private String picUrl;
    private String id;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    private String linkCode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContributionData(String allContribution, String contributionToday, String name) {
        this.allContribution = allContribution;
        this.contributionToday = contributionToday;
        this.name = name;
    }

    public String getAllContribution() {
        return allContribution;
    }

    public String getContributionToday() {
        return contributionToday;
    }

    public String getName() {
        return name;
    }

    public void setAllContribution(String allContribution) {
        this.allContribution = allContribution;
    }

    public void setContributionToday(String contributionToday) {
        this.contributionToday = contributionToday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
