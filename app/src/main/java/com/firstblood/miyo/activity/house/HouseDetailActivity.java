package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_detail);
		ButterKnife.inject(this);


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
}
