package com.hxqc.mall.core.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.hxqc.mall.core.util.utils.ActivityStackManager;
import com.hxqc.mall.core.util.utils.SPUtils;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.mall.core.util.utils.rxjava.RxManage;
import com.hxqc.util.DebugLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaofan on 2016/12/23.
 */
public class BaseActivity extends AppCompatActivity {
    private SparseArray<View> viewCache = new SparseArray<>();
    public Activity mContext;
    public ActivityStackManager mAManager;
    public DiskLruCacheHelper mDiskLruCache;
    public EventBus mEventBus;  //调用前需要regist();
    public RxManage mRxManage;
    public SPUtils mSpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mContext = this;
        initCommonUtils();
    }

    private void initCommonUtils() {
        mAManager = ActivityStackManager.getInstance();
        mAManager.addActivity(this);
        mDiskLruCache = DiskLruCacheHelper.builder(this);
        mSpUtils = SPUtils.getInstance(this);
        mEventBus = EventBus.getDefault();
        mRxManage = new RxManage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAManager.removeActivityFromStack(this);
        mRxManage.clear();
        viewCache.clear();
        if (mEventBus.isRegistered(this)) {
            DebugLog.d("mEventBus", this.getClass().getSimpleName() + " unregister");
            mEventBus.unregister(this);
        }
    }


    public <R> R getView(@IdRes int viewID) {
        return getView(this, viewID);
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


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}
