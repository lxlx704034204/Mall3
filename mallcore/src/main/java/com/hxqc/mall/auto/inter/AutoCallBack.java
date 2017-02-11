package com.hxqc.mall.auto.inter;

import com.hxqc.mall.auto.model.BrandGroup;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 19
 * FIXME
 * Todo
 */

public interface AutoCallBack<T> {

    void onSuccess(T response, ArrayList<BrandGroup> brandGroup);

    void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup);
}
