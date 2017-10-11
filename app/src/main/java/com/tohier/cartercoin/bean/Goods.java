package com.tohier.cartercoin.bean;

public class Goods {

	private String id;  //id
	private String code;  //产品编号
	private String name;// 名称
	private String pic; // 图片
	private String unitprice;//人民币单价
	private String unitctc; //卡特币单价
	private String desc; //描述
	private String qty;//总量
	private String qtyallow;//允许购买数量
	private String qtysell;//已卖出数量
	private String brandname;//品牌名称
	private String brandpic;//品牌图片
	private String content;//详细
	private String datebegin;//开始时间
	private String deteend;//结束时间
	private String sysdate;// 系统时间
	private boolean is_qidong_handler;
    private String begin_tishi;
    private String qtyme;//本人购买数量

	public String getBegin_tishi() {
		return begin_tishi;
	}

	public void setBegin_tishi(String begin_tishi) {
		this.begin_tishi = begin_tishi;
	}

	public void setIs_qidong_handler(boolean is_qidong_handler) {
		this.is_qidong_handler = is_qidong_handler;
	}

	public boolean getIs_qidong_handler() {
		return is_qidong_handler;
	}

	public Goods(String id, String code, String name, String pic,
				 String unitprice, String unitctc, String desc, String qty,
				 String qtyallow, String qtysell, String brandname, String brandpic,
				 String content, String datebegin, String deteend, String sysdate) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.pic = pic;
		this.unitprice = unitprice;
		this.unitctc = unitctc;
		this.desc = desc;
		this.qty = qty;
		this.qtyallow = qtyallow;
		this.qtysell = qtysell;
		this.brandname = brandname;
		this.brandpic = brandpic;
		this.content = content;
		this.datebegin = datebegin;
		this.deteend = deteend;
		this.sysdate = sysdate;
	}
	public String getDatebegin() {
		return datebegin;
	}
	public void setDatebegin(String datebegin) {
		this.datebegin = datebegin;
	}
	public String getDeteend() {
		return deteend;
	}
	public void setDeteend(String deteend) {
		this.deteend = deteend;
	}
	public String getSysdate() {
		return sysdate;
	}
	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(String unitprice) {
		this.unitprice = unitprice;
	}
	public String getUnitctc() {
		return unitctc;
	}
	public void setUnitctc(String unitctc) {
		this.unitctc = unitctc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getQtyallow() {
		return qtyallow;
	}
	public void setQtyallow(String qtyallow) {
		this.qtyallow = qtyallow;
	}
	public String getQtysell() {
		return qtysell;
	}
	public void setQtysell(String qtysell) {
		this.qtysell = qtysell;
	}
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public String getBrandpic() {
		return brandpic;
	}
	public void setBrandpic(String brandpic) {
		this.brandpic = brandpic;
	}
	public Goods(String id, String code, String name, String pic,
				 String unitprice, String unitctc, String desc, String qty,
				 String qtyallow, String qtysell, String brandname, String brandpic) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.pic = pic;
		this.unitprice = unitprice;
		this.unitctc = unitctc;
		this.desc = desc;
		this.qty = qty;
		this.qtyallow = qtyallow;
		this.qtysell = qtysell;
		this.brandname = brandname;
		this.brandpic = brandpic;
	}
	public Goods() {
		super();
	}
	public Goods(String id, String code, String name, String pic,
				 String unitprice, String unitctc, String desc, String qty,
				 String qtyallow, String qtysell, String brandname, String brandpic,
				 String datebegin, String deteend, String sysdate) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.pic = pic;
		this.unitprice = unitprice;
		this.unitctc = unitctc;
		this.desc = desc;
		this.qty = qty;
		this.qtyallow = qtyallow;
		this.qtysell = qtysell;
		this.brandname = brandname;
		this.brandpic = brandpic;
		this.datebegin = datebegin;
		this.deteend = deteend;
		this.sysdate = sysdate;
	}

	public String getQtyme() {
		return qtyme;
	}

	public void setQtyme(String qtyme) {
		this.qtyme = qtyme;
	}
}
