package com.hxqc.mall.reactnative;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.activity.MainActivity.HomeKeyListener;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.mall.fragment.BaseFragment;
import com.hxqc.mall.reactnative.manager.RNPatchUpdateExecuteManager;
import com.hxqc.mall.reactnative.util.RNConfigUtil;
import com.hxqc.mall.reactnative.util.RNFileUpdateUtil;
import com.hxqc.util.DebugLog;


/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME
 * Todo
 */
public class HomeFragment_RN extends BaseFragment implements DefaultHardwareBackBtnHandler, HomeKeyListener {

    public boolean isDebugOnline = RNConfigUtil.isRNDebug;

    public static String Tag = "bsdiff_rn";
    public ReactInstanceManager mReactInstanceManager;
    HomeKeyListener homeKeyListener;
    private ReactRootView mReactRootView;
    private LinearLayout pView;
    private SubmitDialog mProgressDialog;
    RNPatchUpdateExecuteManager rnPatchUpdateExecuteManager;

    private void showLoading() {
        if (mProgressDialog == null)
            mProgressDialog = new SubmitDialog(getActivity());
        if (mProgressDialog.isShowing())
            return;
        mProgressDialog.setText("版本更新中请稍后...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void dismissLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        initListener(activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initListener(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeKeyListener = null;
    }

    private void initListener(Context activity) {
        try {
            MainActivity activity1 = (MainActivity) activity;
            activity1.setHomeKeyListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "--os1ccf");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DebugLog.w("home_data_life", "HomeFragment_RN onCreateView");
        rnPatchUpdateExecuteManager = RNPatchUpdateExecuteManager.getInstance(getContext().getApplicationContext());
        initReactView();
        pView = new LinearLayout(getContext());
        pView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pView.addView(mReactRootView);
        return pView;
    }

    int count = 0;
    boolean isFirstIn = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                DebugLog.i("rn_file_manager", "setContentView: 绘制完成");
                count++;
                if (count == 4) {
                    dismissLoading();
                    RNFileUpdateUtil.deleteExistsFile(RNConfigUtil.finalLoadBundleFilePath);
                    if (isFirstIn) {
                        DebugLog.i(Tag, "onViewCreated 执行操作 executeRNFileUpdate");
                        isFirstIn = false;

                        if (!isDebugOnline)
                            rnPatchUpdateExecuteManager.executeRNFileUpdate();
                    }
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 初始化react view
     */
    private void initReactView() {
        if (isDebugOnline) {
            mReactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(getActivity().getApplication())
                    .setBundleAssetName("index.android.bundle")
                    .setJSMainModuleName("index.android")
                    .addPackage(new MainReactPackage())
                    .addPackage(new NativeUIReactPackage())
                    .addPackage(new NativeModuleHandlePackage())
                    .setUseDeveloperSupport(true)
                    .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                    .build();
        } else {
            mReactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(getActivity().getApplication())
                    .setJSBundleFile(RNConfigUtil.finalLoadBundleFilePath)
                    .setJSMainModuleName("index.android")
                    .addPackage(new MainReactPackage())
                    .addPackage(new NativeUIReactPackage())
                    .addPackage(new NativeModuleHandlePackage())
                    .setUseDeveloperSupport(false)
                    .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                    .build();
        }

        mReactRootView = new ReactRootView(getActivity());
        mReactRootView.startReactApplication(mReactInstanceManager, RNConfigUtil.RNConnectJSKey, null);
    }


    @Override
    public String fragmentDescription() {
        return "RN首页";
    }

    @Override
    public void invokeDefaultOnBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(getActivity(), this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy();
        }
    }

    @Override
    public void onKeyUp(int code) {

        /**
         * 测试模式下的   调试菜单
         */
        mReactInstanceManager.showDevOptionsDialog();
    }
}
