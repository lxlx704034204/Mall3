package com.hxqc.mall.core.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import com.hxqc.util.DebugLog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaofan on 2016/12/23.
 */
public abstract class BaseFragment extends Fragment {
    private SparseArray<View> viewCache = new SparseArray<>();
    public Activity mContext;
    public EventBus mEventBus;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCommonUtils();
    }

    /**
     * 获取BaseActivity实例
     */
   /* public BaseActivity getBaseActivity() {
        return (BaseActivity) mContext;
    }
*/
    private void initCommonUtils() {
        mEventBus = EventBus.getDefault();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(description()); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(description());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewCache.clear();
        if (mEventBus.isRegistered(this)) {
            DebugLog.d("mEventBus", this.toString() + " unregister");
            mEventBus.unregister(this);
        }
    }


    public <R> R getView(@IdRes int viewID) {
        return getView(getView(), viewID);
    }

    public <R> R getView(Object parentView, @IdRes int viewID) {
        View cachedView = viewCache.get(viewID);
        if (null == cachedView) {
            if (parentView instanceof View)
                cachedView = ((View) parentView).findViewById(viewID);
            else if (parentView instanceof Activity) {
                cachedView = ((Activity) parentView).findViewById(viewID);
            }
            viewCache.put(viewID, cachedView);
        }
        return (R) cachedView;
    }

    private String description() {
        if (TextUtils.isEmpty(fragmentDescription())) {
            return "其他";
        }
        return fragmentDescription();
    }

    public abstract String fragmentDescription();

}
