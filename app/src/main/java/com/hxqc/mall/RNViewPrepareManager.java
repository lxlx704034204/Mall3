//package com.hxqc.mall;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//
//import com.facebook.react.LifecycleState;
//import com.facebook.react.ReactInstanceManager;
//import com.facebook.react.ReactRootView;
//import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
//import com.facebook.react.shell.MainReactPackage;
//import com.hxqc.mall.control.BootAnimControl;
//import hxqc.mall.Reactnative.NativeModuleHandlePackage;
//import com.hxqc.mall.reactnative.event.diyscroll.SSVReactPackage;
//import com.hxqc.util.DebugLog;
//
///**
// * Author: wanghao
// * Date: 2016-03-14
// * FIXME
// * Todo
// */
//public class RNViewPrepareManager {
//
//    protected static RNViewPrepareManager rnViewInstance;
//
//
//    private ReactInstanceManager mReactInstanceManager;
//    private ReactRootView mReactRootView;
//    private Activity ctx;
//
//
//    protected RNViewPrepareManager(Activity c) {
//        this.ctx = c;
//        init();
//    }
//
//    public View getmReactRootView() {
//        return mReactRootView;
//    }
//
//    public static RNViewPrepareManager getInstance(Activity c) {
//        if (rnViewInstance == null) {
//            synchronized (RNViewPrepareManager.class) {
//                if (rnViewInstance == null) {
//                    rnViewInstance = new RNViewPrepareManager(c);
//                }
//            }
//        }
//        return rnViewInstance;
//    }
//
//    private void init() {
//        mReactRootView = new ReactRootView(ctx);
//        mReactInstanceManager = ReactInstanceManager.builder()
//                .setApplication(ctx.getApplication())
//                .setBundleAssetName("rn/home.js")
////                .setBundleAssetName("index.android.bundle")
//                .setJSMainModuleName("index.android")
//                .addPackage(new MainReactPackage())
//                .addPackage(new SSVReactPackage())
//                .addPackage(new NativeModuleHandlePackage())
//                .setUseDeveloperSupport(false                                                                                                                                                                                                                                                                                   )
////                .setUseDeveloperSupport(BuildConfig.DEBUG)
//                .setInitialLifecycleState(LifecycleState.RESUMED)
//                .build();
//
//        mReactRootView.startReactApplication(mReactInstanceManager, "MallHome1", null);
//        DebugLog.i("rn_manager","init  complete");
//    }
//
//    public void showMenuDialog() {
//        if (mReactInstanceManager != null)
//            mReactInstanceManager.showDevOptionsDialog();
//    }
//
//    public void onBackPressed() {
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onBackPressed();
//        }
//    }
//
//    public void onPause() {
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onPause();
//        }
//    }
//
//    public void onResume(DefaultHardwareBackBtnHandler handler) {
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onResume(ctx, handler);
//        }
//    }
//
//    public void onDestroy() {
//
//        if (mReactInstanceManager !=null)
//            mReactInstanceManager.onDestroy();
//
//        if (mReactRootView !=null){
//            mReactRootView = null;
//        }
//
//        if (rnViewInstance != null)
//            rnViewInstance = null;
//    }
//
//
//}
