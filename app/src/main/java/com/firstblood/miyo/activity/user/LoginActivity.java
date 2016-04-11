package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.login_username_et)
    EditText loginUsernameEt;
    @InjectView(R.id.login_password_et)
    EditText loginPasswordEt;
    @InjectView(R.id.login_submit_bt)
    Button loginSubmitBt;
    @InjectView(R.id.login_forget_pwd_tv)
    TextView loginForgetPwdTv;

    private RxBus bus;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
	    Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_login)).setRight(getString(R.string.title_register), v -> navigateToRegister());

        bus = RxBus.getInstance();
	    compositeSubscription = new CompositeSubscription();
	    compositeSubscription.add(bus.toObserverable(LoginSuccess.class).subscribe(loginSuccess -> finish()));

        loginSubmitBt.setOnClickListener(v -> {
	        if (checkDataComplete()) {
		        requestLogin();
	        }
        });
        loginForgetPwdTv.setOnClickListener(v -> navigateToForgetPwd());
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!compositeSubscription.isUnsubscribed()) {
			compositeSubscription.unsubscribe();
		}
	}

	private void requestLogin() {
		UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
        userServices.userLogin(loginUsernameEt.getText().toString(), loginPasswordEt.getText().toString())
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
		        .subscribe(new ProgressSubscriber<>(this, o -> {
			        SpUtils.getInstance().putModule(SpDictionary.SP_USER, o);
	                AlertMessageUtil.showAlert(LoginActivity.this, "登录成功");
	                bus.send(new LoginSuccess());
		        }));
	}

    private void navigateToForgetPwd() {

    }

	private void navigateToRegister() {
		startActivity(new Intent(this, RegisterActivity.class));
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

    public static class LoginSuccess {

    }
}
