package com.firstblood.miyo.module;

import java.util.ArrayList;

/**
 * Created by cs on 16/4/27.
 */
public class MessageModule {
	private int index;
	private int count;
	private ArrayList<Message> data;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<Message> getData() {
		return data;
	}

	public void setData(ArrayList<Message> data) {
		this.data = data;
	}
}
