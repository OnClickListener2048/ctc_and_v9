package com.tohier.cartercoin.bean;

import java.io.Serializable;

public class CardData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; //卡id
	private String banktype;  //银行类型
	private String account;  //卡号
	private String province; //城市
	private String bankname; //支行名
	private String name;	//户名
	private String iconUrl; //银行卡图标url

	public String getBanktype() {
		return banktype;
	}
	public void setBantype(String banktype) {
		this.banktype = banktype;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public CardData(String id, String banktype, String account,
					String province, String bankname, String name) {
		super();
		this.id = id;
		this.banktype = banktype;
		this.account = account;
		this.province = province;
		this.bankname = bankname;
		this.name = name;
	}


	public CardData(String id, String banktype, String account, String province, String bankname, String name, String iconUrl) {
		this.id = id;
		this.banktype = banktype;
		this.account = account;
		this.province = province;
		this.bankname = bankname;
		this.name = name;
		this.iconUrl = iconUrl;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setBanktype(String banktype) {
		this.banktype = banktype;
	}
	
	
	
	

}
