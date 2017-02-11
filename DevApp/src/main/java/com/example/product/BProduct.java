package com.example.product;

import android.util.Log;

/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public class BProduct extends AbstractProduct {
	private BValue name;

	public BProduct(BValue name) {
		this.name = name;
	}

	@Override
	public void doSth() {
		super.doSth();
		Log.i("Tag", "这是BBBB" + name.value);
	}
}
