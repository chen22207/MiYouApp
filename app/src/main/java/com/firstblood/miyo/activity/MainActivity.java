package com.firstblood.miyo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.firstblood.miyo.R;
import com.firstblood.miyo.fragment.HomePageFragment;

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
		if (v == mMainTabHomeIb) {
			if (currentTag.equals(FRAGMENT_TAG[0])) {
				return;
			} else {
				currentTag = FRAGMENT_TAG[0];
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

		}
	}
}
