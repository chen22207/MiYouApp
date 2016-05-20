package com.firstblood.miyo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by cs on 16/5/16.
 */
public class BitmapUtils {
	public static Bitmap getVirtualBitmap(Context context, Bitmap bitmap) {
		Bitmap b = bitmap.copy(bitmap.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createCubemapFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

		script.setRadius(5f);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(b);

		return b;
	}
}
