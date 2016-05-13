package com.firstblood.miyo.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cs on 16/4/27.
 */
public class House {
	private String id;
	@SerializedName("userid")
	private String userId;
	private String title;
	private String time;
	@SerializedName("clickcount")
	private String clickCount;
	private int specification;//0所有，1一室，2二室，3三室，4四室以上
	private int renovation;//0未知，1简装，2中等，3精装，4豪华
	private int isflatshare;//0未知，1整租，2合租
	@SerializedName("pricetype")
	private int priceType;//0面议，1押一付一，2押一付二，3押一付三，4半年付，5年付
	private String ico;//图片地址
	private String image;
	private String price;
	private String headphoto;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public int getSpecification() {
		return specification;
	}

	public void setSpecification(int specification) {
		this.specification = specification;
	}

	public int getRenovation() {
		return renovation;
	}

	public void setRenovation(int renovation) {
		this.renovation = renovation;
	}

	public int getIsflatshare() {
		return isflatshare;
	}

	public void setIsflatshare(int isflatshare) {
		this.isflatshare = isflatshare;
	}

	public String getIsflatshareStr() {
		String str = "";
		switch (isflatshare) {
			case 0:
				str = "未知";
				break;
			case 1:
				str = "整租";
				break;
			case 2:
				str = "合租";
				break;
		}
		return str;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getHeadphoto() {
		return headphoto;
	}

	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}
}
