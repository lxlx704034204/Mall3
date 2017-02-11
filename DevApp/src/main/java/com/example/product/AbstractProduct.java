package com.example.product;


import android.util.Log;

/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public abstract class AbstractProduct implements Action {
	@Override
	public void doSth() {
		Log.i("Tag", "这是Abstract");
	}
}
