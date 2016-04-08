package com.cs.networklibrary.http;

import com.cs.networklibrary.converter.MyGsonConverterFracory;
import com.cs.networklibrary.util.PropretiesUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by chenshuai12619 on 2016/3/17 16:39.
 */
public class HttpMethods {
	public static final String BASE_URL = PropretiesUtil.getServerFullPath();

	private static final int DEFAULT_TIMEOUT = PropretiesUtil.getProperty("HTTP_TIMEOUT_SECOND", PropretiesUtil.PropertyType.INT);

	private static final boolean IS_PRINT_LOG = PropretiesUtil.getProperty("IS_PRINT_LOG", PropretiesUtil.PropertyType.BOOLEAN);

	private Retrofit retrofit;

	//构造方法私有
	private HttpMethods() {
		//手动创建一个OkHttpClient并设置超时时间
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

		retrofit = new Retrofit.Builder()
				.client(builder.build())
				.addConverterFactory(MyGsonConverterFracory.create(IS_PRINT_LOG))
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
