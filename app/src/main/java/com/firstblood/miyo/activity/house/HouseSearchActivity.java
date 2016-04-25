package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HouseSearchActivity extends AppCompatActivity {

	@InjectView(R.id.house_search_tb)
	Toolbar mHouseSearchTb;
	@InjectView(R.id.search_main_order_tv)
	TextView mSearchMainOrderTv;
	@InjectView(R.id.search_location_order_tv)
	TextView mSearchLocationOrderTv;
	@InjectView(R.id.search_filter_tv)
	TextView mSearchFilterTv;
	@InjectView(R.id.search_xrv)
	XRecyclerView mSearchXrv;

	private enum SearchSort {
		PUBLISH, HITS, PRICE_DESC, PRICE_ASC
	}

	private SearchSort currentSort = SearchSort.PUBLISH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_search);
		ButterKnife.inject(this);
		mHouseSearchTb.setTitle("");
		setSupportActionBar(mHouseSearchTb);
		mHouseSearchTb.setNavigationIcon(R.drawable.ic_back_white);
		mHouseSearchTb.setNavigationOnClickListener(v -> finish());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);
		MenuItem item = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
		return true;
	}


	@OnClick({R.id.search_main_order_tv, R.id.search_location_order_tv, R.id.search_filter_tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.search_main_order_tv:
				showMainOrderPopupwindow();
				break;
			case R.id.search_location_order_tv:
				break;
			case R.id.search_filter_tv:
				break;
		}
	}

	private PopupWindow sortPw;

	private void showMainOrderPopupwindow() {
		sortPw = new PopupWindow(initSortView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		sortPw.showAsDropDown(mSearchMainOrderTv);
	}

	private View initSortView() {
		View v = View.inflate(this, R.layout.popup_search_main_order, null);
		TextView publishTimeTv = (TextView) v.findViewById(R.id.search_order_publish_time_tv);
		TextView hitsTv = (TextView) v.findViewById(R.id.search_order_hits_tv);
		TextView priceDescTv = (TextView) v.findViewById(R.id.search_order_price_desc_tv);
		TextView priceAscTv = (TextView) v.findViewById(R.id.search_order_price_asc_tv);
		publishTimeTv.setOnClickListener(mMainOrderItemClickListener);
		hitsTv.setOnClickListener(mMainOrderItemClickListener);
		priceDescTv.setOnClickListener(mMainOrderItemClickListener);
		priceAscTv.setOnClickListener(mMainOrderItemClickListener);
		final int color = ContextCompat.getColor(this, R.color.colorPrimary);
		switch (currentSort) {
			case PUBLISH:
				publishTimeTv.setTextColor(color);
				break;
			case HITS:
				hitsTv.setTextColor(color);
				break;
			case PRICE_DESC:
				priceDescTv.setTextColor(color);
				break;
			case PRICE_ASC:
				priceAscTv.setTextColor(color);
				break;
		}
		return v;
	}

	private View.OnClickListener mMainOrderItemClickListener = v -> {
		switch (v.getId()) {
			case R.id.search_order_publish_time_tv:
				currentSort = SearchSort.PUBLISH;
				break;
			case R.id.search_order_hits_tv:
				currentSort = SearchSort.HITS;
				break;
			case R.id.search_order_price_desc_tv:
				currentSort = SearchSort.PRICE_DESC;
				break;
			case R.id.search_order_price_asc_tv:
				currentSort = SearchSort.PRICE_ASC;
				break;
		}
		sortPw.dismiss();
	};
}
