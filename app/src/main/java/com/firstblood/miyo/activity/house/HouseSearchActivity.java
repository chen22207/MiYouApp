package com.firstblood.miyo.activity.house;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.cs.widget.viewwraper.db.LocalCityDbUtils;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.module.HouseSearch;
import com.firstblood.miyo.module.HouseSearchModule;
import com.firstblood.miyo.netservices.HouseServices;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
	@InjectView(R.id.search_no_data_rl)
	RelativeLayout mSearchNoDataRl;

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
	private String locationStr;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_search);
		ButterKnife.inject(this);
		mHouseSearchTb.setTitle("");
		setSupportActionBar(mHouseSearchTb);
		mHouseSearchTb.setNavigationIcon(R.drawable.ic_back_white);
		mHouseSearchTb.setNavigationOnClickListener(v -> finish());
		adapter = new MyAdapter(this);
		mSearchXrv.setAdapter(adapter);
		mSearchXrv.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				requestHouseSearch(Constant.TYPE_REFRESH);
			}

			@Override
			public void onLoadMore() {
				requestHouseSearch(Constant.TYPE_LOADMORE);
			}
		});
		mSearchXrv.setRefreshing(true);
	}

	private void requestHouseSearch(int type) {
		HouseServices services = HttpMethods.getInstance().getClassInstance(HouseServices.class);
		services.getHouses(getMap())
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<HouseSearchModule>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						if (type == Constant.TYPE_REFRESH) {
							mSearchXrv.refreshComplete();
						} else if (type == Constant.TYPE_LOADMORE) {
							mSearchXrv.loadMoreComplete();
						}
					}

					@Override
					public void onNext(HouseSearchModule houseSearchModule) {
						if (type == Constant.TYPE_REFRESH) {
							adapter.clearData();
							mSearchXrv.refreshComplete();
							if (houseSearchModule.getData().isEmpty()) {
								mSearchNoDataRl.setVisibility(View.VISIBLE);
							} else {
								adapter.addData(houseSearchModule.getData());
								mSearchNoDataRl.setVisibility(View.GONE);
								mSearchXrv.setLoadingMoreEnabled(true);
							}
							adapter.notifyDataSetChanged();
						} else if (type == Constant.TYPE_LOADMORE) {
							adapter.addData(houseSearchModule.getData());
							adapter.notifyDataSetChanged();
							mSearchXrv.loadMoreComplete();
							if (houseSearchModule.getData().isEmpty()) {
								AlertMessageUtil.showAlert(HouseSearchActivity.this, "没有更多了");
								mSearchXrv.setLoadingMoreEnabled(false);
							}
						}
					}
				});
	}

	private HashMap<String, String> getMap() {
		HashMap<String, String> map = new HashMap<>();
		if (locationStr != null) {
			map.put("city", locationStr);
		}

		return map;
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
				if (province.equals(city)) {
					locationStr = city + zone;
				} else {
					locationStr = province + city + zone;
				}
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
		CheckBox zhengzuCb = (CheckBox) v.findViewById(R.id.search_filter_type_zhengzu_cb);
		CheckBox hezuCb = (CheckBox) v.findViewById(R.id.search_filter_type_hezu_cb);
		CheckBox yishiCb = (CheckBox) v.findViewById(R.id.search_filter_yishi_cb);
		CheckBox liangshiCb = (CheckBox) v.findViewById(R.id.search_filter_ershi_cb);
		CheckBox sanshiCb = (CheckBox) v.findViewById(R.id.search_filter_sanshi_cb);
		CheckBox sishiCb = (CheckBox) v.findViewById(R.id.search_filter_sishi_cb);
		zhengzuCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(hezuCb);
			}
		});
		hezuCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(zhengzuCb);
			}
		});
		yishiCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(liangshiCb, sanshiCb, sishiCb);
			}
		});
		liangshiCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(yishiCb, sanshiCb, sishiCb);
			}
		});
		sanshiCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(yishiCb, liangshiCb, sishiCb);
			}
		});
		sishiCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				resetCb(yishiCb, liangshiCb, sanshiCb);
			}
		});
		Button clearBt = (Button) v.findViewById(R.id.search_filter_clear_bt);
		Button confirmBt = (Button) v.findViewById(R.id.search_filter_confirm_bt);
		clearBt.setOnClickListener(v1 -> {
			startPriceEt.setText("");
			endPriceEt.setText("");
			resetCb(zhengzuCb, hezuCb, yishiCb, liangshiCb, sanshiCb, sishiCb);
		});
		confirmBt.setOnClickListener(v2 -> {
			startPrice = Double.parseDouble(startPriceEt.getText().toString());
			endPrice = Double.parseDouble(endPriceEt.getText().toString());
			if (zhengzuCb.isChecked()) {
				currentFilterType = FilterType.ZHENGZU;
			} else if (hezuCb.isChecked()) {
				currentFilterType = FilterType.HEZU;
			} else {
				currentFilterType = FilterType.NONE;
			}
			if (yishiCb.isChecked()) {
				currentShiType = ShiType.YISHI;
			} else if (liangshiCb.isChecked()) {
				currentShiType = ShiType.LIANGSHI;
			} else if (sanshiCb.isChecked()) {
				currentShiType = ShiType.SANSHI;
			} else if (sishiCb.isChecked()) {
				currentShiType = ShiType.SISHI;
			} else {
				currentShiType = ShiType.NONE;
			}
			filterPw.dismiss();
		});
		return v;
	}

	private void resetCb(CheckBox... cbs) {
		for (CheckBox cb : cbs) {
			cb.setChecked(false);
		}
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

	private class MyAdapter extends XRecyclerView.Adapter<MyAdapter.ViewHolder> {

		private ArrayList<HouseSearch> houseSearches = new ArrayList<>();

		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		public void addData(ArrayList<HouseSearch> searches) {
			this.houseSearches.addAll(searches);
		}

		public void clearData() {
			this.houseSearches.clear();
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = View.inflate(parent.getContext(), R.layout.listitem_house_search, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			ViewHolder viewHolder = holder;
			HouseSearch houseSearch = houseSearches.get(position);
			viewHolder.title.setText(houseSearch.getTitle());
			viewHolder.type.setText(houseSearch.getIsflatshareStr());
			viewHolder.price.setText(houseSearch.getPrice());
			Picasso.with(context)
					.load(houseSearch.getIco())
					.into(viewHolder.iv);
		}

		@Override
		public int getItemCount() {
			return houseSearches.size();
		}

		class ViewHolder extends XRecyclerView.ViewHolder {
			ImageView iv;
			TextView title;
			TextView type;
			TextView price;

			public ViewHolder(View itemView) {
				super(itemView);
				iv = (ImageView) itemView.findViewById(R.id.list_item_house_search_iv);
				title = (TextView) itemView.findViewById(R.id.list_item_house_search_title_tv);
				type = (TextView) itemView.findViewById(R.id.list_item_house_search_type_tv);
				price = (TextView) itemView.findViewById(R.id.list_item_house_search_price_tv);
			}
		}
	}
}
