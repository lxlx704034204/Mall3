package com.hxqc.mall.amap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;

import com.hxqc.mall.amap.utils.AMapUtils;
import com.hxqc.mall.amap.control.TTSController;
import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * 高德地图导航
 */

public class SimpleNaviActivity extends Activity implements AMapNaviViewListener {

    //导航View
    private AMapNaviView mAmapAMapNaviView;
    //是否为模拟导航
    private boolean mIsEmulatorNavi = true;
    //记录有哪个页面跳转而来，处理返回键
    private int mCode = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_navi);
//        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        processBundle(bundle);
        init(savedInstanceState);

    }

    private void processBundle(Bundle bundle) {
        if (bundle != null) {
//            mIsEmulatorNavi = true;
            mIsEmulatorNavi = bundle.getBoolean(AMapUtils.ISEMULATOR, true);
            mCode = bundle.getInt(AMapUtils.ACTIVITYINDEX);
        }
    }

    /**
     * 初始化
     */
    private void init(Bundle savedInstanceState) {
        mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.core_simple_navi_map);
        mAmapAMapNaviView.onCreate(savedInstanceState);
        mAmapAMapNaviView.setAMapNaviViewListener(this);

        if (mIsEmulatorNavi) {
            // 设置模拟速度
            AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
            // 开启模拟导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);

        } else {
            // 开启实时导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
        }

        TTSController ttsManager = TTSController.getInstance(this);// 初始化语音模块
        ttsManager.init();
        AMapNavi.getInstance(this).addAMapNaviListener(ttsManager);

    }

//-----------------------------导航界面回调事件------------------------

    /**
     * 导航界面返回按钮监听
     */
    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviSetting() {
        DebugLog.i("Navi", "onNaviSetting--------");
    }

    @Override
    public void onNaviMapMode(int arg0) {
        // TODO Auto-generated method stub
        DebugLog.i("Navi", "onNaviMapMode--------" + arg0);
    }

    @Override
    public void onNaviTurnClick() {
        // TODO Auto-generated method stub
        DebugLog.i("Navi", "onNaviTurnClick--------");

    }

    @Override
    public void onNextRoadClick() {
        // TODO Auto-generated method stub
        DebugLog.i("Navi", "onNextRoadClick--------");
    }

    @Override
    public void onScanViewButtonClick() {
        // TODO Auto-generated method stub
        DebugLog.i("Navi", "onScanViewButtonClick--------");
    }

    /**
     * 返回键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    // ------------------------------生命周期方法---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAmapAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAmapAMapNaviView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAmapAMapNaviView.onPause();
        AMapNavi.getInstance(this).stopNavi();
        TTSController.getInstance(this).stopSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAmapAMapNaviView.onDestroy();
        AMapNavi.getInstance(this).stopNavi();
        AMapNavi.getInstance(this).removeAMapNaviListener(TTSController.getInstance(this));
        TTSController.getInstance(this).stopSpeaking();
        TTSController.getInstance(this).destroy();
    }

    @Override
    public void onLockMap(boolean arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNaviViewLoaded() {
    }

}
