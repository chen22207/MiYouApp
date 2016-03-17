package com.cs.networklibrary.http;

import com.cs.networklibrary.entity.HttpResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

/**
 * Created by chenshuai12619 on 2016/3/17 16:39.
 */
public class HttpMethods {
	public static final String BASE_URL = "https://api.douban.com/v2/movie/";

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

	/**
	 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
	 *
	 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
	 */
	private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

		@Override
		public T call(HttpResult<T> httpResult) {
			if (!httpResult.getResultCode().equals("0000")) {
				throw new ApiException(100);
			}
			return httpResult.getData();
		}
	}
}
