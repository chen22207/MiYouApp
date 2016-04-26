package com.firstblood.miyo.application;

import android.app.Application;

import com.cs.networklibrary.util.PropertiesUtil;
import com.cs.widget.viewwraper.db.LocalCityDbUtils;
import com.firstblood.miyo.database.SpUtils;

/**
 * Created by Administrator on 2016/3/19.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
	    SpUtils.getInstance().init(this);
	    PropertiesUtil.init(this);
	    initProvinceCityZone();
    }

	/**
	 * 初始化省市区
	 */
	private void initProvinceCityZone() {
		LocalCityDbUtils.getInstance().initDb(this);
		new Thread(() -> {
			LocalCityDbUtils.getInstance().initCityData();
		}).start();
	}
}
