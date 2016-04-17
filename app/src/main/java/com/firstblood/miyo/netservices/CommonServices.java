package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.NoData;
import com.firstblood.miyo.module.Token;
import com.firstblood.miyo.module.Vcode;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/3/24.
 */
public interface CommonServices {
	//获取验证码
	@FormUrlEncoded
    @POST("sendVCodeAPI.aspx")
    Observable<HttpResult<Vcode>> sendVcode(@Field("phone") String phone);

	//获取七牛的token
	@POST("getToken.aspx")
	Observable<HttpResult<Token>> getToken();

	//反馈
	@FormUrlEncoded
	@POST("sendFeedBack.aspx")
	Observable<HttpResult<NoData>> sendFeedBack(@Field("text") String text, @Field("usermsg") String userContact);
}

