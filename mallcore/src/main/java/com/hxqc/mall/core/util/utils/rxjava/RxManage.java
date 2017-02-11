package com.hxqc.mall.core.util.utils.rxjava;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Rxjava 生命周期管理
 * Created by zf
 */
public class RxManage {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者


    public void add(Subscription m) {

        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
        mCompositeSubscription.clear();
    }

}
