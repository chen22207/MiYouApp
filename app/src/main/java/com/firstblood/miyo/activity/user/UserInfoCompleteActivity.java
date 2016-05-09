package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.cs.networklibrary.util.PropertiesUtil;
import com.cs.widget.viewwraper.db.LocalCityDbUtils;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.Token;
import com.firstblood.miyo.module.User;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.AlertMessageUtil;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.view.dialog.EditInfoDialog;
import com.isseiaoki.simplecropview.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.orhanobut.logger.Logger;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserInfoCompleteActivity extends AppCompatActivity {

	private static final int REQUEST_PICK_IMAGE = 1001;
	private static final int REQUEST_CROP_IMAGE = 1002;
	private static final int REQUEST_IMAGE = 1003;
	private static final int REQUEST_INTENT = 1004;
	@InjectView(R.id.user_info_header_iv)
	CircleImageView mUserInfoHeaderIv;
	@InjectView(R.id.user_info_header_tv)
	TextView mUserInfoHeaderTv;
	@InjectView(R.id.user_info_nickname_tv)
	TextView mUserInfoNicknameTv;
	@InjectView(R.id.user_info_phone_tv)
	TextView mUserInfoPhoneTv;
	@InjectView(R.id.user_info_QQ_tv)
	TextView mUserInfoQQTv;
	@InjectView(R.id.user_info_username_tv)
	TextView mUserInfoUsernameTv;
	@InjectView(R.id.user_info_sex_tv)
	TextView mUserInfoSexTv;
	@InjectView(R.id.user_info_age_tv)
	TextView mUserInfoAgeTv;
	@InjectView(R.id.user_info_nativeplace_tv)
	TextView mUserInfoNativeplaceTv;
	@InjectView(R.id.user_info_address_tv)
	TextView mUserInfoAddressTv;
	@InjectView(R.id.user_info_job_tv)
	TextView mUserInfoJobTv;
	@InjectView(R.id.user_info_accept_hezu_switch)
	Switch mUserInfoAcceptHezuSwitch;
	@InjectView(R.id.user_info_accept_hezu_tv)
	TextView mUserInfoAcceptHezuTv;

	private HashMap<String, Object> dataMap;
	private ArrayList<String> defaultDataArray = new ArrayList<>();
	private boolean headImgChanged = false;
	private String imgKey;
	private User user;
	private int provinceOption = 10, cityOption = 2, zoneOption = 4;//代表杭州西湖区。
	private OptionsPickerView<String> optionsPickerView;
	private LocalCityDbUtils.CityData c;
	private String locationStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_complete);
		ButterKnife.inject(this);
		Navigation.getInstance(this).setTitle("完善信息").setBack().setRight("保存", o -> {
			if (headImgChanged) getToken();
			else {
				updateInfo();
			}
		});
		dataMap = new HashMap<>();
		requestUserInfo();

	}

	private void requestUserInfo() {
		UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
		userServices.getUserInfo(SpUtils.getInstance().getUserId())
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, this::initData));
	}

	private void initData(User u) {
		user = u;
		user.setUserId(SpUtils.getInstance().getUserId());
		CommonUtils.loadHeadImage(this, user, mUserInfoHeaderIv);
		mUserInfoNicknameTv.setText(TextUtils.isEmpty(user.getNickName()) ? "请输入" : user.getNickName());
		mUserInfoPhoneTv.setText(TextUtils.isEmpty(user.getPhone()) ? "请输入" : user.getPhone());
		mUserInfoAgeTv.setText(user.getAge() == 0 ? "请输入" : user.getAge() + "");
		mUserInfoJobTv.setText(TextUtils.isEmpty(user.getJob()) ? "请输入" : user.getJob());
		mUserInfoUsernameTv.setText(TextUtils.isEmpty(user.getName()) ? "请输入" : user.getName());
		mUserInfoAcceptHezuSwitch.setChecked(user.getIsAllowShareHouse() == 1);
		int sex = user.getSex();
		String sexStr = "";
		if (sex == 0) {
			sexStr = "请设置";
		} else if (sex == 1) {
			sexStr = "男";
		} else if (sex == 2) {
			sexStr = "女";
		}
		mUserInfoSexTv.setText(sexStr);
		mUserInfoAddressTv.setText(TextUtils.isEmpty(user.getLivePlace()) ? "请输入" : user.getLivePlace());
		mUserInfoNativeplaceTv.setText(TextUtils.isEmpty(user.getNativePlace()) ? "请输入" : user.getNativePlace());
		mUserInfoQQTv.setText(user.getQq());
	}

	private void getToken() {
		CommonServices services = HttpMethods.getInstance().getClassInstance(CommonServices.class);
		services.getToken()
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<Token>(this, this::uploadImg));
	}

	private void uploadImg(Token token) {
		imgKey = System.currentTimeMillis() + "";
		KProgressHUD pd = new KProgressHUD(this);
		pd.setStyle(KProgressHUD.Style.BAR_DETERMINATE)
				.setMaxProgress(100)
				.setLabel("上传中...")
				.setCancellable(false)
				.setProgress(0);
		pd.show();
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(new File(this.getCacheDir(), "crop_" + user.getUserId()), imgKey, token.getToken(), (key, info, response) -> {
			pd.dismiss();
			Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
			user.setHeadImg(PropertiesUtil.getProperty("QINIU_URL") + "/" + key);
			dataMap.put("headphoto", imgKey);
			updateInfo();
		}, new UploadOptions(null, null, false, (key, percent) -> pd.setProgress((int) (percent * 100)), null));
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
					AlertMessageUtil.showAlert(UserInfoCompleteActivity.this, "保存成功");
				}));
	}

	@OnClick({R.id.user_info_header_tv, R.id.user_info_nickname_tv, R.id.user_info_phone_tv, R.id.user_info_QQ_tv, R.id.user_info_username_tv, R.id.user_info_sex_tv, R.id.user_info_age_tv, R.id.user_info_nativeplace_tv, R.id.user_info_address_tv, R.id.user_info_job_tv, R.id.user_info_accept_hezu_tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.user_info_header_tv:
				headImgSelect();
				break;
			case R.id.user_info_nickname_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_nickname), "请输入昵称", mUserInfoNicknameTv.getText().toString(), mUserInfoNicknameTv::setText)
						.setMaxTextLength(10)
						.show();
				break;
			case R.id.user_info_phone_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_phone), "请输入手机号", mUserInfoPhoneTv.getText().toString(), mUserInfoPhoneTv::setText)
						.setInputType(InputType.TYPE_CLASS_PHONE)
						.setMaxTextLength(13)
						.show();
				break;
			case R.id.user_info_QQ_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_phone), "请输入QQ号", mUserInfoQQTv.getText().toString(), mUserInfoQQTv::setText)
						.setInputType(InputType.TYPE_CLASS_NUMBER)
						.setMaxTextLength(15)
						.show();
				break;
			case R.id.user_info_username_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_name), "请输入真是姓名", "", mUserInfoUsernameTv::setText)
						.setMaxTextLength(8)
						.show();
				break;
			case R.id.user_info_sex_tv:
				new AlertDialog.Builder(this)
						.setTitle("选择性别")
						.setNegativeButton("男", (dialog, which) -> {
							mUserInfoSexTv.setText("男");
						})
						.setPositiveButton("女", (dialog, which) -> {
							mUserInfoSexTv.setText("女");
						})
						.show();
				break;
			case R.id.user_info_age_tv:
				new EditInfoDialog.Builder(this, "年龄", "请输入年龄", mUserInfoAgeTv.getText().toString(), mUserInfoAgeTv::setText)
						.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED)
						.setMaxTextLength(2)
						.show();
				break;
			case R.id.user_info_nativeplace_tv:
				showLocationPickView();
				break;
			case R.id.user_info_address_tv:
				new EditInfoDialog.Builder(this, "居住地", "请输入居住地", mUserInfoAddressTv.getText().toString(), mUserInfoAddressTv::setText)
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.setMaxTextLength(20)
						.show();
				break;
			case R.id.user_info_job_tv:
				new EditInfoDialog.Builder(this, "工作", "请输入您的工作", mUserInfoJobTv.getText().toString(), mUserInfoJobTv::setText)
						.setInputType(InputType.TYPE_CLASS_TEXT)
						.setMaxTextLength(10)
						.show();
				break;
			case R.id.user_info_accept_hezu_tv:
				if (mUserInfoAcceptHezuSwitch.isChecked()) {
					Intent intent = new Intent(UserInfoCompleteActivity.this, UserRentalIntentActivity.class);
					initData();
					intent.putExtra("dataMap", dataMap);
					startActivityForResult(intent, REQUEST_INTENT);
				}
				break;
		}
	}

	private void showLocationPickView() {
		while ((c = LocalCityDbUtils.getInstance().getCityData()) == null) {

		}
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
				mUserInfoNativeplaceTv.setText(zone);
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

	private void initData() {
		dataMap.put("nickname", mUserInfoNicknameTv.getText().toString());
		dataMap.put("phone", mUserInfoPhoneTv.getText().toString());
		dataMap.put("age", mUserInfoAgeTv.getText().toString().equals("请输入") ? 0 : mUserInfoAgeTv.getText().toString());
		dataMap.put("job", mUserInfoJobTv.getText().toString());
		dataMap.put("name", mUserInfoUsernameTv.getText().toString());
		dataMap.put("isallowsharehouse", mUserInfoAcceptHezuSwitch.isChecked() ? 1 : 0);
		dataMap.put("qq", mUserInfoQQTv.getText().toString());

		String sex = mUserInfoSexTv.getText().toString();
		int s = 0;
		if (sex.equals("请设置")) {
			s = 0;
		} else if (sex.equals("男")) {
			s = 1;
		} else if (sex.equals("女")) {
			s = 2;
		}
		dataMap.put("sex", s);
		dataMap.put("liveplace", mUserInfoAddressTv.getText().toString());
		dataMap.put("nativeplace", mUserInfoNativeplaceTv.getText().toString());
		dataMap.put("userid", SpUtils.getInstance().getUserId());
		dataMap.put("wifi", user.getWifi());
		dataMap.put("heater", user.getHeater());
		dataMap.put("television", user.getTelevision());
		dataMap.put("airconditioner", user.getAirconditioner());
		dataMap.put("refrigerator", user.getRefrigerator());
		dataMap.put("washingmachine", user.getWashingmachine());
		dataMap.put("elevator", user.getElevator());
		dataMap.put("accesscontrol", user.getAccesscontrol());
		dataMap.put("parkingspace", user.getParkingspace());
		dataMap.put("smoking", user.getSmoking());
		dataMap.put("bathtub", user.getBathtub());
		dataMap.put("keepingpets", user.getKeepingpets());
		dataMap.put("paty", user.getPaty());
		dataMap.put("hopepricemin", user.getHopePriceMin());
		dataMap.put("hopepricemax", user.getHopePriceMax());
		dataMap.put("address_x", user.getAddress_x());
		dataMap.put("address_y", user.getAddress_y());
		dataMap.put("hopeaddress", TextUtils.isEmpty(user.getHopeAddress()) ? "" : user.getHopeAddress());
		dataMap.put("hoperenovation", user.getHopeRenovation());
	}

	private void headImgSelect() {
//	    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//	    dialog.setTitle("选择图片");
//	    dialog.setNegativeButton("拍照", (dialog1, which) -> {
//		    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		    startActivityForResult(cameraIntent,REQUEST_PICK_IMAGE);
//	    });
//	    dialog.setPositiveButton("从相册选", (dialog1, which) -> {
//            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK_IMAGE);
//	    });
//	    dialog.show();
		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// max select image amount
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
		// default select images (support array list)
		intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			if (resultCode == RESULT_OK) {
				// Get the result list of select image paths
				List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// do your logic ....
				Intent intent = new Intent(this, CropImageActivity.class);
				intent.putExtra("path", Uri.fromFile(new File(path.get(0))));
				startActivityForResult(intent, REQUEST_CROP_IMAGE);
			}
		}
		if (requestCode == REQUEST_CROP_IMAGE) {
			Uri uri = data.getParcelableExtra("cropImgUri");
			if (uri != null) {
				Observable.just(uri)
						.map(uri1 -> Utils.decodeSampledBitmapFromUri(this, uri, 200))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(bitmap -> {
							mUserInfoHeaderIv.setImageBitmap(bitmap);
							headImgChanged = true;
						});
			}
		}

		if (requestCode == REQUEST_INTENT) {
			if (resultCode == RESULT_OK) {
				requestUserInfo();
			}
		}
//	    if (requestCode == REQUEST_PICK_IMAGE) {
//		    if (resultCode == RESULT_OK) {
//			    Intent intent = new Intent(this, CropImageActivity.class);
//	            intent.putExtra("path", data.getData());
//	            startActivityForResult(intent, REQUEST_CROP_IMAGE);
//		    }
//	    }
	}

	@Override
	public void onBackPressed() {
		if (optionsPickerView != null && optionsPickerView.isShowing()) {
			optionsPickerView.dismiss();
			return;
		}
		super.onBackPressed();
	}
}
