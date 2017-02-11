package com.hxqc.xiaoneng;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created 胡俊杰
 * 2016/10/10.
 * Todo:  投诉按钮
 */

public class ComplainView extends ImageView {
	public ComplainView(Context context) {
		super(context);
	}

	public ComplainView(final Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClassName(context, "com.hxqc.mall.activity.me.ComplaintsActivity2");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
	}
}
