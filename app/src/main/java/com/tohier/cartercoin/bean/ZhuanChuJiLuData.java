package com.tohier.cartercoin.bean;

public class ZhuanChuJiLuData {

	private String id;
	private String address;
	private String qty;
	private String createdate;
	private String status;
	private String free;
	private String type; // 币种

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public ZhuanChuJiLuData() {
		super();
	}

	public ZhuanChuJiLuData(String id, String address, String qty, String createdate, String status, String free) {
		this.id = id;
		this.address = address;
		this.qty = qty;
		this.createdate = createdate;
		this.status = status;
		this.free = free;
	}
	
	
}
