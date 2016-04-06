package com.firstblood.miyo.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.view.dialog.EditInfoDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoCompleteActivity extends AppCompatActivity {

	@InjectView(R.id.user_info_header_iv)
	CircleImageView mUserInfoHeaderIv;
	@InjectView(R.id.user_info_header_tv)
	TextView mUserInfoHeaderTv;
	@InjectView(R.id.user_info_nickname_tv)
	TextView mUserInfoNicknameTv;
	@InjectView(R.id.user_info_pwd_tv)
	TextView mUserInfoPwdTv;
	@InjectView(R.id.user_info_phone_tv)
	TextView mUserInfoPhoneTv;
	@InjectView(R.id.user_info_QQ_tv)
	TextView mUserInfoQQTv;
	@InjectView(R.id.user_info_username_tv)
	TextView mUserInfoUsernameTv;
	@InjectView(R.id.user_info_sex_tv)
	TextView mUserInfoSexTv;
	@InjectView(R.id.user_info_age_tv)
	TextView mUserInfoAgeTv;
	@InjectView(R.id.user_info_birthplace_tv)
	TextView mUserInfoBirthplaceTv;
	@InjectView(R.id.user_info_address_tv)
	TextView mUserInfoAddressTv;
	@InjectView(R.id.user_info_job_tv)
	TextView mUserInfoJobTv;
	@InjectView(R.id.user_info_submit_bt)
	Button mUserInfoSubmitBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_complete);
		ButterKnife.inject(this);

	}

	@OnClick({R.id.user_info_header_tv, R.id.user_info_nickname_tv, R.id.user_info_pwd_tv, R.id.user_info_phone_tv, R.id.user_info_QQ_tv, R.id.user_info_username_tv, R.id.user_info_sex_tv, R.id.user_info_age_tv, R.id.user_info_birthplace_tv, R.id.user_info_address_tv, R.id.user_info_job_tv, R.id.user_info_submit_bt})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.user_info_header_tv:

				break;
			case R.id.user_info_nickname_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_nickname), "请输入昵称", mUserInfoNicknameTv.getText().toString(), mUserInfoNicknameTv::setText)
						.setMaxTextLength(10)
						.show();
				break;
			case R.id.user_info_pwd_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_pwd), "请输入密码", mUserInfoPwdTv.getText().toString(), mUserInfoPwdTv::setText)
						.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
						.setMaxTextLength(10)
						.show();
				break;
			case R.id.user_info_phone_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_phone), "请输入手机号", mUserInfoPwdTv.getText().toString(), mUserInfoPwdTv::setText)
						.setInputType(InputType.TYPE_CLASS_PHONE)
						.setMaxTextLength(13)
						.show();
				break;
			case R.id.user_info_QQ_tv:
				break;
			case R.id.user_info_username_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_name), "请输入真是姓名", "", mUserInfoUsernameTv::setText)
						.setMaxTextLength(8)
						.show();
				break;
			case R.id.user_info_sex_tv:
				break;
			case R.id.user_info_age_tv:
				break;
			case R.id.user_info_birthplace_tv:
				break;
			case R.id.user_info_address_tv:
				break;
			case R.id.user_info_job_tv:
				break;
			case R.id.user_info_submit_bt:
				break;
		}
	}
}
