package com.example.product;


/**
 * Created 胡俊杰
 * 2016/11/9.
 * Todo:
 */

public class Factory {

	public static Action getProduct(Object name) {

		if (name instanceof AValue) {
			return new AProduct((AValue) name);
		} else if (name instanceof BValue) {
			return new BProduct((BValue) name);
		} else {
			throw new IllegalArgumentException();
		}
	}

}
