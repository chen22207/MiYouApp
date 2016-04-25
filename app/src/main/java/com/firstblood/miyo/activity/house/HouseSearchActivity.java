package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
}
