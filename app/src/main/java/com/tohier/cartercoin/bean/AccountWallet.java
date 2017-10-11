package com.tohier.cartercoin.bean;

/**
 * Created by Administrator on 2017/7/24.
 */

public class AccountWallet {

    private String IconName;//币种
    private String Money;//余额
    private String HaveMoney;//可用余额
    private String NoHaveMoney;//冻结余额

    public String getIconName() {
        return IconName;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getHaveMoney() {
        return HaveMoney;
    }

    public void setHaveMoney(String haveMoney) {
        HaveMoney = haveMoney;
    }

    public String getNoHaveMoney() {
        return NoHaveMoney;
    }

    public void setNoHaveMoney(String noHaveMoney) {
        NoHaveMoney = noHaveMoney;
    }

    public AccountWallet(String iconName, String money, String haveMoney, String noHaveMoney) {

        IconName = iconName;
        Money = money;
        HaveMoney = haveMoney;
        NoHaveMoney = noHaveMoney;
    }
}
