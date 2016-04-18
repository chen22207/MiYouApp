package com.firstblood.miyo.database;

import android.content.Context;
import android.util.Base64;

import com.firstblood.miyo.module.User;
import com.orhanobut.logger.Logger;

import net.grandcentrix.tray.core.ItemNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by chenshuai12619 on 2016/3/28 14:08.
 */
public class SpUtils {
	private static SpUtils instance;
	private MyModulePreference mAppPreferences;

	private SpUtils() {
	}

	public static SpUtils getInstance() {
		if (instance == null) {
			instance = new SpUtils();
			return instance;
		} else {
			return instance;
		}
	}

	public void init(Context context) {
		if (mAppPreferences == null) {
			mAppPreferences = new MyModulePreference(context);
		}
	}

	public void putString(String key, String value) {
		mAppPreferences.put(key, value);
	}

	public void getString(String key) {
		try {
			mAppPreferences.getString(key);
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void putBoolean(String key, boolean b) {
		mAppPreferences.put(key, b);
	}

	public void getBoolean(String key) {
		mAppPreferences.getBoolean(key, false);
	}

	public void putInt(String key, int i) {
		mAppPreferences.put(key, i);
	}

	public void getInt(String key) {
		mAppPreferences.getInt(key, 0);
	}

	public void putFloat(String key, float f) {
		mAppPreferences.put(key, f);
	}

	public void getFloat(String key) {
		mAppPreferences.getFloat(key, 0);
	}

	public void putLong(String key, long l) {
		mAppPreferences.put(key, l);
	}

	public void getLong(String key) {
		mAppPreferences.getLong(key, 0l);
	}

	public void putModule(String key, Object o) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			mAppPreferences.put(key, oAuth_Base64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getModule(String key) {
		Object object = null;
		try {
			String string = mAppPreferences.getString(key);
			if (string == "") {
				return null;
			}
			byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64);
			try {
				ObjectInputStream bis = new ObjectInputStream(bais);
				object = bis.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

	public void remove(String key) {
		mAppPreferences.remove(key);
	}


	/**------------以上是基本方法-------------**/

	/**
	 * ------------以下是针对某些常用参数的便捷获取方式-------------
	 **/

	public User getUser() {
		Object module = getInstance().getModule(SpDictionary.SP_USER);
		if (module != null) {
			return (User) module;
		} else {
			Logger.wtf("用户信息为空");
			return null;
		}
	}

	public String getUserId() {
		String userId = "";
		Object module = getInstance().getModule(SpDictionary.SP_USER);
		if (module != null) {
			userId = ((User) module).getUserId();
		}
		return userId;
	}
}
