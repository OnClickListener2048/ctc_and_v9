package com.tohier.cartercoin.bean;

import android.support.annotation.NonNull;

import com.tohier.cartercoin.config.PinyinUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/24.
 */
public class ZanRanking implements Comparable<ZanRanking>,Serializable {

    private String id;
    private String name; //昵称
    private String pic;  //头像
    private String level;
    private String praisenum;
    private String ranking;
    private String praiseme;
    private String power;
    private String mobile; //手机号
    private String linkCode;//链接码
    private String type;//会员级别
    private String pinYin; //昵称转换之后的拼音
    private String level1; //挖矿级别
    private String count;//亲密度
    private String area;//地区
    private String pra; //主动 0---没点  1---点了
    private String bepra; //被动 0---没点  1---点了
    private String zanType; //0---主动 1---被动  2---双向   3---都没有点
    private String oneau;//签名
    private String remark;//备注
    private String sex; //性别
    private String sta; //0---不是好友  1---是好友
    private String backgroundpic; //背景图片
    private String status; //接受---未处理的请求   //已添加  //已拒绝

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    private String paymentCode; //付款码

    public String getPra() {
        return pra;
    }

    public void setPra(String pra) {
        this.pra = pra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBackgroundpic() {
        return backgroundpic;
    }

    public void setBackgroundpic(String backgroundpic) {
        this.backgroundpic = backgroundpic;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOneau() {
        return oneau;
    }

    public void setOneau(String oneau) {
        this.oneau = oneau;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZanType() {
        return zanType;
    }

    public void setZanType(String zanType) {
        this.zanType = zanType;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkCode() {
        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getPraiseme() {
        return praiseme;
    }

    public void setPraiseme(String praiseme) {
        this.praiseme = praiseme;
    }

    public ZanRanking() {
    }

    public ZanRanking(String name, String pic, String level, String praisenum) {
        this.name = name;
        this.pic = pic;
        this.level = level;
        this.praisenum = praisenum;
        this.pinYin = PinyinUtil.getPinyin(name);
    }

    public ZanRanking(String name, String pic, String level, String praisenum, String ranking, String power, String mobile) {

        this.name = name;
        this.pic = pic;
        this.level = level;
        this.praisenum = praisenum;
        this.ranking = ranking;
        this.power = power;
        this.mobile = mobile;
        this.pinYin = PinyinUtil.getPinyin(name);
    }

    public ZanRanking(String id ,String name, String pic, String linkCode, String type, String level1, String count, String area) {
        this.name = name;
        this.pic = pic;
        this.linkCode = linkCode;
        this.type = type;
        this.pinYin = PinyinUtil.getPinyin(name);
        this.level1 = level1;
        this.count = count;
        this.area = area;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(String praisenum) {
        this.praisenum = praisenum;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    @Override
    public int compareTo(@NonNull ZanRanking o) {

        if(pinYin!=null&&pinYin.length()>0&&o.pinYin!=null&&o.pinYin.length()>0)
        {
            if ((this.pinYin.charAt(0)+"").equals("#") && !(o.pinYin.charAt(0)+"").equals("#")) {
                return 1;
            } else if (!(this.pinYin.charAt(0)+"").equals("#") && (o.pinYin.charAt(0)+"").equals("#")){
                return -1;
            } else {
                return pinYin.compareToIgnoreCase(o.getPinYin());
            }
        }else
        {
            return pinYin.compareToIgnoreCase(o.getPinYin());
        }

    }
}
