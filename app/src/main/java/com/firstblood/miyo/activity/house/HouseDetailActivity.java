package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.HouseDetail;
import com.firstblood.miyo.module.NoData;
import com.firstblood.miyo.netservices.HouseServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.util.Navigation;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HouseDetailActivity extends AppCompatActivity {

	@InjectView(R.id.house_detail_house_pic_iv)
	ImageView mHouseDetailHousePicIv;
	@InjectView(R.id.house_detail_price_tv)
	TextView mHouseDetailPriceTv;
	@InjectView(R.id.house_detail_hits_tv)
	TextView mHouseDetailHitsTv;
	@InjectView(R.id.house_detail_info_title_tv)
	TextView mHouseDetailInfoTitleTv;
	@InjectView(R.id.house_detail_info_detail_tv)
	TextView mHouseDetailInfoDetailTv;
	@InjectView(R.id.house_detail_info_more_tv)
	TextView mHouseDetailInfoMoreTv;
	@InjectView(R.id.house_detail_fangxing_tv)
	TextView mHouseDetailFangxingTv;
	@InjectView(R.id.house_detail_mianji_tv)
	TextView mHouseDetailMianjiTv;
	@InjectView(R.id.house_detail_louceng_tv)
	TextView mHouseDetailLoucengTv;
	@InjectView(R.id.house_detail_chaoxiang_tv)
	TextView mHouseDetailChaoxiangTv;
	@InjectView(R.id.house_detail_zhuangxiu_tv)
	TextView mHouseDetailZhuangxiuTv;
	@InjectView(R.id.house_detail_fabushijian_tv)
	TextView mHouseDetailFabushijianTv;
	@InjectView(R.id.house_detail_kuandai_cb)
	CheckBox mHouseDetailKuandaiCb;
	@InjectView(R.id.house_detail_bingxiang_cb)
	CheckBox mHouseDetailBingxiangCb;
	@InjectView(R.id.house_detail_xiyiji_cb)
	CheckBox mHouseDetailXiyijiCb;
	@InjectView(R.id.house_detail_dianshi_cb)
	CheckBox mHouseDetailDianshiCb;
	@InjectView(R.id.house_detail_reshuiqi_cb)
	CheckBox mHouseDetailReshuiqiCb;
	@InjectView(R.id.house_detail_kongtiao_cb)
	CheckBox mHouseDetailKongtiaoCb;
	@InjectView(R.id.house_detail_menjin_cb)
	CheckBox mHouseDetailMenjinCb;
	@InjectView(R.id.house_detail_dianti_cb)
	CheckBox mHouseDetailDiantiCb;
	@InjectView(R.id.house_detail_tingche_cb)
	CheckBox mHouseDetailTingcheCb;
	@InjectView(R.id.house_detail_yugang_cb)
	CheckBox mHouseDetailYugangCb;
	@InjectView(R.id.house_detail_chongwu_cb)
	CheckBox mHouseDetailChongwuCb;
	@InjectView(R.id.house_detail_chouyan_cb)
	CheckBox mHouseDetailChouyanCb;
	@InjectView(R.id.house_detail_jucan_cb)
	CheckBox mHouseDetailJucanCb;
	@InjectView(R.id.house_detail_map)
	MapView mHouseDetailMap;
	@InjectView(R.id.house_detail_location_tv)
	TextView mHouseDetailLocationTv;
	@InjectView(R.id.house_detail_zhengzu_tv)
	TextView mHouseDetailZhengzuTv;
	@InjectView(R.id.house_detail_hezu_tv)
	TextView mHouseDetailHezuTv;

	private String houseId;
	private AMap aMap;
	private Navigation navigation;
	private boolean isCollect;
	private HouseServices services;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_detail);
		ButterKnife.inject(this);
		mHouseDetailMap.onCreate(savedInstanceState);
		navigation = Navigation.getInstance(this).setBack();
		houseId = getIntent().getStringExtra("houseId");
		requestHouseDetail();

	}

	private void requestHouseDetail() {
		services = HttpMethods.getInstance().getClassInstance(HouseServices.class);
		services.getHouseDetail(SpUtils.getInstance().getUserId(), houseId)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, this::initView));
	}

	private void initView(HouseDetail detail) {
		try {
			JSONArray array = new JSONArray(detail.getImage());
			Picasso.with(this)
					.load(CommonUtils.getQiNiuImgUrl(array.getString(0), Constant.IMAGE_CROP_RULE_W_720))
					.placeholder(R.drawable.img_default)
					.into(mHouseDetailHousePicIv);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mHouseDetailPriceTv.setText(detail.getPrice());
		mHouseDetailHitsTv.setText(detail.getClickcount());
		mHouseDetailInfoTitleTv.setText(detail.getTitle());
		mHouseDetailInfoDetailTv.setText(detail.getDesctiption());
		mHouseDetailFangxingTv.setText(detail.getSpecification_s());
		mHouseDetailMianjiTv.setText(detail.getSize());
		mHouseDetailChaoxiangTv.setText(CommonUtils.getOrientation(detail.getOrientation()));
		mHouseDetailZhuangxiuTv.setText(CommonUtils.getZhuangXiu(detail.getRenovation()));
		mHouseDetailFabushijianTv.setText(detail.getTitle().replace("T", " "));
		mHouseDetailKuandaiCb.setChecked(detail.getWifi() == 1);
		mHouseDetailReshuiqiCb.setChecked(detail.getHeater() == 1);
		mHouseDetailDianshiCb.setChecked(detail.getTelevision() == 1);
		mHouseDetailKongtiaoCb.setChecked(detail.getAirconditioner() == 1);
		mHouseDetailBingxiangCb.setChecked(detail.getRefrigerator() == 1);
		mHouseDetailXiyijiCb.setChecked(detail.getWashingmachine() == 1);
		mHouseDetailDiantiCb.setChecked(detail.getElevator() == 1);
		mHouseDetailMenjinCb.setChecked(detail.getAccesscontrol() == 1);
		mHouseDetailTingcheCb.setChecked(detail.getParkingspace() == 1);
		mHouseDetailChouyanCb.setChecked(detail.getSmoking() == 1);
		mHouseDetailYugangCb.setChecked(detail.getBathtub() == 1);
		mHouseDetailChongwuCb.setChecked(detail.getKeepingpets() == 1);
		mHouseDetailJucanCb.setChecked(detail.getPaty() == 1);
		mHouseDetailLocationTv.setText(detail.getAddress());

		isCollect = detail.getIscollect() == 1;
		if (isCollect) {//未收藏
			navigation.setRightDrawable(R.drawable.icon_collected);
		} else {
			navigation.setRightDrawable(R.drawable.icon_uncollect);
		}
		navigation.setRightListener(v -> {
			if (isCollect) {
				requestUnCollect();
			} else {
				requestCollect();
			}
		});
		initMap(detail.getAddress_y(), detail.getAddress_x());
	}

	private void initMap(double latitude, double longitude) {
		//地图
		aMap = mHouseDetailMap.getMap();
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		aMap.getUiSettings().setScaleControlsEnabled(false);
		aMap.getUiSettings().setScrollGesturesEnabled(false);
		aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setMyLocationEnabled(false);
		aMap.setOnMapClickListener(v -> {

		});

		//移动到指定未知
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
		LatLng target = aMap.getCameraPosition().target;
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(target);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location));
		aMap.addMarker(markerOptions);
	}


	private void requestUnCollect() {
		navigation.setRightDrawable(R.drawable.icon_uncollect);
		services.collectHouseCancel(SpUtils.getInstance().getUserId(), houseId)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<NoData>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						navigation.setRightDrawable(R.drawable.icon_collected);
						AlertMessageUtil.showAlert(HouseDetailActivity.this, "取消收藏失败");
					}

					@Override
					public void onNext(NoData noData) {
						isCollect = false;
						AlertMessageUtil.showAlert(HouseDetailActivity.this, "取消收藏成功");
					}
				});
	}

	private void requestCollect() {
		navigation.setRightDrawable(R.drawable.icon_collected);
		services.collectHouse(SpUtils.getInstance().getUserId(), houseId)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<NoData>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						navigation.setRightDrawable(R.drawable.icon_uncollect);
						AlertMessageUtil.showAlert(HouseDetailActivity.this, "收藏失败");
					}

					@Override
					public void onNext(NoData noData) {
						isCollect = true;
						AlertMessageUtil.showAlert(HouseDetailActivity.this, "收藏成功");
					}
				});
	}

	@OnClick({R.id.house_detail_info_more_tv, R.id.house_detail_zhengzu_tv, R.id.house_detail_hezu_tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.house_detail_info_more_tv:
				break;
			case R.id.house_detail_zhengzu_tv:
				break;
			case R.id.house_detail_hezu_tv:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHouseDetailMap.onDestroy();
	}
}
