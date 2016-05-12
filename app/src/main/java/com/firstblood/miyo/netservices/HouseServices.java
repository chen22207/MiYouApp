package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.Banner;
import com.firstblood.miyo.module.HouseModule;
import com.firstblood.miyo.module.NoData;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cs on 16/4/27.
 */
public interface HouseServices {

	//获取房源
	@FormUrlEncoded
	@POST("getHousesAPI.aspx")
	Observable<HttpResult<HouseModule>> getHouses(@FieldMap HashMap<String, Object> map);

	//发布房源
	@FormUrlEncoded
	@POST("releaseHouseAPI.aspx")
	Observable<HttpResult<NoData>> publishHouse(@FieldMap HashMap<String, Object> map);

	//首页广告数据
	@POST("getBanner.aspx")
	Observable<HttpResult<ArrayList<Banner>>> getBanner();

	//首页房源数据
	@POST("getHeadPage.aspx")
	Observable<HttpResult<HouseModule>> getHeadPage();
}
