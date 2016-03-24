package com.firstblood.miyo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyou.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.login_username_et)
    EditText loginUsernameEt;
    @InjectView(R.id.login_password_et)
    EditText loginPasswordEt;
    @InjectView(R.id.login_submit_bt)
    Button loginSubmitBt;
    @InjectView(R.id.login_forget_pwd_tv)
    TextView loginForgetPwdTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_login)).setRight(getString(R.string.title_register), v -> navigateToRegist());

        loginSubmitBt.setOnClickListener(v -> {
	        if (checkDataComplete()) {
		        requestLogin();
	        }
        });
        loginForgetPwdTv.setOnClickListener(v -> navigateToForgetPwd());
    }

    private void requestLogin() {
        UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
        userServices.userLogin(loginUsernameEt.getText().toString(), loginPasswordEt.getText().toString())
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(o -> {
                    AlertMessageUtil.showAlert(this, "dengluchenggong");
                }, this));
    }

    private void navigateToForgetPwd() {

    }

    private void navigateToRegist() {

    }

    private boolean checkDataComplete() {
        boolean b = true;
        if (loginUsernameEt.getText().toString().isEmpty()) {
            AlertMessageUtil.showAlert(this, "用户名不能为空");
            b = false;
        } else if (loginPasswordEt.getText().toString().isEmpty()) {
            AlertMessageUtil.showAlert(this, "密码不能为空");
            b = false;
        }
        return b;
    }
}