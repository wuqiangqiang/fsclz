package com.zrodo.fsclz.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by grandry.xu on 15-10-28.
 */
public class ActivityUtil {

    /**
     * 短时间toast
     */
    public static void shortToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间toast
     */
    public static void longToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * toast显示持续时间
     */
    public static enum  MsgDuration{
        SHORT,LONG
    }
}
