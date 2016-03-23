package com.firstblood.miyo.util;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firstblood.miyou.R;

/**
 * Created by Administrator on 2016/3/23.
 * 设置标题栏
 */
public class Navigation {
    private FragmentActivity mActivity;

    public Navigation() {
    }

    public Navigation(FragmentActivity activity) {
        this.mActivity = activity;
    }

    public static Navigation getInstance(FragmentActivity activity) {
        return new Navigation(activity);
    }

    public Navigation setBack() {
        mActivity.findViewById(R.id.base_header_back_iv).setOnClickListener(v -> mActivity.finish());
        return this;
    }

    public Navigation setTitle(String title) {
        ((TextView) mActivity.findViewById(R.id.base_header_title_tv)).setText(title);
        return this;
    }

    public Navigation setRight(String text, View.OnClickListener clickListener) {
        Button bt = ((Button) mActivity.findViewById(R.id.base_header_right_bt));
        bt.setText(text);
        bt.setOnClickListener(clickListener);
        return this;
    }
}
