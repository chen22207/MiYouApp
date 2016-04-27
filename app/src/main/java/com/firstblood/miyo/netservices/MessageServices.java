package com.firstblood.miyo.netservices;

import com.cs.networklibrary.entity.HttpResult;
import com.firstblood.miyo.module.MessageDetail;
import com.firstblood.miyo.module.MessageModule;
import com.firstblood.miyo.module.NoData;

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
	Observable<HttpResult<MessageModule>> getMessageList(@Field("userid") String userId, @Field("index") int index, @Field("count") int count);

    @FormUrlEncoded
    @POST("getMsgInfo.aspx")
    Observable<HttpResult<MessageDetail>> getMessageDetail(@Field("msgid") String msgId);

    @FormUrlEncoded
    @POST("deleteMsg.aspx")
    Observable<HttpResult<NoData>> deleteMessage(@Field("msgid") String msgId);
}
