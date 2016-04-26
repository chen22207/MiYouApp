package com.firstblood.miyo.activity.house;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cs.widget.viewwraper.db.LocalCityDbUtils;
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

	private LocalCityDbUtils.CityData c;

	private int provinceOption = 10, cityOption = 2, zoneOption = 4;//代表杭州西湖区。
	private OptionsPickerView<String> optionsPickerView;
	private SearchSort currentSort = SearchSort.PUBLISH;

	private PopupWindow sortPw;
	private PopupWindow filterPw;
	private double startPrice;
	private double endPrice;
	private FilterType currentFilterType = FilterType.NONE;
	private ShiType currentShiType = ShiType.NONE;
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
				showLocationPickWindow();
				break;
			case R.id.search_filter_tv:
				showFilterPopupwindow();
				break;
		}
	}

	private void showMainOrderPopupwindow() {
		if (sortPw != null) {
			if (sortPw.isShowing()) {
				sortPw.dismiss();
			} else {
				initTabView();
				sortPw.showAsDropDown(mSearchMainOrderTv, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
				mSearchMainOrderTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_down_blue), null);
				mSearchMainOrderTv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
			}
		} else {
			sortPw = new PopupWindow(initSortView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
			sortPw.setOnDismissListener(() -> mSearchMainOrderTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_up_blue), null));
			sortPw.setTouchable(true);
			sortPw.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
			sortPw.showAsDropDown(mSearchMainOrderTv, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
		}
	}

	private View initSortView() {
		View v = View.inflate(this, R.layout.popup_search_main_order, null);
		TextView publishTimeTv = (TextView) v.findViewById(R.id.search_order_publish_time_tv);
		TextView hitsTv = (TextView) v.findViewById(R.id.search_order_hits_tv);
		TextView priceDescTv = (TextView) v.findViewById(R.id.search_order_price_desc_tv);
		TextView priceAscTv = (TextView) v.findViewById(R.id.search_order_price_asc_tv);
		RelativeLayout outsideRl = (RelativeLayout) v.findViewById(R.id.search_order_outside_rl);
		publishTimeTv.setOnClickListener(v1 -> {
			currentSort = SearchSort.PUBLISH;
			mSearchMainOrderTv.setText(getString(R.string.popup_publish_time_order));
			sortPw.dismiss();
		});
		hitsTv.setOnClickListener(v1 -> {
			currentSort = SearchSort.HITS;
			mSearchMainOrderTv.setText(getString(R.string.popup_hits_order));
			sortPw.dismiss();
		});
		priceDescTv.setOnClickListener(v1 -> {
			currentSort = SearchSort.PRICE_DESC;
			mSearchMainOrderTv.setText(getString(R.string.popup_price_order_desc));
			sortPw.dismiss();
		});
		priceAscTv.setOnClickListener(v1 -> {
			currentSort = SearchSort.PRICE_ASC;
			mSearchMainOrderTv.setText(getString(R.string.popup_price_order_asc));
			sortPw.dismiss();
		});
		outsideRl.setOnClickListener(v1 -> sortPw.dismiss());
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

	private void showLocationPickWindow() {
		while ((c = LocalCityDbUtils.getInstance().getCityData()) == null) {

		}
		initTabView();
		mSearchLocationOrderTv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
		if (optionsPickerView == null) {
			optionsPickerView = new OptionsPickerView<>(this);
			optionsPickerView.setPicker(c.getProvince(), c.getCity(), c.getZone(), true);
			optionsPickerView.setTitle("选择地区");
			optionsPickerView.setCyclic(false, false, false);
			optionsPickerView.setSelectOptions(provinceOption, cityOption, zoneOption);
			optionsPickerView.setOnoptionsSelectListener((options1, option2, options3) -> {
				this.provinceOption = options1;
				this.cityOption = option2;
				this.zoneOption = options3;
				String province = c.getProvince().get(provinceOption);
				String city = c.getCity().get(provinceOption).get(cityOption);
				String zone = c.getZone().get(provinceOption).get(cityOption).get(zoneOption);
				mSearchLocationOrderTv.setText(zone);
				optionsPickerView.dismiss();
			});
			optionsPickerView.show();
		} else {
			optionsPickerView.setSelectOptions(provinceOption, cityOption, zoneOption);
			optionsPickerView.show();
		}
	}

	private void showFilterPopupwindow() {
		if (filterPw != null) {
			if (filterPw.isShowing()) {
				filterPw.dismiss();
			} else {
				initTabView();
				filterPw.showAsDropDown(mSearchFilterTv, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
				mSearchFilterTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_down_blue), null);
				mSearchFilterTv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
			}
		} else {
			filterPw = new PopupWindow(initFilterView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
			filterPw.setOnDismissListener(() -> mSearchFilterTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_up_blue), null));
			filterPw.setTouchable(true);
			filterPw.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));
			filterPw.showAsDropDown(mSearchFilterTv, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
		}
	}

	private View initFilterView() {
		View v = View.inflate(this, R.layout.popup_search_filter, null);
		EditText startPriceEt = (EditText) v.findViewById(R.id.search_filter_start_price_et);
		EditText endPriceEt = (EditText) v.findViewById(R.id.search_filter_end_price_et);
		RadioGroup typeRg = (RadioGroup) v.findViewById(R.id.search_filter_type_rg);
		RadioGroup shiRg = (RadioGroup) v.findViewById(R.id.search_filter_shi_rg);
		Button clearBt = (Button) v.findViewById(R.id.search_filter_clear_bt);
		Button confirmBt = (Button) v.findViewById(R.id.search_filter_confirm_bt);
		clearBt.setOnClickListener(v1 -> {
			startPriceEt.setText("");
			endPriceEt.setText("");
			typeRg.clearCheck();
			shiRg.clearCheck();
		});
		confirmBt.setOnClickListener(v2 -> {
			startPrice = Double.parseDouble(startPriceEt.getText().toString());
			endPrice = Double.parseDouble(endPriceEt.getText().toString());

			filterPw.dismiss();
		});
		return v;
	}

	private void initTabView() {
		mSearchMainOrderTv.setTextColor(ContextCompat.getColor(this, R.color.black));
		mSearchFilterTv.setTextColor(ContextCompat.getColor(this, R.color.black));
		mSearchLocationOrderTv.setTextColor(ContextCompat.getColor(this, R.color.black));
		mSearchFilterTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_up_black), null);
		mSearchMainOrderTv.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.icon_arrow_up_black), null);
	}

	@Override
	public void onBackPressed() {
		if (optionsPickerView != null && optionsPickerView.isShowing()) {
			optionsPickerView.dismiss();
		}
		super.onBackPressed();
	}

	private enum SearchSort {
		PUBLISH, HITS, PRICE_DESC, PRICE_ASC
	}

	private enum FilterType {//整租、合租、不选
		ZHENGZU, HEZU, NONE
	}

	private enum ShiType {//一室、两室、三室、四室以上、不选
		YISHI, LIANGSHI, SANSHI, SISHI, NONE
	}
}
