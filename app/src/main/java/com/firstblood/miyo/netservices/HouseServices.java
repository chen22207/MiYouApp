package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.HouseSearchModule;
import com.firstblood.miyo.module.NoData;

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
	Observable<HttpResult<HouseSearchModule>> getHouses(@FieldMap HashMap<String, Object> map);

	@FormUrlEncoded
	@POST("releaseHouseAPI.aspx")
	Observable<HttpResult<NoData>> publishHouse(@FieldMap HashMap<String, Object> map);
}
