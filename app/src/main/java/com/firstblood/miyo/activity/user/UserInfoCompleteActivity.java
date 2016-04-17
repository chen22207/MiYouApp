package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.cs.networklibrary.http.HttpMethods;
import com.cs.networklibrary.http.HttpResultFunc;
import com.cs.networklibrary.util.PropertiesUtil;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.NoData;
import com.firstblood.miyo.module.Token;
import com.firstblood.miyo.module.User;
import com.firstblood.miyo.netservices.CommonServices;
import com.firstblood.miyo.netservices.UserServices;
import com.firstblood.miyo.subscribers.ProgressSubscriber;
import com.firstblood.miyo.util.CommonUtils;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.view.dialog.EditInfoDialog;
import com.isseiaoki.simplecropview.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	@InjectView(R.id.user_info_birthplace_tv)
	TextView mUserInfoBirthplaceTv;
	@InjectView(R.id.user_info_address_tv)
	TextView mUserInfoAddressTv;
	@InjectView(R.id.user_info_job_tv)
	TextView mUserInfoJobTv;
	@InjectView(R.id.user_info_accept_hezu_switch)
	Switch mUserInfoAcceptHezuSwitch;

	private ArrayList<String> defaultDataArray = new ArrayList<>();
	private boolean headImgChanged = false;
	private String imgKey;
	private User user;

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
		requestUserInfo();
		user = (User) SpUtils.getInstance().getModule(SpDictionary.SP_USER);

	}

	private void requestUserInfo() {
		UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
		userServices.getUserInfo(SpUtils.getInstance().getUserId())
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<>(this, this::initData));
	}

	private void initData(User user) {
		CommonUtils.loadHeadImage(this, user, mUserInfoHeaderIv);
		mUserInfoNicknameTv.setText(user.getName());
		mUserInfoPhoneTv.setText(user.getPhone());
		mUserInfoAgeTv.setText(user.getAge());
		mUserInfoJobTv.setText(user.getJob());
		mUserInfoUsernameTv.setText(user.getName());
		mUserInfoAcceptHezuSwitch.setChecked(user.getIsAllowShareHouse() == 1);
		mUserInfoSexTv.setText(user.getSex());
		mUserInfoAddressTv.setText(user.getLivePlace());
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
			updateInfo();
		}, new UploadOptions(null, null, false, (key, percent) -> pd.setProgress((int) (percent * 100)), null));
	}

	private void updateInfo() {
		Map<String, String> map = new HashMap<>();

		UserServices userServices = HttpMethods.getInstance().getClassInstance(UserServices.class);
		userServices.updateUserInfo(map)
				.map(new HttpResultFunc<>())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new ProgressSubscriber<NoData>(this, o -> {
				}));
	}

	@OnClick({R.id.user_info_header_tv, R.id.user_info_nickname_tv, R.id.user_info_phone_tv, R.id.user_info_QQ_tv, R.id.user_info_username_tv, R.id.user_info_sex_tv, R.id.user_info_age_tv, R.id.user_info_birthplace_tv, R.id.user_info_address_tv, R.id.user_info_job_tv})
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
				break;
			case R.id.user_info_username_tv:
				new EditInfoDialog.Builder(this, getString(R.string.user_name), "请输入真是姓名", "", mUserInfoUsernameTv::setText)
						.setMaxTextLength(8)
						.show();
				break;
			case R.id.user_info_sex_tv:
				break;
			case R.id.user_info_age_tv:
				break;
			case R.id.user_info_birthplace_tv:
				break;
			case R.id.user_info_address_tv:
				break;
			case R.id.user_info_job_tv:
				break;
		}
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
//	    if (requestCode == REQUEST_PICK_IMAGE) {
//		    if (resultCode == RESULT_OK) {
//			    Intent intent = new Intent(this, CropImageActivity.class);
//	            intent.putExtra("path", data.getData());
//	            startActivityForResult(intent, REQUEST_CROP_IMAGE);
//		    }
//	    }
	}
}
