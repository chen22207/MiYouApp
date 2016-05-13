package com.firstblood.miyo.module;

/**
 * Created by cs on 16/5/13.
 */
public class HouseDetail {
	private int iscollect;//0未收藏，1已收藏
	private String title;//房源标题
	private String ico;//图标地址
	private String image;//主图地址
	private String time;//发布时间
	private String price;//价格/月
	private String clickcount;// 点击量
	private int specification;//[int]房屋户型（1一室 2二室 3三室 4四室及以上）
	private String specification_s;//[string] 房屋户型（文本）
	private int renovation;//装修规格(0:未知  1：简装修 2：中等装修 3：精装修 4：豪华装修)
	private int pricetype;//支付类型(0：面议 1：压一负一 2：压一付二 3：压一付三 4：半年付 5：年付)
	private int orientation;//（1 东 2 南 3 西 4 北）
	private String size;//面积
	private String address;//地址
	private double address_x;//经度
	private double address_y;//纬度
	private String addressname;//地址名称
	private int isflatshare;//是否合租 1整租，2合租
	private String headindex;//首页序号（小于999会在首页显示）
	private String desctiption;//描述
	private int wifi;
	private int heater;
	private int television;
	private int airconditioner;
	private int refrigerator;
	private int washingmachine;
	private int elevator;
	private int accesscontrol;
	private int parkingspace;
	private int smoking;
	private int bathtub;
	private int keepingpets;
	private int paty;

	public int getIscollect() {
		return iscollect;
	}

	public void setIscollect(int iscollect) {
		this.iscollect = iscollect;
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

	public int getRenovation() {
		return renovation;
	}

	public void setRenovation(int renovation) {
		this.renovation = renovation;
	}

	public int getPricetype() {
		return pricetype;
	}

	public void setPricetype(int pricetype) {
		this.pricetype = pricetype;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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

	public String getDesctiption() {
		return desctiption;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public int getWifi() {
		return wifi;
	}

	public void setWifi(int wifi) {
		this.wifi = wifi;
	}

	public int getHeater() {
		return heater;
	}

	public void setHeater(int heater) {
		this.heater = heater;
	}

	public int getTelevision() {
		return television;
	}

	public void setTelevision(int television) {
		this.television = television;
	}

	public int getAirconditioner() {
		return airconditioner;
	}

	public void setAirconditioner(int airconditioner) {
		this.airconditioner = airconditioner;
	}

	public int getRefrigerator() {
		return refrigerator;
	}

	public void setRefrigerator(int refrigerator) {
		this.refrigerator = refrigerator;
	}

	public int getWashingmachine() {
		return washingmachine;
	}

	public void setWashingmachine(int washingmachine) {
		this.washingmachine = washingmachine;
	}

	public int getElevator() {
		return elevator;
	}

	public void setElevator(int elevator) {
		this.elevator = elevator;
	}

	public int getAccesscontrol() {
		return accesscontrol;
	}

	public void setAccesscontrol(int accesscontrol) {
		this.accesscontrol = accesscontrol;
	}

	public int getParkingspace() {
		return parkingspace;
	}

	public void setParkingspace(int parkingspace) {
		this.parkingspace = parkingspace;
	}

	public int getSmoking() {
		return smoking;
	}

	public void setSmoking(int smoking) {
		this.smoking = smoking;
	}

	public int getBathtub() {
		return bathtub;
	}

	public void setBathtub(int bathtub) {
		this.bathtub = bathtub;
	}

	public int getKeepingpets() {
		return keepingpets;
	}

	public void setKeepingpets(int keepingpets) {
		this.keepingpets = keepingpets;
	}

	public int getPaty() {
		return paty;
	}

	public void setPaty(int paty) {
		this.paty = paty;
	}
}
