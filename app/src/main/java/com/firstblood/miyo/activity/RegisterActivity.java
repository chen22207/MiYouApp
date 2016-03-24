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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
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

    private String vCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        registerSendSmsBt.setOnClickListener(v -> {
            requestSendSms();
        });

        registerSubmitBt.setOnClickListener(v -> requestRegister());

    }

    @OnClick(R.id.register_submit_bt)
    public void onClick() {
        checkDataComplete();
    }

    private void checkDataComplete() {
        if (TextUtils.isEmpty(registerUsernameEt.getText().toString())) {
            AlertMessageUtil.showAlert(this, "");
        }
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
