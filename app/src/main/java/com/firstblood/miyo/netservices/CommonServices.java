package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.Vcode;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/3/24.
 */
public interface CommonServices {
    @FormUrlEncoded
    @POST("sendVCodeAPI.aspx")
    Observable<HttpResult<Vcode>> sendVcode(@Field("phone") String phone);
}

