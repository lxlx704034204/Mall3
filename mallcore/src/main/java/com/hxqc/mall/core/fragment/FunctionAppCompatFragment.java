package com.hxqc.mall.core.fragment;

import android.app.Fragment;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;


/**
 * Author: wanghao
 * Date: 2015-05-25
 * FIXME
 * Todo
 */
public abstract class FunctionAppCompatFragment extends Fragment {

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(description()); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(description());
    }

    private String description() {
        if (TextUtils.isEmpty(fragmentDescription())) {
            return "其他";
        }
        return fragmentDescription();
    }

    public abstract String fragmentDescription();

}
