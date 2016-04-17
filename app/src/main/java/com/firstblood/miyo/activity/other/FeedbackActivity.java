package com.firstblood.miyo.activity.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.module.NoData;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedbackActivity extends AppCompatActivity {

    @InjectView(R.id.feedback_content_et)
    EditText feedbackContentEt;
    @InjectView(R.id.feedback_contact_et)
    EditText feedbackContactEt;
    @InjectView(R.id.feedback_submit_bt)
    Button feedbackSubmitBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
	    Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_feedback));
    }

    @OnClick(R.id.feedback_submit_bt)
    public void onClick() {
	    if (TextUtils.isEmpty(feedbackContentEt.getText().toString())) {
		    AlertMessageUtil.showAlert(this, "反馈信息不能为空");
		    return;
	    }
	    CommonServices services = HttpMethods.getInstance().getClassInstance(CommonServices.class);
	    services.sendFeedBack(feedbackContentEt.getText().toString(), feedbackContactEt.getText().toString())
			    .map(new HttpResultFunc<>())
			    .subscribeOn(Schedulers.io())
			    .observeOn(AndroidSchedulers.mainThread())
			    .subscribe(new ProgressSubscriber<NoData>(this, o -> AlertMessageUtil.showAlert(this, "感谢您宝贵的意见")));
    }
}
