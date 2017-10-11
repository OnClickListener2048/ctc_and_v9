package com.tohier.cartercoin.bean;

public class Product {

	private String id;
	private String picpath;
	private String url;
	private String desc;
	private String title;

	public String getIsActivityList() {
		return isActivityList;
	}

	public void setIsActivityList(String isActivityList) {
		this.isActivityList = isActivityList;
	}

	/**
	 * false---h5
	 * true---活动列表
	 */

	private String isActivityList;

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicpath() {
		return picpath;
	}

	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Product(String id, String picpath, String url) {

		this.id = id;
		this.picpath = picpath;
		this.url = url;
	}

	public Product() {
		super();
	}
}
