package com.firstblood.miyo.activity.user;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firstblood.miyo.R;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CropImageActivity extends AppCompatActivity {


	@InjectView(R.id.cropImageView)
	CropImageView mCropImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		ButterKnife.inject(this);
		String path = getIntent().getStringExtra("path");

		mCropImageView.startLoad(Uri.parse(path), new LoadCallback() {
			@Override
			public void onSuccess() {
				Toast.makeText(CropImageActivity.this, "load success", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError() {
				Toast.makeText(CropImageActivity.this, "load error", Toast.LENGTH_SHORT).show();
			}
		});

	}
}
