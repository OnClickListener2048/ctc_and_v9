package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2016/11/11.
 */

public class MiningInfo {

    private String times;   //已挖矿时间（秒）
    private String bonus;   //已挖币的数量
    private String bonustotal;  //这个时间段里总的挖币的数量
    private String refreshtimes;   //刷新的时间
    private String power;    //会员算力
    private String powertotal;  //全网算力
    private String sharecount;   //分享数量
    private String energyvalue;  //能量值
    private String active;            //活动
    private String task;               //任务量
    private String ctc;                  //卡特币的数量
    private String totlatimes;   //总的挖矿时间

    public MiningInfo(String times, String bonus, String bonustotal, String refreshtimes, String power, String powertotal, String sharecount, String energyvalue, String active, String task, String ctc, String totlatimes) {
        this.times = times;
        this.bonus = bonus;
        this.bonustotal = bonustotal;
        this.refreshtimes = refreshtimes;
        this.power = power;
        this.powertotal = powertotal;
        this.sharecount = sharecount;
        this.energyvalue = energyvalue;
        this.active = active;
        this.task = task;
        this.ctc = ctc;
        this.totlatimes = totlatimes;
    }

    public MiningInfo() {

    }

    public String getTimes() {
        return times;
    }

    public String getBonus() {
        return bonus;
    }

    public String getPowertotal() {
        return powertotal;
    }

    public String getRefreshtimes() {
        return refreshtimes;
    }

    public String getBonustotal() {
        return bonustotal;
    }

    public String getPower() {
        return power;
    }

    public String getSharecount() {
        return sharecount;
    }

    public String getEnergyvalue() {
        return energyvalue;
    }

    public String getActive() {
        return active;
    }

    public String getTask() {
        return task;
    }

    public String getCtc() {
        return ctc;
    }

    public String getTotlatimes() {
        return totlatimes;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public void setBonustotal(String bonustotal) {
        this.bonustotal = bonustotal;
    }

    public void setRefreshtimes(String refreshtimes) {
        this.refreshtimes = refreshtimes;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setPowertotal(String powertotal) {
        this.powertotal = powertotal;
    }

    public void setSharecount(String sharecount) {
        this.sharecount = sharecount;
    }

    public void setEnergyvalue(String energyvalue) {
        this.energyvalue = energyvalue;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setCtc(String ctc) {
        this.ctc = ctc;
    }

    public void setTotlatimes(String totlatimes) {
        this.totlatimes = totlatimes;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
