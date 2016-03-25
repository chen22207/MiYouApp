package com.firstblood.miyo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.module.Vcode;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyou.R;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenshuai12619 on 2016/3/24 11:53.
 */
public class RegisterActivity extends AppCompatActivity {
    @InjectView(R.id.register_username_et)
    EditText registerUsernameEt;
    @InjectView(R.id.register_sms_et)
    EditText registerSmsEt;
    @InjectView(R.id.register_send_sms_bt)
    Button registerSendSmsBt;
    @InjectView(R.id.register_nickname_et)
    EditText registerNicknameEt;
    @InjectView(R.id.register_password_et)
    EditText registerPasswordEt;
    @InjectView(R.id.register_submit_bt)
    Button registerSubmitBt;
    @InjectView(R.id.register_user_rule_cb)
    CheckBox registerUserRuleCb;

	private String vCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_register);
	    ButterKnife.inject(this);

	    registerSendSmsBt.setOnClickListener(v -> {
		    startTimer();
		    requestSendSms();
	    });

	    registerSubmitBt.setOnClickListener(v -> {
		    if (checkDataComplete()) {
			    requestRegister();
		    }
	    });

    }

	private boolean checkDataComplete() {
		boolean b = true;
		if (TextUtils.isEmpty(registerUsernameEt.getText().toString())) {
	        AlertMessageUtil.showAlert(this, "用户名不能为空");
	        b = false;
		}
		if (TextUtils.isEmpty(registerSmsEt.getText().toString())) {
			AlertMessageUtil.showAlert(this, "验证码不能为空");
			b = false;
		}
		if (!vCode.isEmpty() && !registerSmsEt.getText().toString().equals(vCode)) {
			AlertMessageUtil.showAlert(this, "验证码不正确");
			b = false;
		}
		if (TextUtils.isEmpty(registerNicknameEt.getText().toString())) {
			AlertMessageUtil.showAlert(this, "昵称不能为空");
			b = false;
		}
		if (TextUtils.isEmpty(registerPasswordEt.getText().toString()) || registerPasswordEt.getText().toString().length() < 6) {
			AlertMessageUtil.showAlert(this, "密码不能少于6位");
			b = false;
		}
		return b;
	}

	private void startTimer() {
		registerSendSmsBt.setTextColor(getResources().getColor(R.color.gray));
		registerSendSmsBt.setText(String.format("获取验证码（%s）", "60"));
		registerSendSmsBt.setEnabled(false);
		Observable.interval(1, TimeUnit.SECONDS).take(59)
				.subscribe(new Observer<Long>() {
					@Override
					public void onCompleted() {
						registerSendSmsBt.setTextColor(getResources().getColor(R.color.colorPrimary));
						registerSendSmsBt.setEnabled(true);
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(Long aLong) {
						registerSendSmsBt.setText(String.format("获取验证码（%s）", (59l - aLong) + ""));
					}
				});
	}

    private void requestSendSms() {
        if (Patterns.PHONE.matcher(registerUsernameEt.getText().toString()).matches()) {
            CommonServices commonServices = HttpMethods.getInstance().getClassInstance(CommonServices.class);
            commonServices.sendVcode(registerUsernameEt.getText().toString())
                    .map(new HttpResultFunc<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<>(o -> vCode = ((Vcode) o).getvCode(), this));
        }
    }

    private void requestRegister() {
        UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
        userServices.register(registerUsernameEt.getText().toString(), registerNicknameEt.getText().toString(), registerPasswordEt.getText().toString())
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(o -> {
                    AlertMessageUtil.showAlert(this, "注册成功");
                }, this));
    }
}
