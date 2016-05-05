package com.firstblood.miyo.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.firstblood.miyo.R;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, AMap.OnCameraChangeListener {

	@InjectView(R.id.map_view)
	MapView mMapView;
	@InjectView(R.id.map_back_iv)
	ImageView mMapBackIv;
	@InjectView(R.id.map_sv)
	SearchView mMapSv;
	@InjectView(R.id.map_rv)
	RecyclerView mMapRv;

	private AMapLocationClient locationClient;
	private AMapLocationClientOption locationClientOption;
	private OnLocationChangedListener locationChangedListener;
	private Marker centerMarker;
	private AMap aMap;
	private ArrayList<PoiItem> aroundPois;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ButterKnife.inject(this);
		mMapBackIv.setOnClickListener(v -> finish());
		aroundPois = new ArrayList<>();
		mMapView.onCreate(savedInstanceState);
		initMap();
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mMapRv.setLayoutManager(layoutManager);
		adapter = new MyAdapter();
		mMapRv.setAdapter(adapter);
	}

	private void initMap() {
		//地图
		aMap = mMapView.getMap();
		aMap.setLocationSource(this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.getUiSettings().setScaleControlsEnabled(true);
		aMap.setMyLocationEnabled(true);
		aMap.setOnCameraChangeListener(this);
		MyLocationStyle locationStyle = new MyLocationStyle();
		locationStyle.strokeColor(Color.BLACK);
		locationStyle.strokeWidth(1.0f);
		aMap.setMyLocationStyle(locationStyle);

		//移动到杭州
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.285384, 120.154606), 13));
		LatLng target = aMap.getCameraPosition().target;
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(target);
		markerOptions.draggable(true);
		markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location));
		centerMarker = aMap.addMarker(markerOptions);
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
				AlertMessageUtil.showAlert(this, "定位失败" + aMapLocation.getErrorInfo() + "--" + aMapLocation.getLocationDetail());
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
		centerMarker.setPosition(cameraPosition.target);
	}

	@Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
		jumpPoint(centerMarker, cameraPosition.target);
		searchAround(cameraPosition.target);
	}

	public void jumpPoint(final Marker marker, LatLng latLng) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(latLng);
		startPoint.offset(0, -50);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1200;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * latLng.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * latLng.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				aMap.invalidate();// 刷新地图
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	private void searchAround(LatLng latLng) {
		PoiSearch.Query query = new PoiSearch.Query("", "", "");
		query.setPageSize(10);
		query.setPageNum(1);
		PoiSearch search = new PoiSearch(this, query);
		search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 1000));
		search.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
			@Override
			public void onPoiSearched(PoiResult poiResult, int i) {
				if (i == 1000) {
					ArrayList<PoiItem> pois = poiResult.getPois();
					aroundPois.clear();
					aroundPois.addAll(pois);
					adapter.notifyDataSetChanged();
				} else {
					Logger.e("搜周边错误：" + i);
				}
			}

			@Override
			public void onPoiItemSearched(PoiItem poiItem, int i) {

			}
		});
		search.searchPOIAsyn();
	}

	private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = View.inflate(parent.getContext(), R.layout.listitem_map_poi, null);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			ViewHolder h = holder;
			PoiItem item = aroundPois.get(position);
			h.title.setText(item.getAdName());
			h.description.setText(item.getTitle());
		}

		@Override
		public int getItemCount() {
			return aroundPois.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			private TextView title;
			private TextView description;

			public ViewHolder(View itemView) {
				super(itemView);
				title = (TextView) itemView.findViewById(R.id.listitem_map_poi_title_tv);
				description = (TextView) itemView.findViewById(R.id.listitem_map_poi_des_tv);
			}
		}
	}
}
