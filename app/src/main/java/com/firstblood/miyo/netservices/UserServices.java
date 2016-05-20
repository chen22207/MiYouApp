package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.CollectionModule;
import com.firstblood.miyo.module.MyPublishModule;
import com.firstblood.miyo.module.NoData;
import com.firstblood.miyo.module.User;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/3/18.
 */
public interface UserServices {

	//登陆
	@FormUrlEncoded
    @POST("loginAPI.aspx")
    Observable<HttpResult<User>> userLogin(@Field("account") String account, @Field("password") String password);

	//注册
	@FormUrlEncoded
    @POST("registerAPI.aspx")
    Observable<HttpResult<User>> register(@Field("account") String account, @Field("nickname") String nickname, @Field("password") String password);

	//用户信息
	@FormUrlEncoded
	@POST("getUserInfo.aspx")
	Observable<HttpResult<User>> getUserInfo(@Field("userid") String userId);

	//更新用户信息
	@FormUrlEncoded
	@POST("setUserInfoAPI.aspx")
	Observable<HttpResult<NoData>> updateUserInfo(@FieldMap Map<String, Object> map);

	//更新密码
	@FormUrlEncoded
	@POST("updatePwd.aspx")
	Observable<HttpResult<NoData>> updatePwd(@Field("account") String account, @Field("newpassword") String newPwd);

	//获取我的发布
	@FormUrlEncoded
	@POST("getMyReleaseAPI.aspx")
	Observable<HttpResult<MyPublishModule>> getMyPublishList(@Field("userid") String userid, @Field("index") int index, @Field("count") int count);

	@FormUrlEncoded
	@POST("getMyCollectAPI.aspx")
	Observable<HttpResult<CollectionModule>> getMyCollection(@Field("userid") String userid, @Field("index") int index, @Field("count") int count);

}
