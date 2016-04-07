package com.firstblood.miyo.view.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.firstblood.miyo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by chenshuai12619 on 2016/4/6 13:31.
 */
public class EditInfoDialog extends DialogFragment {
	@InjectView(R.id.dialog_user_info_edit_tv)
	TextView mDialogUserInfoEditTv;
	@InjectView(R.id.dialog_user_info_edit_et)
	EditText mDialogUserInfoEditEt;
	@InjectView(R.id.dialog_user_info_edit_cancel_bt)
	TextView mDialogUserInfoEditCancelBt;
	@InjectView(R.id.dialog_user_info_edit_confirm_bt)
	TextView mDialogUserInfoEditConfirmBt;

	private String text;
	private String etHint;
	private String etText;
	private int maxTextLength;
	private int mInputType = InputType.TYPE_CLASS_TEXT;
	private OnCancelClickListener mOnCancelClickListener;
	private OnConfirmClickListener mOnConfirmClickListener;

	public static class Builder {
		private static final String TAG = "EditInfoDialog";
		FragmentActivity mContext;
		String text;
		String etHint;
		String etText;
		int maxTextLength;
		int mInputType = InputType.TYPE_CLASS_TEXT;
		OnCancelClickListener mCancelClickListener;
		OnConfirmClickListener mConfirmClickListener;

		public Builder(FragmentActivity context) {
			this(context, null, null);
		}

		public Builder(FragmentActivity context, String text, String etHint) {
			this(context, text, etHint, null);
		}

		public Builder(FragmentActivity context, String text, String etHint, String etText) {
			this(context, text, etHint, etText, null);
		}

		public Builder(FragmentActivity context, String text, String etHint, String etText, OnConfirmClickListener confirmClickListener) {
			this(context, text, etHint, etText, null, confirmClickListener);
		}

		public Builder(FragmentActivity context, String text, String etHint, String etText, OnCancelClickListener cancelClickListener, OnConfirmClickListener confirmClickListener) {
			this.mContext = context;
			this.text = text;
			this.etHint = etHint;
			this.mCancelClickListener = cancelClickListener;
			this.mConfirmClickListener = confirmClickListener;
		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public Builder setEtHint(String etHint) {
			this.etHint = etHint;
			return this;
		}

		public Builder setMaxTextLength(int length) {
			this.maxTextLength = length;
			return this;
		}

		public Builder setInputType(int inputType) {
			this.mInputType = inputType;
			return this;
		}

		public void show() {
			EditInfoDialog dialog = new EditInfoDialog();
			dialog.setText(text);
			dialog.setEtHint(etHint);
			dialog.setEtText(etText);

			if (maxTextLength != 0) {
				dialog.setMaxTextLength(maxTextLength);
			}
			dialog.setInputType(mInputType);

			dialog.setOnCancelClickListener(mCancelClickListener);
			dialog.setOnConfirmClickListener(mConfirmClickListener);

			dialog.setCancelable(false);//此参数未开放出去

			dialog.show(mContext.getSupportFragmentManager(), "EditInfoDialog");
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_user_info_edit, container);
		ButterKnife.inject(this, v);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialogUserInfoEditTv.setText(text);
		mDialogUserInfoEditEt.setHint(etHint);
		mDialogUserInfoEditEt.setText(etText);
		InputFilter[] filters = {new InputFilter.LengthFilter(maxTextLength)};
		mDialogUserInfoEditEt.setFilters(filters);
//		mDialogUserInfoEditEt.setInputType(mInputType);
		return v;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@OnClick({R.id.dialog_user_info_edit_cancel_bt, R.id.dialog_user_info_edit_confirm_bt})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.dialog_user_info_edit_cancel_bt:
				if (mOnCancelClickListener != null) {
					mOnCancelClickListener.click();
				}
				this.dismiss();
				break;
			case R.id.dialog_user_info_edit_confirm_bt:
				if (mOnConfirmClickListener != null) {
					mOnConfirmClickListener.click(mDialogUserInfoEditEt.getText().toString());
				}
				this.dismiss();
				break;
		}
	}

	public void setOnCancelClickListener(OnCancelClickListener OnCancelClickListener) {
		mOnCancelClickListener = OnCancelClickListener;
	}

	public void setOnConfirmClickListener(OnConfirmClickListener OnConfirmClickListener) {
		mOnConfirmClickListener = OnConfirmClickListener;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setEtHint(String hint) {
		this.etHint = hint;
	}

	public void setEtText(String text) {
		this.etText = text;
	}

	public void setMaxTextLength(int length) {
		this.maxTextLength = length;
	}

	public void setInputType(int inputType) {
		this.mInputType = inputType;
	}

	public interface OnCancelClickListener {
		void click();
	}

	public interface OnConfirmClickListener {
		void click(String text);
	}
}
