package com.hxqc.mall.core.base.mvp;

import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.core.util.utils.rxjava.RxManage;


/**
 * Created by zf
 */
public class BasePresenter<E, T extends BaseView> {
    public BaseActivity mContext;
    public E mModel;
    public T mView;
    public RxManage mRxManage;

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart() {
        if (mView instanceof initActivity) {
            mContext = (BaseActivity) mView;
        } else if (mView instanceof initFragment) {
            mContext = (BaseActivity) ((initFragment) mView).mContext;
        }

        if (mContext != null)
            mRxManage = mContext.mRxManage;
    }

    public void onDestroy() {
    }
}
