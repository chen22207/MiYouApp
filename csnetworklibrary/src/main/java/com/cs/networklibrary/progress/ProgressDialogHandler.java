package com.cs.networklibrary.progress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenshuai12619 on 2016/3/17 16:49.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private KProgressHUD pd;

	private Context mContext;
	private boolean cancelable;
	private ProgressCancelListener mProgressCancelListener;

	public ProgressDialogHandler(Context context, ProgressCancelListener progressCancelListener, boolean cancelable) {
		super();
		this.mContext = context;
		this.mProgressCancelListener = progressCancelListener;
		this.cancelable = cancelable;
	}

	private void initProgressDialog() {
		if (pd == null) {
            pd = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setDimAmount(0.5f)
                    .setAnimationSpeed(2)
                    .setCancellable(cancelable);

			if (cancelable) {
				Class temp = pd.getClass();
				try {
					Field field = temp.getDeclaredField("mProgressDialog");
					field.setAccessible(true);
					Object obj = field.get(pd);
					Method m = obj.getClass().getDeclaredMethod("setOnCancelListener", DialogInterface.OnCancelListener.class);
					m.invoke(obj, new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							if (mProgressCancelListener != null) {
								mProgressCancelListener.onCancelProgress();
							}
						}
					});
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			if (!pd.isShowing()) {
				pd.show();
			}
		}
	}

	private void dismissProgressDialog() {
		if (pd != null) {
			pd.dismiss();
			pd = null;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case SHOW_PROGRESS_DIALOG:
				initProgressDialog();
				break;
			case DISMISS_PROGRESS_DIALOG:
				dismissProgressDialog();
				break;
		}
	}
}
