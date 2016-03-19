package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.NoData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface UserServices {

    @FormUrlEncoded
    @POST("loginAPI.aspx")
    Observable<HttpResult<NoData>> userLogin(@FieldMap Map<String, String> queryMap);
}
