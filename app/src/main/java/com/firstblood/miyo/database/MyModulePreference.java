package com.firstblood.miyo.database;

import android.content.Context;

import net.grandcentrix.tray.TrayPreferences;

/**
 * Created by chenshuai12619 on 2016/3/30 14:31.
 */
public class MyModulePreference extends TrayPreferences {

	public MyModulePreference(Context context) {
		this(context, "MiYoDataBase", 1);
	}

	public MyModulePreference(Context context, String module, int version) {
		super(context, module, version);
	}


}
