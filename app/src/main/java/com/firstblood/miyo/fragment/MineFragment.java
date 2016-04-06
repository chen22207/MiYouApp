package com.firstblood.miyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.user.UserInfoCompleteActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chenshuai12619 on 2016/4/5 10:01.
 */
public class MineFragment extends Fragment {

	public static MineFragment newInstance() {
		return new MineFragment();
	}

	@InjectView(R.id.mine_header_iv)
	CircleImageView mMineHeaderIv;
	@InjectView(R.id.mine_username_tv)
	TextView mMineUsernameTv;
	@InjectView(R.id.mine_edit_info_tv)
	TextView mMineEditInfoTv;
	@InjectView(R.id.mine_publish_tv)
	TextView mMinePublishTv;
	@InjectView(R.id.mine_setting_tv)
	TextView mMineSettingTv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mine, container, false);
		ButterKnife.inject(this, v);
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@OnClick({R.id.mine_header_iv, R.id.mine_username_tv, R.id.mine_edit_info_tv, R.id.mine_publish_tv, R.id.mine_setting_tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.mine_header_iv:
				break;
			case R.id.mine_username_tv:
				break;
			case R.id.mine_edit_info_tv:
				getActivity().startActivity(new Intent(getActivity(), UserInfoCompleteActivity.class));
				break;
			case R.id.mine_publish_tv:
				break;
			case R.id.mine_setting_tv:
				break;
		}
	}
}
