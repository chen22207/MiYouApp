package com.cs.networklibrary.http;

import com.cs.networklibrary.util.PropertiesUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chenshuai12619 on 2016/3/17 16:39.
 */
public class HttpMethods {
	public static final String BASE_URL = PropertiesUtil.getServerFullPath();

	private static final int DEFAULT_TIMEOUT = PropertiesUtil.getProperty("HTTP_TIMEOUT_SECOND", PropertiesUtil.PropertyType.INT);

	private static final boolean IS_PRINT_LOG = PropertiesUtil.getProperty("IS_PRINT_LOG", PropertiesUtil.PropertyType.BOOLEAN);

	private Retrofit retrofit;

	//构造方法私有
	private HttpMethods() {
		//手动创建一个OkHttpClient并设置超时时间
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

		if (IS_PRINT_LOG) {
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			builder.addInterceptor(interceptor);
		}

		retrofit = new Retrofit.Builder()
				.client(builder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.baseUrl(BASE_URL)
				.build();
	}

	//获取单例
	public static HttpMethods getInstance() {
		return SingletonHolder.INSTANCE;
	}

    public <T> T getClassInstance(Class<T> clazz) {
        return retrofit.create(clazz);
    }

	//在访问HttpMethods时创建单例
	private static class SingletonHolder {
		private static final HttpMethods INSTANCE = new HttpMethods();
	}


}
