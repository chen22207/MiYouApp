package com.firstblood.miyo.module;

import java.util.ArrayList;

/**
 * Created by cs on 16/4/27.
 */
public class HouseModule {
	private int index;
	private int count;
	private ArrayList<House> data;

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

	public ArrayList<House> getData() {
		return data;
	}

	public void setData(ArrayList<House> data) {
		this.data = data;
	}
}
