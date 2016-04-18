package com.firstblood.miyo.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.BuildConfig;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Vcode;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PasswordModifyActivity extends AppCompatActivity {

	@InjectView(R.id.pwd_modify_phone_et)
	EditText mPwdModifyPhoneEt;
	@InjectView(R.id.pwd_modify_send_sms_bt)
	Button mPwdModifySendSmsBt;
	@InjectView(R.id.pwd_modify_sms_et)
	EditText mPwdModifySmsEt;
	@InjectView(R.id.pwd_modify_new_pwd_et)
	EditText mPwdModifyNewPwdEt;

	private String vCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_modify);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setBack().setTitle("修改密码");
		mPwdModifyPhoneEt.setText(SpUtils.getInstance().getUser().getPhone());
	}

	@OnClick({R.id.pwd_modify_send_sms_bt, R.id.pwd_modify_submit_bt})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pwd_modify_send_sms_bt:
				requestSendSms();
				break;
			case R.id.pwd_modify_submit_bt:
				if (checkDataComplete()) {
					requestPwdModify();
				}
				break;
		}
	}

	private void requestSendSms() {
		if (!mPwdModifyPhoneEt.getText().toString().isEmpty() && mPwdModifyPhoneEt.getText().toString().length() == 11) {
			startTimer();
			CommonServices commonServices = HttpMethods.getInstance().getClassInstance(CommonServices.class);
			commonServices.sendVcode(mPwdModifyPhoneEt.getText().toString())
					.map(new HttpResultFunc<>())
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new ProgressSubscriber<>(this, o -> {
						vCode = ((Vcode) o).getvCode();
						//TODO 仅测试使用
						if (BuildConfig.DEBUG) {
							mPwdModifySmsEt.setText(vCode);
						}
					}));
		} else {
			mPwdModifyPhoneEt.setError("手机号位数不正确");
		}
	}

	private void startTimer() {
		mPwdModifySendSmsBt.setTextColor(getResources().getColor(R.color.gray));
		mPwdModifySendSmsBt.setText(String.format("获取验证码（%s）", "60"));
		mPwdModifySendSmsBt.setEnabled(false);
		Observable.interval(1, TimeUnit.SECONDS).take(59)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Long>() {
					@Override
					public void onCompleted() {
						mPwdModifySendSmsBt.setText("获取验证码");
						mPwdModifySendSmsBt.setTextColor(getResources().getColor(R.color.colorPrimary));
						mPwdModifySendSmsBt.setEnabled(true);
					}

					@Override
					public void onError(Throwable e) {
					}

					@Override
					public void onNext(Long aLong) {
						mPwdModifySendSmsBt.setText(String.format("获取验证码（%s）", (59l - aLong) + ""));
					}
				});
	}

	private boolean checkDataComplete() {
		if (!mPwdModifySmsEt.getText().toString().equals(vCode)) {
			AlertMessageUtil.showAlert(this, "验证码不正确");
			return false;
		}
		if (mPwdModifyNewPwdEt.getText().toString().length() < 8) {
			AlertMessageUtil.showAlert(this, "密码不能少于8位");
			return false;
		}
		return true;
	}

	private void requestPwdModify() {
		UserServices services = HttpMethods.getInstance().getClassInstance(UserServices.class);
		services.updatePwd(mPwdModifyPhoneEt.getText().toString(), mPwdModifyNewPwdEt.getText().toString())
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, o -> {
					AlertMessageUtil.showAlert(this, "密码修改成功");
					finish();
				}));
	}
}
