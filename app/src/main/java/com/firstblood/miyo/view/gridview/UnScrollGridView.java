package com.firstblood.miyo.view.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by cs on 16/5/6.
 */
public class UnScrollGridView extends GridView {
	public UnScrollGridView(Context context) {
		super(context);
	}

	public UnScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UnScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
