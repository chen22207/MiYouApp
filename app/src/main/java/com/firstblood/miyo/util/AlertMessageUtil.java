package com.firstblood.miyo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/3/23.
 */
public class AlertMessageUtil {
    public static void showAlert(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
