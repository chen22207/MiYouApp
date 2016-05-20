package com.firstblood.miyo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cs.networklibrary.util.PropertiesUtil;
import com.firstblood.miyo.R;
import com.firstblood.miyo.database.Constant;
import com.firstblood.miyo.module.User;
import com.isseiaoki.simplecropview.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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
	public static void loadHeadImage(Context context, User user, ImageView imageView, ImageView bg) {
		File file = new File(context.getCacheDir(), "crop_" + user.getUserId());
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);
			if (uri != null) {
				Observable.just(uri)
						.map(uri1 -> Utils.decodeSampledBitmapFromUri(context, uri, 200))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(bitmap -> {
							imageView.setImageBitmap(bitmap);
							if (bg != null) {
								bg.setImageBitmap(BitmapUtils.getVirtualBitmap(context, bitmap));
							}
						});
			}
		} else {
			if (!TextUtils.isEmpty(user.getHeadImg())) {
				try {
					Bitmap b = Picasso.with(context)
							.load(getQiNiuImgUrl(user.getHeadImg(), Constant.IMAGE_CROP_RULE_W_200))
							.placeholder(R.drawable.icon_default_head_img)
							.tag(MultiImageSelectorFragment.TAG)
							.get();
					imageView.setImageBitmap(b);
					if (bg != null) {
						bg.setImageBitmap(BitmapUtils.getVirtualBitmap(context, b));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	/**
	 * 获取七牛图片完整路径
	 *
	 * @param imgName 储存在七牛服务器的图片名
	 * @return 完整路径
	 */
	public static String getQiNiuImgUrl(String imgName) {
		return getQiNiuImgUrl(imgName, "");
	}

	/**
	 * 获取七牛图片完整路径
	 *
	 * @param imgName  储存在七牛服务器的图片名
	 * @param cropType 图片压缩裁剪方式，详见七牛官网‘图片高级处理’文档
	 * @return 完整路径
	 */
	public static String getQiNiuImgUrl(String imgName, String cropType) {
		return "http://" + PropertiesUtil.getProperty("QINIU_URL") + "/" + imgName + cropType;
	}

	public static String getOrientation(int o) {
		if (o == 1) return "东";
		else if (o == 2) return "南";
		else if (o == 3) return "西";
		else if (o == 4) return "北";
		else return "";
	}

	public static String getZhuangXiu(int z) {
		if (z == 1) return "简单";
		else if (z == 2) return "中等";
		else if (z == 3) return "精装";
		else if (z == 4) return "豪华";
		else return "未知";
	}
}
