package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.HouseSearchModule;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cs on 16/4/27.
 */
public interface HouseServices {
	@FormUrlEncoded
	@POST("getHousesAPI.aspx")
	Observable<HttpResult<HouseSearchModule>> getHouses(@FieldMap HashMap<String, String> map);
}
