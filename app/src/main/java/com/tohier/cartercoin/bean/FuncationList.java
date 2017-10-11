package com.tohier.cartercoin.bean;

public class FuncationList {

	private int image ;
	private String text;
	public FuncationList(int image, String text) {
		super();
		this.image = image;
		this.text = text;
	}
	public FuncationList() {
		super();
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	};
	
}
