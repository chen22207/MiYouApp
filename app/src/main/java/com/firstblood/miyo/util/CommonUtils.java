package com.firstblood.miyo.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.module.User;
import com.isseiaoki.simplecropview.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import me.nereo.multi_image_selector.MultiImageSelectorFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/21.
 */
public class CommonUtils {
	public static void loadHeadImage(Context context, User user, ImageView imageView) {
		File file = new File(context.getCacheDir(), "crop_" + user.getUserId());
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);
			if (uri != null) {
				Observable.just(uri)
						.map(uri1 -> Utils.decodeSampledBitmapFromUri(context, uri, 200))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(imageView::setImageBitmap);
			}
		} else {
			if (!TextUtils.isEmpty(user.getHeadImg())) {
				Picasso.with(context)
						.load(user.getHeadImg())
						.placeholder(R.drawable.icon_default_head_img)
						.tag(MultiImageSelectorFragment.TAG)
						.centerCrop()
						.into(imageView);
			} else {
				imageView.setImageResource(R.drawable.icon_default_head_img);
			}
		}
	}
}
