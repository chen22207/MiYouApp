package com.firstblood.miyo.module;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/18.
 */
public class User implements Serializable {
	@SerializedName("userid")
	private String userId;
	private String name;
    private String address;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
