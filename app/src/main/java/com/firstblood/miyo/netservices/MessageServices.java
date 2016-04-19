package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.Message;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cs on 16/4/19.
 */
public interface MessageServices {
	@FormUrlEncoded
	@POST("getMsgListAPI.aspx")
	Observable<HttpResult<List<Message>>> getMessageList(@Field("userid") String userId, @Field("count") int count);
}
