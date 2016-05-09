package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.firstblood.miyo.R;
import com.firstblood.miyo.activity.MapActivity;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.Navigation;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserRentalIntentActivity extends AppCompatActivity {

	private static final int REQUEST_MAP = 1;

	@InjectView(R.id.user_rental_location_tv)
	TextView mUserRentalLocationTv;
	@InjectView(R.id.user_rental_min_price_et)
	EditText mUserRentalMinPriceEt;
	@InjectView(R.id.user_rental_max_price_et)
	EditText mUserRentalMaxPriceEt;
	@InjectView(R.id.user_rental_kuandai)
	CheckBox mUserRentalKuandai;
	@InjectView(R.id.user_rental_xiyiji)
	CheckBox mUserRentalXiyiji;
	@InjectView(R.id.user_rental_dianshi)
	CheckBox mUserRentalDianshi;
	@InjectView(R.id.user_rental_bingxiang)
	CheckBox mUserRentalBingxiang;
	@InjectView(R.id.user_rental_reshuiqi)
	CheckBox mUserRentalReshuiqi;
	@InjectView(R.id.user_rental_kongtiao)
	CheckBox mUserRentalKongtiao;
	@InjectView(R.id.user_rental_menjin)
	CheckBox mUserRentalMenjin;
	@InjectView(R.id.user_rental_dianti)
	CheckBox mUserRentalDianti;
	@InjectView(R.id.user_rental_tingche)
	CheckBox mUserRentalTingche;
	@InjectView(R.id.user_rental_yugang)
	CheckBox mUserRentalYugang;
	@InjectView(R.id.user_rental_chongwu)
	CheckBox mUserRentalChongwu;
	@InjectView(R.id.user_rental_chouyan)
	CheckBox mUserRentalChouyan;
	@InjectView(R.id.user_rental_jucan)
	CheckBox mUserRentalJucan;
	@InjectView(R.id.user_rental_jingzhuang_rb)
	RadioButton mUserRentalJingzhuangRb;
	@InjectView(R.id.user_rental_haohua_rb)
	RadioButton mUserRentalHaohuaRb;
	@InjectView(R.id.user_rental_zhongdeng_rb)
	RadioButton mUserRentalZhongdengRb;
	@InjectView(R.id.user_rental_jiandan_rb)
	RadioButton mUserRentalJiandanRb;
	@InjectView(R.id.user_rental_zhuangxiu_rg)
	RadioGroup mUserRentalZhuangxiuRg;

	private HashMap<String, Object> dataMap;

	private double address_x;
	private double address_y;
	private String hopeAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_rental_intent);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setBack().setTitle(getString(R.string.title_rental_intent)).setRight("保存", v -> updateInfo());
		dataMap = (HashMap<String, Object>) getIntent().getSerializableExtra("dataMap");
		initViewData();
	}

	private void initViewData() {
		String hopeaddress = (String) dataMap.get("hopeaddress");
		mUserRentalLocationTv.setText(TextUtils.isEmpty(hopeaddress) ? "请选择详细地址" : hopeaddress);

		mUserRentalKuandai.setChecked((int) dataMap.get("wifi") == 1);
		mUserRentalReshuiqi.setChecked((int) dataMap.get("heater") == 1);
		mUserRentalDianshi.setChecked((int) dataMap.get("television") == 1);
		mUserRentalKongtiao.setChecked((int) dataMap.get("airconditioner") == 1);
		mUserRentalBingxiang.setChecked((int) dataMap.get("refrigerator") == 1);
		mUserRentalXiyiji.setChecked((int) dataMap.get("washingmachine") == 1);
		mUserRentalDianti.setChecked((int) dataMap.get("elevator") == 1);
		mUserRentalMenjin.setChecked((int) dataMap.get("accesscontrol") == 1);
		mUserRentalTingche.setChecked((int) dataMap.get("parkingspace") == 1);
		mUserRentalChouyan.setChecked((int) dataMap.get("smoking") == 1);
		mUserRentalYugang.setChecked((int) dataMap.get("bathtub") == 1);
		mUserRentalChongwu.setChecked((int) dataMap.get("keepingpets") == 1);
		mUserRentalJucan.setChecked((int) dataMap.get("paty") == 1);

		switch ((int) dataMap.get("hoperenovation")) {
			case 1:
				mUserRentalZhuangxiuRg.check(R.id.user_rental_jiandan_rb);
				break;
			case 2:
				mUserRentalZhuangxiuRg.check(R.id.user_rental_zhongdeng_rb);
				break;
			case 3:
				mUserRentalZhuangxiuRg.check(R.id.user_rental_jingzhuang_rb);
				break;
			case 4:
				mUserRentalZhuangxiuRg.check(R.id.user_rental_haohua_rb);
				break;
			default:
				mUserRentalZhuangxiuRg.check(R.id.user_rental_jiandan_rb);
				break;
		}
	}

	private void updateInfo() {
		initData();
		UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
		userServices.updateUserInfo(dataMap)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, o -> {
					Logger.d("保存成功");
					AlertMessageUtil.showAlert(UserRentalIntentActivity.this, "保存成功");
					Intent intent = new Intent();
					intent.putExtra("dataMap", dataMap);
					setResult(RESULT_OK, intent);
					finish();
				}));
	}

	private void initData() {
		dataMap.put("address_x", address_x);
		dataMap.put("address_y", address_y);
		dataMap.put("hopeaddress", mUserRentalLocationTv.getText().toString());
		int i = 0;
		switch (mUserRentalZhuangxiuRg.getCheckedRadioButtonId()) {
			case R.id.user_rental_jiandan_rb:
				i = 1;
				break;
			case R.id.user_rental_zhongdeng_rb:
				i = 2;
				break;
			case R.id.user_rental_jingzhuang_rb:
				i = 3;
				break;
			case R.id.user_rental_haohua_rb:
				i = 4;
				break;
		}
		dataMap.put("hoperenovation", i);
		dataMap.put("hopepricemin", mUserRentalMinPriceEt.getText().toString());
		dataMap.put("hopepricemax", mUserRentalMaxPriceEt.getText().toString());
		dataMap.put("wifi", mUserRentalKuandai.isChecked() ? 1 : 0);
		dataMap.put("heater", mUserRentalReshuiqi.isChecked() ? 1 : 0);
		dataMap.put("television", mUserRentalDianshi.isChecked() ? 1 : 0);
		dataMap.put("airconditioner", mUserRentalKongtiao.isChecked() ? 1 : 0);
		dataMap.put("refrigerator", mUserRentalBingxiang.isChecked() ? 1 : 0);
		dataMap.put("washingmachine", mUserRentalXiyiji.isChecked() ? 1 : 0);
		dataMap.put("elevator", mUserRentalDianti.isChecked() ? 1 : 0);
		dataMap.put("accesscontrol", mUserRentalMenjin.isChecked() ? 1 : 0);
		dataMap.put("parkingspace", mUserRentalTingche.isChecked() ? 1 : 0);
		dataMap.put("smoking", mUserRentalChouyan.isChecked() ? 1 : 0);
		dataMap.put("bathtub", mUserRentalYugang.isChecked() ? 1 : 0);
		dataMap.put("keepingpets", mUserRentalChongwu.isChecked() ? 1 : 0);
		dataMap.put("paty", mUserRentalJucan.isChecked() ? 1 : 0);
	}

	@OnClick(R.id.user_rental_location_tv)
	public void onClick() {
		startActivityForResult(new Intent(this, MapActivity.class), REQUEST_MAP);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				address_x = data.getDoubleExtra("x", 0.00);
				address_y = data.getDoubleExtra("y", 0.00);
				hopeAddress = data.getStringExtra("addressName");
				mUserRentalLocationTv.setText(data.getStringExtra("addressName"));
			}
		}
	}

}
