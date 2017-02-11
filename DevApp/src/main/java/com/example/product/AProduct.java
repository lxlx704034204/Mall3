package com.example.product;

import android.util.Log;

/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public class AProduct extends AbstractProduct {
	private AValue name;

	public AProduct(AValue name) {
		this.name = name;
	}


	@Override
	public void doSth() {
		super.doSth();
		Log.i("Tag", "这是AAAA   " + name.value);
	}
}
