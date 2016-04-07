package com.cs.networklibrary.util;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

/**
 * 网络请求配置文件读取工具
 * Created by chenshuai12619 on 2016/4/7 10:21.
 */
public class PropretiesUtil {
	private static Properties mProperties;

	public enum PropertyType {
		INT,
		STRING,
		FLOAT,
		BOOLEAN
	}

	public static void init(Context context) {
		mProperties = new Properties();
		try {
			mProperties.load(context.getAssets().open("netconfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> T getProperty(String key) {
		return getProperty(key, PropertyType.STRING);
	}

	public static <T> T getProperty(String key, PropertyType type) {
		String value = mProperties.getProperty(key);
		switch (type) {
			case INT:
				return (T) new Integer(value);
			case STRING:
				return (T) value;
			case FLOAT:
				return (T) new Float(value);
			case BOOLEAN:
				return (T) new Boolean(value);
			default:
				return (T) value;
		}
	}

	public static String getServerFullPath() {
		String type = mProperties.getProperty("http_type");
		String url = mProperties.getProperty("server_url");
		String port = mProperties.getProperty("server_port");
		String project = mProperties.getProperty("server_project");
		return type + "://" + url + ":" + port + "/" + project + "/";
	}
}
