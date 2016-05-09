package com.firstblood.miyo.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.util.RxBus;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;

public class PublishActivity2 extends AppCompatActivity {

	@InjectView(R.id.publish_type_shi_jia_iv)
	ImageView publishTypeShiJiaIv;
	@InjectView(R.id.publish_type_shi_number_tv)
	TextView publishTypeShiNumberTv;
	@InjectView(R.id.publish_type_shi_jian_iv)
	ImageView publishTypeShiJianIv;
	@InjectView(R.id.publish_type_ting_jia_iv)
	ImageView publishTypeTingJiaIv;
	@InjectView(R.id.publish_type_ting_number_tv)
	TextView publishTypeTingNumberTv;
	@InjectView(R.id.publish_type_ting_jian_iv)
	ImageView publishTypeTingJianIv;
	@InjectView(R.id.publish_type_wei_jia_iv)
	ImageView publishTypeWeiJiaIv;
	@InjectView(R.id.publish_type_wei_number_tv)
	TextView publishTypeWeiNumberTv;
	@InjectView(R.id.publish_type_wei_jian_iv)
	ImageView publishTypeWeiJianIv;
	@InjectView(R.id.publish_type_yangtai_jia_iv)
	ImageView publishTypeYangtaiJiaIv;
	@InjectView(R.id.publish_type_yangtai_number_tv)
	TextView publishTypeYangtaiNumberTv;
	@InjectView(R.id.publish_type_yangtai_jian_iv)
	ImageView publishTypeYangtaiJianIv;
	@InjectView(R.id.publish_house_type_next_bt)
	Button publishHouseTypeNextBt;
	@InjectView(R.id.publish_type_mianji_et)
	EditText mPublishTypeMianjiEt;
	@InjectView(R.id.publish_type_jingzhuang_rb)
	RadioButton mPublishTypeJingzhuangRb;
	@InjectView(R.id.publish_type_haohua_rb)
	RadioButton mPublishTypeHaohuaRb;
	@InjectView(R.id.publish_type_zhongdeng_rb)
	RadioButton mPublishTypeZhongdengRb;
	@InjectView(R.id.publish_type_jiandan_rb)
	RadioButton mPublishTypeJiandanRb;
	@InjectView(R.id.publish_type_zhuangxiu_rg)
	RadioGroup mPublishTypeZhuangxiuRg;
	@InjectView(R.id.publish_type_east_rb)
	RadioButton mPublishTypeEastRb;
	@InjectView(R.id.publish_type_south_rb)
	RadioButton mPublishTypeSouthRb;
	@InjectView(R.id.publish_type_west_rb)
	RadioButton mPublishTypeWestRb;
	@InjectView(R.id.publish_type_north_rb)
	RadioButton mPublishTypeNorthRb;
	@InjectView(R.id.publish_type_chaoxiang_rg)
	RadioGroup mPublishTypeChaoxiangRg;

	private int shi = 1;//室
	private int ting = 1;//厅
	private int wei = 1;//卫
	private int tai = 1;//阳台

	private HashMap<String, Object> dataMap;

	private RxBus bus;
	private Subscription mSubscription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish2);
		ButterKnife.inject(this);

		dataMap = (HashMap<String, Object>) getIntent().getSerializableExtra("dataMap");

		Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_publish2)).setRight(getString(R.string.publish_next), v -> {
			Intent intent = new Intent(this, PublishActivity3.class);
			setData();
			intent.putExtra("dataMap", dataMap);
			startActivity(intent);
		});
		bus = RxBus.getInstance();
		mSubscription = bus.toObserverable(PublishActivity5.publishSucceed.class).subscribe(publishSucceed -> finish());
	}

	private void setData() {
		String mianji = mPublishTypeMianjiEt.getText().toString();
		if (!TextUtils.isEmpty(mianji)) {
			dataMap.put("size", mianji);
		}
		switch (mPublishTypeZhuangxiuRg.getCheckedRadioButtonId()) {
			case R.id.publish_type_jingzhuang_rb:
				dataMap.put("renovation", 3);
				break;
			case R.id.publish_type_haohua_rb:
				dataMap.put("renovation", 4);
				break;
			case R.id.publish_type_zhongdeng_rb:
				dataMap.put("renovation", 2);
				break;
			case R.id.publish_type_jiandan_rb:
				dataMap.put("renovation", 1);
				break;
		}
		switch (mPublishTypeChaoxiangRg.getCheckedRadioButtonId()) {
			case R.id.publish_type_east_rb:
				dataMap.put("orientation", 1);
				break;
			case R.id.publish_type_south_rb:
				dataMap.put("orientation", 2);
				break;
			case R.id.publish_type_west_rb:
				dataMap.put("orientation", 3);
				break;
			case R.id.publish_type_north_rb:
				dataMap.put("orientation", 4);
				break;
		}
		dataMap.put("specification", shi > 4 ? 4 : shi);
		StringBuilder sb = new StringBuilder();
		if (shi > 0) {
			sb.append(shi).append("室");
		}
		if (ting > 0) {
			sb.append(ting).append("厅");
		}
		if (wei > 0) {
			sb.append(wei).append("卫");
		}
		if (tai > 0) {
			sb.append(tai).append("阳台");
		}
		dataMap.put("specification_s", sb.toString());
	}

	@OnClick({R.id.publish_type_shi_jia_iv, R.id.publish_type_shi_jian_iv, R.id.publish_type_ting_jia_iv, R.id.publish_type_ting_jian_iv, R.id.publish_type_wei_jia_iv, R.id.publish_type_wei_jian_iv, R.id.publish_type_yangtai_jia_iv, R.id.publish_type_yangtai_jian_iv,})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.publish_type_shi_jia_iv:
				shi++;
				publishTypeShiNumberTv.setText(shi + "");
				break;
			case R.id.publish_type_shi_jian_iv:
				if (shi > 0) {
					shi--;
					publishTypeShiNumberTv.setText(shi + "");
				}
				break;
			case R.id.publish_type_ting_jia_iv:
				ting++;
				publishTypeTingNumberTv.setText(ting + "");
				break;
			case R.id.publish_type_ting_jian_iv:
				if (ting > 0) {
					ting--;
				}
				publishTypeTingNumberTv.setText(ting + "");
				break;
			case R.id.publish_type_wei_jia_iv:
				wei++;
				publishTypeWeiNumberTv.setText(wei + "");
				break;
			case R.id.publish_type_wei_jian_iv:
				if (wei > 0) {
					wei--;
				}
				publishTypeWeiNumberTv.setText(wei + "");
				break;
			case R.id.publish_type_yangtai_jia_iv:
				tai++;
				publishTypeYangtaiNumberTv.setText(tai + "");
				break;
			case R.id.publish_type_yangtai_jian_iv:
				if (tai > 0) {
					tai--;
				}
				publishTypeYangtaiNumberTv.setText(tai + "");
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!mSubscription.isUnsubscribed()) {
			mSubscription.unsubscribe();
		}
	}
}
