package com.hxqc.mall.core.util.utils;

import android.content.Context;
import android.widget.Toast;

/**
  保持全局只有一个Toast实例
 * 
 */
public class ToastFactory {
	private static Context context = null;
	private static Toast toast = null;

	public static Toast getToast(Context context, String text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);

		} else {

			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}
		return toast;
	}
	
	public static Toast getToastLong(Context context, String text) {
		if (ToastFactory.context == context) {
			// toast.cancel();
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_LONG);

		} else {

			ToastFactory.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		return toast;
	}
	

	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

}
