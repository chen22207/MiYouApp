package com.firstblood.miyo.activity.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

	@InjectView(R.id.setting_feedback_tv)
	TextView mSettingFeedbackTv;
	@InjectView(R.id.setting_change_pwd_tv)
	TextView mSettingChangePwdTv;
	@InjectView(R.id.setting_logout_bt)
	Button mLoginSubmitBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.inject(this);
	}

	@OnClick({R.id.setting_feedback_tv, R.id.setting_change_pwd_tv, R.id.setting_logout_bt})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.setting_feedback_tv:
				break;
			case R.id.setting_change_pwd_tv:
				break;
			case R.id.setting_logout_bt:
				SpUtils.getInstance().remove(SpDictionary.SP_USER);
				break;
		}
	}
}
