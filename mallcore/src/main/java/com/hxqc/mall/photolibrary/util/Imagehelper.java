package com.hxqc.mall.photolibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.photolibrary.activity.PreviewImageActivity;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * 查看大图
 */
public class Imagehelper {

	/**
	 * 查看大图
	 *
	 * @param position
	 * @param isPreview
	 */
	public static void toPreview(Context context, int position, boolean isPreview, Bundle b) {
		Intent intent = new Intent(context,
				PreviewImageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putBoolean("isPreview", isPreview);
		if (b != null) {
			bundle.putInt("locationX", b.getInt("locationX"));
			bundle.putInt("locationY", b.getInt("locationY"));
			bundle.putInt("width", b.getInt("width"));
			bundle.putInt("height", b.getInt("height"));
		}
		intent.putExtra("bundle", bundle);
		context.startActivity(intent);
		Activity activity = (Activity) context;
		activity.overridePendingTransition(0, 0);

	}


}
