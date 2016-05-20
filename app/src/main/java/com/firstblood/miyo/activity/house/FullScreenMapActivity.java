package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FullScreenMapActivity extends AppCompatActivity {

	@InjectView(R.id.full_screen_map)
	MapView mFullScreenMap;

	private AMap aMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_map);
		ButterKnife.inject(this);
		mFullScreenMap.onCreate(savedInstanceState);
		double latitude = getIntent().getDoubleExtra("latitude", 30.285384);
		double longitude = getIntent().getDoubleExtra("longitude", 120.154606);
		initMap(latitude, longitude);

	}

	private void initMap(double latitude, double longitude) {
		//地图
		aMap = mFullScreenMap.getMap();
		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(false);
		uiSettings.setScaleControlsEnabled(true);
		uiSettings.setScrollGesturesEnabled(true);
		uiSettings.setZoomControlsEnabled(true);
		uiSettings.setZoomGesturesEnabled(true);
		aMap.setMyLocationEnabled(true);

		//移动到指定未知
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
		LatLng target = aMap.getCameraPosition().target;
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(target);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location));
		aMap.addMarker(markerOptions);
	}
}
