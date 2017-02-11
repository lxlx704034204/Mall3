package com.hxqc.mall.auto.inter;

import com.hxqc.mall.auto.model.BrandGroup;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 22
 * FIXME
 * Todo 回调控制器
 */

public interface CallBackControl {

    interface CallBack<T> {

        void onSuccess(T response);

        void onFailed(boolean offLine);
    }

    interface AutoCallBack<T> {

        void onSuccess(T response, ArrayList<BrandGroup> brandGroup);

        void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup);
    }

    interface LocalCallBack<T> {

        void onSuccess(T response);

        void onFailed(T response);
    }

}
