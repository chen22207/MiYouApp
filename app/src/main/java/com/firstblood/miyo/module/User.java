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
	private String time;//注册时间
	private String name;//真实姓名
	private int sex;//性别
	private String birthday;
	private int age;
	private String phone;
	private String QQ;
	private String weichat;
	private String email;
	@SerializedName("liveplace")
	private String livePlace;
	private String job;
	@SerializedName("isallowsharehouse ")
	private int isAllowShareHouse;

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

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
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
}
