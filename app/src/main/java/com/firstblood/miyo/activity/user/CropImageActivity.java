package com.firstblood.miyo.activity.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firstblood.miyo.R;
import com.isseiaoki.simplecropview.CropImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CropImageActivity extends AppCompatActivity {

	@InjectView(R.id.cropImageView)
	CropImageView cropImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		ButterKnife.inject(this);

	}
}
