package com.tohier.cartercoin.bean;

public class News {

	private String id;
	
	/**
	 * ����
	 */
	private String title;
	/**
	 * ͼƬ·��
	 */
	private String pic;
	/**
	 * �����
	 */
	private String clicks;
	/**
	 * ������
	 */
	private String likes;
	/**
	 * ����ʱ��
	 */
	private String createdate;
	/**
	 * ���ŵ�ַ
	 */
	private String desc;

	private String kid;

	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 0---new  1---old

	 */
	private String status;

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public News() {
		super();
	}
	public News(String id, String title, String pic, String clicks,
				String likes, String createdate) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.clicks = clicks;
		this.likes = likes;
		this.createdate = createdate;
	}
	public News(String id, String title, String pic, String clicks,
				String likes, String createdate, String desc, String url) {
		super();
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.clicks = clicks;
		this.likes = likes;
		this.createdate = createdate;
		this.desc = desc;
		this.url = url;
	}

	public News(String id, String title, String pic, String clicks, String likes, String createdate, String desc, String kid, String url) {
		this.id = id;
		this.title = title;
		this.pic = pic;
		this.clicks = clicks;
		this.likes = likes;
		this.createdate = createdate;
		this.desc = desc;
		this.kid = kid;
		this.url = url;
	}
}
