package com.hxqc.mall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.fragment.FunctionFragment;


/**
 * Author: HuJunJie
 * Date: 2015-03-16
 * FIXME
 * Todo
 */
public abstract class BaseTabFragment extends FunctionFragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getResourcesLayout(), container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    protected abstract int getResourcesLayout();


    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getClass().toString()); //统计页面
    }


    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().toString());
    }
}
