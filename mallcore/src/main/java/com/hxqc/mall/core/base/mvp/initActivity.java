package com.hxqc.mall.core.base.mvp;

import android.os.Bundle;

import com.hxqc.mall.core.base.BaseActivity;
import com.hxqc.mall.core.util.utils.TUtil;

/**
 * Created by zf
 */
public abstract class initActivity<T extends BasePresenter, E extends BaseModel> extends BaseActivity {
    public T mPresenter;
    public E mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutId());
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        this.bindView();
        this.init();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();
    }


    public abstract int getLayoutId();

    public abstract void bindView();

    public abstract void init();


}
