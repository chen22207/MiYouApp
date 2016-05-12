package com.firstblood.miyo.view.convenientbanner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.firstblood.miyo.R;
import com.firstblood.miyo.module.Banner;
import com.firstblood.miyo.util.CommonUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by cs on 16/5/12.
 */
public class LocalImageHolderView implements Holder<Banner> {
	private ImageView imageView;

	@Override
	public View createView(Context context) {
		imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return imageView;
	}

	@Override
	public void UpdateUI(Context context, int position, Banner banner) {
		Picasso.with(context)
				.load(CommonUtils.getQiNiuImgUrl(banner.getUrl(), "?imageView2/2/w/720"))
				.placeholder(R.drawable.img_default)
				.error(R.drawable.img_default)
				.into(imageView);
	}
}
