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
import com.firstblood.miyo.activity.other.SettingActivity;
import com.firstblood.miyo.activity.user.LoginActivity;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.fragment.HomePageFragment;
import com.firstblood.miyo.fragment.MineFragment;
import com.firstblood.miyo.util.RxBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.subscriptions.CompositeSubscription;

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
	private String intentTag = "";//未登录时，点击底部栏，“收藏”，“消息”，“我”会跳到登录界面，登录成功或注册成功后，返回此界面需定位界面。

	private RxBus bus;
	private CompositeSubscription compositeSubscription;
	private boolean loginAct = false;
	private boolean logoutAct = false;

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

		bus = RxBus.getInstance();
		compositeSubscription = new CompositeSubscription();
		compositeSubscription.add(bus.toObserverable(LoginActivity.LoginSuccess.class).subscribe(loginSuccess -> loginAct = true));
		compositeSubscription.add(bus.toObserverable(SettingActivity.LogoutAction.class).subscribe(logoutAction -> logoutAct = true));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (loginAct) {//只有登录成功或注册成功，才会执行。
			if (intentTag.equals(FRAGMENT_TAG[4])) {//未登录时点击“我”，跳转到登录界面（或注册界面），登陆成功（或注册成功）之后回来。
				mMainTabMineIb.performClick();
			}
		}
		if (logoutAct) {
			mMainTabHomeIb.performClick();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		loginAct = false;
		logoutAct = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!compositeSubscription.isUnsubscribed()) compositeSubscription.unsubscribe();
	}

	private void hideAllFragment() {
		for (Fragment fragment : fragmentList) {
			fragmentManager.beginTransaction().hide(fragment).commit();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mMainTabHomeIb) {
			if (currentTag.equals(FRAGMENT_TAG[0])) {
				return;
			} else {
				resetTabImage();
				currentTag = FRAGMENT_TAG[0];
				mMainTabHomeIb.setImageResource(R.drawable.iconfont_shouye_checked);
			}
			hideAllFragment();
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
				resetTabImage();
				currentTag = FRAGMENT_TAG[1];
				mMainTabCollectionIb.setImageResource(R.drawable.iconfont_shoucang_checked);
			}
			hideAllFragment();
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
				resetTabImage();
				currentTag = FRAGMENT_TAG[3];
				mMainTabMessageIb.setImageResource(R.drawable.iconfont_xiaoxi_checked);
			}
		} else if (v == mMainTabMineIb) {
			if (SpUtils.getInstance().getModule(SpDictionary.SP_USER) == null) {
				intentTag = FRAGMENT_TAG[4];
				intentToLogin();
				return;
			}
			if (currentTag.equals(FRAGMENT_TAG[4])) {
				return;
			} else {
				resetTabImage();
				currentTag = FRAGMENT_TAG[4];
				mMainTabMineIb.setImageResource(R.drawable.iconfont_wo_checked);
			}
			hideAllFragment();
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
