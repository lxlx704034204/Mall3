package com.hxqc.mall.photolibrary.util;

import android.content.Context;

import java.io.File;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class FileUtil extends com.hxqc.util.FileUtil {

	public static String getCameraPath(Context context) {
		return String.valueOf(context.getExternalCacheDir()) + File.separator +
				"Camera" + System.currentTimeMillis() + ".png";
	}

}
