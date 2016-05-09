package com.firstblood.miyo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cs.networklibrary.util.PropertiesUtil;
import com.firstblood.miyo.R;
import com.firstblood.miyo.module.User;
import com.isseiaoki.simplecropview.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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
						.load(PropertiesUtil.getProperty("QINIU_URL") + "/" + user.getHeadImg())
						.placeholder(R.drawable.icon_default_head_img)
						.tag(MultiImageSelectorFragment.TAG)
						.centerCrop()
						.into(imageView);
			} else {
				imageView.setImageResource(R.drawable.icon_default_head_img);
			}
		}
	}

	/**
	 * 打印SHA1
	 *
	 * @param context
	 * @return
	 */
	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);

			byte[] cert = info.signatures[0].toByteArray();

			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			return hexString.toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
