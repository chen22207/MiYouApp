package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.NoData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface UserServices {
    @GET("loginAPI.aspx")
    Observable<HttpResult<NoData>> userLogin(@Query("account") String account, @Query("password") String pwd);
}
