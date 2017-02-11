package com.hxqc.mall.auto.util;

import android.text.method.ReplacementTransformationMethod;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 30
 * FIXME
 * Todo 大小写切换
 */

public class MyAllCapTransformationMethod extends ReplacementTransformationMethod {

    public MyAllCapTransformationMethod() {
    }

    @Override
    protected char[] getOriginal() {
        char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        return cc;
    }
}
