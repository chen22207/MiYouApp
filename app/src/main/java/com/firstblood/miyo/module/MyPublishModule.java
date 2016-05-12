package com.firstblood.miyo.module;

import java.util.ArrayList;

/**
 * Created by cs on 16/5/9.
 */
public class MyPublishModule {
	private int index;
	private int count;
	private ArrayList<MyPublish> data;

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

	public ArrayList<MyPublish> getData() {
		return data;
	}

	public void setData(ArrayList<MyPublish> data) {
		this.data = data;
	}
}
