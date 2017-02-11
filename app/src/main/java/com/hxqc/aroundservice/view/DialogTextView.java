package com.hxqc.aroundservice.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Author:胡仲俊
 * Date: 2016 - 06 - 20
 * FIXME
 * Todo
 */
public class DialogTextView extends TextView {

    public DialogTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showSamplePhoto(final Context context, final int resid) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10,0,10,0);
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(resid);
                new AlertDialog.Builder(context)
                        .setView(imageView)
                        .show();
            }
        });
    }
}
