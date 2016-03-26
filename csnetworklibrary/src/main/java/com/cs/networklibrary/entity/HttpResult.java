package com.cs.networklibrary.entity;

/**
 * Created by chenshuai12619 on 2016/3/17 15:10.
 */
public class HttpResult<T> {
	private String resultCode;
	private String resultMsg;
	private T data;

    public String getResultCode() {
        return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
