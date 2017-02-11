package com.hxqc.mall.core.base.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.base.BaseFragment;
import com.hxqc.mall.core.util.utils.TUtil;

/**
 * Created by zf
 */
public abstract class initFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment {
    public T mPresenter;
    public E mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        this.bindView(getView());
        this.init();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
    }


    public abstract int getLayoutId();

    public abstract void bindView(View view);

    public abstract void init();

    @Override
    public String fragmentDescription() {
        return this.getClass().getSimpleName();
    }
}
