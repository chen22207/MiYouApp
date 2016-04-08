package com.cs.networklibrary.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by chenshuai12619 on 2016/4/8 13:34.
 */
public class MyGsonConverterFracory extends Converter.Factory {
	private boolean isPrintResult = false;

	public static MyGsonConverterFracory create() {
		return create(new Gson(), false);
	}

	public static MyGsonConverterFracory create(boolean isPrintResult) {
		return create(new Gson(), isPrintResult);
	}

	public static MyGsonConverterFracory create(Gson gson, boolean isPrintResult) {
		return new MyGsonConverterFracory(gson, isPrintResult);
	}

	private final Gson gson;

	private MyGsonConverterFracory(Gson gson, boolean isPrintResult) {
		if (gson == null) throw new NullPointerException("gson == null");
		this.gson = gson;
		this.isPrintResult = isPrintResult;
	}

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
	                                                        Retrofit retrofit) {
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
		return new MyGsonResponseBodyConverter<>(adapter, isPrintResult);
	}

	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type,
	                                                      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
		return new MyGsonRequestBodyConverter<>(gson, adapter);
	}
}
