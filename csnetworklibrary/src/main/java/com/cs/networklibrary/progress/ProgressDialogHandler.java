package com.cs.networklibrary.progress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.kaopiz.kprogresshud.KProgressHUD;

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
				pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mProgressCancelListener.onCancelProgress();
					}
				});
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
