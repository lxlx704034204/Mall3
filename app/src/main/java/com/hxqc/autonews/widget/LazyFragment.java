package com.hxqc.autonews.widget;

import android.support.v4.app.Fragment;

/**
 * 延迟加载
 * Created by huangyi on 16/1/5.
 */
public abstract class LazyFragment extends Fragment {

    /**
     * Fragment当前状态是否可见
     **/
    protected boolean isVisible;

    //adapter中的每个fragment切换的时候都会被调用 如果是切换到当前页 那么isVisibleToUser = true 否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     **/
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     **/
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     **/
    protected abstract void lazyLoad();

}
