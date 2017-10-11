package com.tohier.cartercoin.bean;

public class TiXianData {
	private String bankcard;
	private String qty;
	private String createdate;
	private String status;
	private String counterfee;
	private String cashoutid;

	public String getCashoutid() {
		return cashoutid;
	}

	public void setCashoutid(String cashoutid) {
		this.cashoutid = cashoutid;
	}



	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
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
	
	public String getCounterfee() {
		return counterfee;
	}
	public void setCounterfee(String counterfee) {
		this.counterfee = counterfee;
	}
	public TiXianData(String bankcard, String qty, String createdate,
					  String status, String counterfee) {
		super();
		this.bankcard = bankcard;
		this.qty = qty;
		this.createdate = createdate;
		this.status = status;
		this.counterfee = counterfee;
	}

	public TiXianData(String bankcard, String qty, String createdate, String status, String counterfee, String cashoutid) {
		this.bankcard = bankcard;
		this.qty = qty;
		this.createdate = createdate;
		this.status = status;
		this.counterfee = counterfee;
		this.cashoutid = cashoutid;
	}
}
