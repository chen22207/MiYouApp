package com.firstblood.miyo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.firstblood.miyo.R;
import com.firstblood.miyo.util.AlertMessageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnCameraChangeListener {

	@InjectView(R.id.map_view)
	MapView mMapView;

	private AMapLocationClient locationClient;
	private AMapLocationClientOption locationClientOption;
	private OnLocationChangedListener locationChangedListener;
	private Marker centerMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ButterKnife.inject(this);
		//地图
		mMapView.onCreate(savedInstanceState);
		AMap map = mMapView.getMap();
		map.setLocationSource(this);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setScaleControlsEnabled(true);
		map.setMyLocationEnabled(true);
		MyLocationStyle locationStyle = new MyLocationStyle();
		locationStyle.strokeColor(Color.BLACK);
		locationStyle.strokeWidth(1.0f);
		map.setMyLocationStyle(locationStyle);

		//移动到杭州
		map.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(30.285384, 120.154606)));

	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		this.locationChangedListener = onLocationChangedListener;
		if (locationClient == null) {
			//定位
			locationClient = new AMapLocationClient(this);
			locationClientOption = new AMapLocationClientOption();
			locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			locationClientOption.setNeedAddress(true);
			locationClientOption.setGpsFirst(true);
			locationClient.setLocationListener(this);
			locationClient.setLocationOption(locationClientOption);
			locationClient.startLocation();
		}
	}

	@Override
	public void deactivate() {
		locationChangedListener = null;
		if (locationClient != null) {
			locationClient.stopLocation();
			locationClient.onDestroy();
		}
		locationClient = null;

	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (locationChangedListener != null && aMapLocation != null) {
			if (aMapLocation.getErrorCode() == 0) {
				locationChangedListener.onLocationChanged(aMapLocation);
			} else {
				AlertMessageUtil.showAlert(this, "定位失败");
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != locationClient) {
			locationClient.onDestroy();
			locationClient = null;
			locationClientOption = null;
		}
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {

	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {

	}
}
