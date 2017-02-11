package com.hxqc.mall.core.util.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.hxqc.mall.config.application.SampleApplicationContext;

/**
 * Created by zf
 */

public class ToastUtils {
    public ToastUtils() {
    }

    public static void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(SampleApplicationContext.context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void show(final String message) {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                          @Override
                          public void run() {
                              Toast toast = ToastFactory.getToast(SampleApplicationContext.context, message);
                              toast.setGravity(Gravity.CENTER, 0, 0);
                              toast.show();
                          }
                      }
                );

    }

}
