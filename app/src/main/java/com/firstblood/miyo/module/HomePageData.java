package com.firstblood.miyo.module;

import java.util.ArrayList;

/**
 * 因为首页双接口的特殊性而创建的数据类
 * Created by cs on 16/5/12.
 */
public class HomePageData {
	private ArrayList<Banner> banners;
	private HouseModule houseModule;

	public HomePageData() {
	}

	public HomePageData(ArrayList<Banner> banners, HouseModule houseModule) {
		this.banners = banners;
		this.houseModule = houseModule;
	}

	public ArrayList<Banner> getBanners() {
		return banners;
	}

	public void setBanners(ArrayList<Banner> banners) {
		this.banners = banners;
	}

	public HouseModule getHouseModule() {
		return houseModule;
	}

	public void setHouseModule(HouseModule houseModule) {
		this.houseModule = houseModule;
	}
}
