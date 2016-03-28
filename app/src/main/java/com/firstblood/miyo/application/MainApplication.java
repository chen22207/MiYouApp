package com.firstblood.miyo.application;

import android.app.Application;

import com.firstblood.miyo.database.SpUtils;

/**
 * Created by Administrator on 2016/3/19.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
	    SpUtils.getInstance().init(getApplicationContext());
    }
}
