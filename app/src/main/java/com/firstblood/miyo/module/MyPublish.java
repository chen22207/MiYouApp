package com.firstblood.miyo.module;

/**
 * Created by cs on 16/5/9.
 */
public class MyPublish {
	private String id;//房源id
	private String userid;//用户id
	private String title;//房源标题
	private String ico;//图标地址
	private String image;//主图地址
	private String time;//发布时间
	private String price;//价格/月
	private String clickcount;// 点击量
	private int specification;//[int]房屋户型（1一室 2二室 3三室 4四室及以上）
	private String specification_s;//[string] 房屋户型（文本）
	private String renovation;//装修规格(0:未知  1：简装修 2：中等装修 3：精装修 4：豪华装修)
	private int pricetype;//支付类型(0：面议 1：压一负一 2：压一付二 3：压一付三 4：半年付 5：年付)
	private String address;//地址
	private double address_x;//经度
	private double address_y;//纬度
	private String addressname;//地址名称
	private int isflatshare;//是否合租
	private String headindex;//首页序号（小于999会在首页显示）
	private String headphoto;//头像

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getClickcount() {
		return clickcount;
	}

	public void setClickcount(String clickcount) {
		this.clickcount = clickcount;
	}

	public int getSpecification() {
		return specification;
	}

	public void setSpecification(int specification) {
		this.specification = specification;
	}

	public String getSpecification_s() {
		return specification_s;
	}

	public void setSpecification_s(String specification_s) {
		this.specification_s = specification_s;
	}

	public String getRenovation() {
		return renovation;
	}

	public void setRenovation(String renovation) {
		this.renovation = renovation;
	}

	public int getPricetype() {
		return pricetype;
	}

	public void setPricetype(int pricetype) {
		this.pricetype = pricetype;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getAddress_x() {
		return address_x;
	}

	public void setAddress_x(double address_x) {
		this.address_x = address_x;
	}

	public double getAddress_y() {
		return address_y;
	}

	public void setAddress_y(double address_y) {
		this.address_y = address_y;
	}

	public String getAddressname() {
		return addressname;
	}

	public void setAddressname(String addressname) {
		this.addressname = addressname;
	}

	public int getIsflatshare() {
		return isflatshare;
	}

	public void setIsflatshare(int isflatshare) {
		this.isflatshare = isflatshare;
	}

	public String getHeadindex() {
		return headindex;
	}

	public void setHeadindex(String headindex) {
		this.headindex = headindex;
	}

	public String getHeadphoto() {
		return headphoto;
	}

	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}
}
