package com.cs.networklibrary.converter;

import com.cs.networklibrary.BuildConfig;
import com.google.gson.TypeAdapter;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by chenshuai12619 on 2016/4/8 13:36.
 */
final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
	private final TypeAdapter<T> mAdapter;
	private boolean isPrintResult = false;

	MyGsonResponseBodyConverter(TypeAdapter<T> adapter, boolean isPrintResult) {
		this.mAdapter = adapter;
		this.isPrintResult = isPrintResult;
	}

	@Override
	public T convert(ResponseBody value) throws IOException {
		try {
			String string = value.string();
			if (isPrintResult && BuildConfig.DEBUG) {
				Logger.json(string);
			}
			return mAdapter.fromJson(string);
		} finally {
			value.close();
		}
	}
}
