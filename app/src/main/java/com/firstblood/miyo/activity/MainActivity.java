package com.firstblood.miyo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.user.LoginActivity;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.fragment.HomePageFragment;
import com.firstblood.miyo.fragment.MineFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	@InjectView(R.id.main_fl)
	FrameLayout mainFl;
	@InjectView(R.id.main_tab_home_ib)
	ImageButton mMainTabHomeIb;
	@InjectView(R.id.main_tab_collection_ib)
	ImageButton mMainTabCollectionIb;
	@InjectView(R.id.main_tab_search_ib)
	ImageButton mMainTabSearchIb;
	@InjectView(R.id.main_tab_message_ib)
	ImageButton mMainTabMessageIb;
	@InjectView(R.id.main_tab_mine_ib)
	ImageButton mMainTabMineIb;

	private FragmentManager fragmentManager;

	private ArrayList<Fragment> fragmentList = new ArrayList<>();
	private String[] FRAGMENT_TAG = {"首页", "收藏", "搜索", "消息", "我"};
	private String currentTag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		fragmentManager = getSupportFragmentManager();
		initEvent();
		mMainTabHomeIb.performClick();
	}

	private void initEvent() {
		mMainTabHomeIb.setOnClickListener(this);
		mMainTabCollectionIb.setOnClickListener(this);
		mMainTabSearchIb.setOnClickListener(this);
		mMainTabMessageIb.setOnClickListener(this);
		mMainTabMineIb.setOnClickListener(this);
	}

	private void hideAllFragment() {
		for (Fragment fragment : fragmentList) {
			fragmentManager.beginTransaction().hide(fragment).commit();
		}
	}

	@Override
	public void onClick(View v) {
		hideAllFragment();
		resetTabImage();
		if (v == mMainTabHomeIb) {
			if (currentTag.equals(FRAGMENT_TAG[0])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[0];
				mMainTabHomeIb.setImageResource(R.drawable.iconfont_shouye_checked);
			}
			Fragment f = fragmentManager.findFragmentByTag(FRAGMENT_TAG[0]);
			if (f != null) {
				fragmentManager.beginTransaction().show(f).commit();
			} else {
				HomePageFragment homePageFragment = HomePageFragment.newInstance();
				fragmentList.add(homePageFragment);
				fragmentManager.beginTransaction().add(R.id.main_fl, homePageFragment, FRAGMENT_TAG[0]).commit();
			}
		} else if (v == mMainTabCollectionIb) {
			if (currentTag.equals(FRAGMENT_TAG[1])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[1];
				mMainTabCollectionIb.setImageResource(R.drawable.iconfont_shoucang_checked);
			}
			Fragment f1 = fragmentManager.findFragmentByTag(FRAGMENT_TAG[1]);
			if (f1 != null) {
				fragmentManager.beginTransaction().show(f1).commit();
			} else {
				HomePageFragment homePageFragment = HomePageFragment.newInstance();
				fragmentList.add(homePageFragment);
				fragmentManager.beginTransaction().add(R.id.main_fl, homePageFragment, FRAGMENT_TAG[1]).commit();
			}
		} else if (v == mMainTabSearchIb) {
			if (currentTag.equals(FRAGMENT_TAG[2])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[2];
			}

		} else if (v == mMainTabMessageIb) {
			if (currentTag.equals(FRAGMENT_TAG[3])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[3];
				mMainTabMessageIb.setImageResource(R.drawable.iconfont_xiaoxi_checked);
			}
		} else if (v == mMainTabMineIb) {
			if (SpUtils.getInstance().getModule(SpDictionary.SP_USER) == null) {
				intentToLogin();
				return;
			}
			if (currentTag.equals(FRAGMENT_TAG[4])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[4];
				mMainTabMineIb.setImageResource(R.drawable.iconfont_wo_checked);
			}
			Fragment f4 = fragmentManager.findFragmentByTag(FRAGMENT_TAG[4]);
			if (f4 != null) {
				fragmentManager.beginTransaction().show(f4).commit();
			} else {
				MineFragment mineFragment = MineFragment.newInstance();
				fragmentList.add(mineFragment);
				fragmentManager.beginTransaction().add(R.id.main_fl, mineFragment, FRAGMENT_TAG[4]).commit();
			}
		}
	}

	private void resetTabImage() {
		mMainTabHomeIb.setImageResource(R.drawable.iconfont_shouye_normal);
		mMainTabCollectionIb.setImageResource(R.drawable.iconfont_shoucang_normal);
		mMainTabMessageIb.setImageResource(R.drawable.iconfont_xiaoxi_normal);
		mMainTabMineIb.setImageResource(R.drawable.iconfont_wo_normal);
	}

	private void intentToLogin() {
		startActivity(new Intent(this, LoginActivity.class));
	}
}
