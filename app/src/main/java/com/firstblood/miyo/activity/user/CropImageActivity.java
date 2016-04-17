package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstblood.miyo.R;
import com.firstblood.miyo.database.SpDictionary;
import com.firstblood.miyo.database.SpUtils;
import com.firstblood.miyo.module.User;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CropImageActivity extends AppCompatActivity {


	@InjectView(R.id.cropImageView)
	CropImageView mCropImageView;
	@InjectView(R.id.btn_back)
	ImageView mBtnBack;
	@InjectView(R.id.commit)
	Button mCommit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		ButterKnife.inject(this);
		Uri uri = getIntent().getParcelableExtra("path");

		mCropImageView.startLoad(uri, new LoadCallback() {
			@Override
			public void onSuccess() {
				Toast.makeText(CropImageActivity.this, "图片加载成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError() {
				Toast.makeText(CropImageActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
			}
		});
		mCropImageView.setOutputMaxSize(300, 300);
		mCropImageView.setCompressQuality(50);
		mCropImageView.setDebug(true);
		mCropImageView.setCompressFormat(Bitmap.CompressFormat.PNG);


	}

	@OnClick({R.id.btn_back, R.id.commit})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.commit:
				startCrop();
				break;
		}
	}

	private void startCrop() {
		KProgressHUD pd = KProgressHUD.create(this)
				.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
				.setDimAmount(0.5f)
				.setAnimationSpeed(2)
				.setCancellable(false)
				.show();
		mCropImageView.startCrop(Uri.fromFile(new File(this.getCacheDir(), "crop_" + ((User) SpUtils.getInstance().getModule(SpDictionary.SP_USER)).getUserId())), new CropCallback() {
			@Override
			public void onSuccess(Bitmap cropped) {

			}

			@Override
			public void onError() {

			}
		}, new SaveCallback() {
			@Override
			public void onSuccess(Uri outputUri) {
				pd.dismiss();
				Intent intent = new Intent();
				intent.putExtra("cropImgUri", outputUri);
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void onError() {

			}
		});

	}
}
