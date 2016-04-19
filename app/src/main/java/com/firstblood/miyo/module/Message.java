package com.firstblood.miyo.module;

import com.google.gson.annotations.SerializedName;

/**
 * 我的消息
 * Created by cs on 16/4/19.
 */
public class Message {
	private String id;
	private String content;
	private int type;//消息类型，1合租，2系统
	private int state;//消息状态，0未读，1已读
	private String time;//发送时间
	@SerializedName("nickname")
	private String nickName;
	@SerializedName("receiveuserid")
	private String receiveUserId;
	@SerializedName("senduserid")
	private String sendUserId;
	@SerializedName("houseid")
	private String houseId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
}
