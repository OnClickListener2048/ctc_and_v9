package com.tohier.cartercoin.bean;

public class Address {

	private String consignee;
	private String address;
	private String province;
	private String city;
	private String weChat;
	private String mobile;
	private String id;
	private String default_address;

	public String getId() {
		return id;
	}

	public String getDefault_address() {
		return default_address;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDefault_address(String default_address) {
		this.default_address = default_address;
	}

	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Address(String consignee, String address, String province,
				   String city, String weChat, String mobile) {
		super();
		this.consignee = consignee;
		this.address = address;
		this.province = province;
		this.city = city;
		this.weChat = weChat;
		this.mobile = mobile;
	}

	public Address(String consignee, String address, String province, String city, String weChat, String mobile, String id, String default_address) {
		this.consignee = consignee;
		this.address = address;
		this.province = province;
		this.city = city;
		this.weChat = weChat;
		this.mobile = mobile;
		this.id = id;
		this.default_address = default_address;
	}

	public Address() {
		super();
	}
	
	
	
	
}
