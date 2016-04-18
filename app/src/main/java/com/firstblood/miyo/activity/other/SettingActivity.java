package com.firstblood.miyo.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.user.PasswordModifyActivity;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

	RxBus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_setting));
		bus = RxBus.getInstance();
	}

	@OnClick({R.id.setting_feedback_tv, R.id.setting_change_pwd_tv, R.id.setting_logout_bt})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.setting_feedback_tv:
				startActivity(new Intent(this, FeedbackActivity.class));
				break;
			case R.id.setting_change_pwd_tv:
				startActivity(new Intent(this, PasswordModifyActivity.class));
				break;
			case R.id.setting_logout_bt:
				logout();
				break;
		}
	}

	private void logout() {
		new AlertDialog.Builder(this)
				.setTitle("确定要退出？")
				.setNegativeButton("退出", (dialog, which) -> {
					SpUtils.getInstance().remove(SpDictionary.SP_USER);
					bus.send(new LogoutAction());
					finish();
				})
				.setPositiveButton("取消", (dialog, which) -> dialog.dismiss())
				.show();
	}

	public static class LogoutAction {
	}
}
