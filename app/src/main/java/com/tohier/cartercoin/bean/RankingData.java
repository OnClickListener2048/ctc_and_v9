package com.tohier.cartercoin.bean;

public class RankingData {
	
	
	public RankingData(){
		
	}
	
	public RankingData(String numUrl, String imgUrl, String name, String price) {
		super();
		this.numUrl = numUrl;
		this.imgUrl = imgUrl;
		this.name = name;
		this.price = price;
	}
	private String numUrl;
	private String imgUrl;// 头像地址
	private String name;// 名字
	private String price;// 金额
	private String ranking; //排行
	
	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}

	public String getNumUrl() {
		return numUrl;
	}

	public void setNumUrl(String numUrl) {
		this.numUrl = numUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
