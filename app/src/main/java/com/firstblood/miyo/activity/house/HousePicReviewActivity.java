package com.firstblood.miyo.activity.house;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.util.CommonUtils;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HousePicReviewActivity extends AppCompatActivity {

	@InjectView(R.id.house_pic_review_vp)
	ViewPager mHousePicReviewVp;
	@InjectView(R.id.house_pic_review_index_tv)
	TextView mHousePicReviewIndexTv;

	private String[] pics;

	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_pic_review);
		ButterKnife.inject(this);
		pics = getIntent().getStringArrayExtra("pics");
		adapter = new MyAdapter();
		mHousePicReviewVp.setAdapter(adapter);
		mHousePicReviewVp.addOnPageChangeListener(onPageChangeListener);
		mHousePicReviewVp.setOffscreenPageLimit(9);
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pics.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(container.getContext());
			iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Picasso.with(container.getContext())
					.load(CommonUtils.getQiNiuImgUrl(pics[position], Constant.IMAGE_CROP_RULE_W_720))
					.into(iv);
			container.addView(iv);
			return iv;
		}
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pics.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = new ImageView(container.getContext());
			iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			Picasso.with(container.getContext())
					.load(CommonUtils.getQiNiuImgUrl(pics[position], Constant.IMAGE_CROP_RULE_W_720))
					.into(iv);
			container.addView(iv);
			return iv;
		}
	}

	private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			mHousePicReviewIndexTv.setText((position + 1) + "/" + pics.length);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};
}
