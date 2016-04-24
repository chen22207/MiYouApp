package com.firstblood.miyo.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.module.MessageDetail;
import com.firstblood.miyo.netservices.MessageServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessageDetailActivity extends AppCompatActivity {

    @InjectView(R.id.message_detail_content_tv)
    TextView messageDetailContentTv;

    private String msgId;

    private RxBus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.inject(this);
        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_message_detial));
        bus = RxBus.getInstance();
        msgId = getIntent().getStringExtra("msgId");
        requestDetail(msgId);
    }

    private void requestDetail(String msgId) {
        MessageServices services = HttpMethods.getInstance().getClassInstance(MessageServices.class);
        services.getMessageDetail(msgId)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(this, this::setData));
    }

    private void setData(MessageDetail detail) {
        messageDetailContentTv.setText(detail.getContent());
        bus.send(new ReadMsg(msgId));
    }

    public static class ReadMsg {
        private String msgId;

        public ReadMsg(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgId() {
            return msgId;
        }
    }
}
