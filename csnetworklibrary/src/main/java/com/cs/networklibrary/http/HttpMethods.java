package com.cs.networklibrary.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenshuai12619 on 2016/3/17 16:39.
 */
public class HttpMethods {
    public static final String BASE_URL = "http://120.26.233.80/API/";

	private static final int DEFAULT_TIMEOUT = 10;

	private Retrofit retrofit;

	//构造方法私有
	private HttpMethods() {
		//手动创建一个OkHttpClient并设置超时时间
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

		retrofit = new Retrofit.Builder()
				.client(builder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.baseUrl(BASE_URL)
				.build();
	}

	//在访问HttpMethods时创建单例
	private static class SingletonHolder {
		private static final HttpMethods INSTANCE = new HttpMethods();
	}

	//获取单例
	public static HttpMethods getInstance() {
		return SingletonHolder.INSTANCE;
	}

    public <T> T getClassInstance(Class<T> clazz) {
        return retrofit.create(clazz);
    }


}
