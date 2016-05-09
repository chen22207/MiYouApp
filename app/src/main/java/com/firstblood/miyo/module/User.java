package com.firstblood.miyo.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户
 * Created by Administrator on 2016/3/18.
 */
public class User implements Serializable {
	@SerializedName("userid")
	private String userId;
	@SerializedName("nickname")
	private String nickName;
	@SerializedName("headphoto")
	private String headImg;
	private String address;
	private String time;//最后修改时间
	private String name;//真实姓名
	private int sex;//性别
	private String birthday;
	private int age;
	private String phone;
	private String qq;
	private String weichat;
	private String email;
	@SerializedName("liveplace")
	private String livePlace;
	@SerializedName("nativeplace")
	private String nativePlace;
	private String job;
	@SerializedName("isallowsharehouse")
	private int isAllowShareHouse;
	@SerializedName("hopeaddress")
	private String hopeAddress;
	private double address_x;
	private double address_y;
	@SerializedName("hopepricemin")
	private double hopePriceMin;
	@SerializedName("hoppricemax")
	private double hopePriceMax;
	@SerializedName("hoperenovation")
	private int hopeRenovation;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String Qq) {
		this.qq = Qq;
	}

	public String getWeichat() {
		return weichat;
	}

	public void setWeichat(String weichat) {
		this.weichat = weichat;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLivePlace() {
		return livePlace;
	}

	public void setLivePlace(String livePlace) {
		this.livePlace = livePlace;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getIsAllowShareHouse() {
		return isAllowShareHouse;
	}

	public void setIsAllowShareHouse(int isAllowShareHouse) {
		this.isAllowShareHouse = isAllowShareHouse;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getHopeAddress() {
		return hopeAddress;
	}

	public void setHopeAddress(String hopeAddress) {
		this.hopeAddress = hopeAddress;
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

	public double getHopePriceMin() {
		return hopePriceMin;
	}

	public void setHopePriceMin(double hopePriceMin) {
		this.hopePriceMin = hopePriceMin;
	}

	public double getHopePriceMax() {
		return hopePriceMax;
	}

	public void setHopePriceMax(double hopePriceMax) {
		this.hopePriceMax = hopePriceMax;
	}

	public int getHopeRenovation() {
		return hopeRenovation;
	}

	public void setHopeRenovation(int hopeRenovation) {
		this.hopeRenovation = hopeRenovation;
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
