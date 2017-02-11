//package com.hxqc.mall.reactnative;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import hxqc.mall.R;
//import com.hxqc.mall.core.util.SharedPreferencesHelper;
//import com.hxqc.mall.fragment.BaseFragment;
//import com.hxqc.util.DebugLog;
//import com.squareup.picasso.Picasso;
//import com.umeng.analytics.AnalyticsConfig;
//
///**
// * Author: wanghao
// * Date: 2016-03-14
// * FIXME
// * Todo
// */
//public class RNMainLaunchFragment extends BaseFragment {
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ImageView b = (ImageView) view.findViewById(R.id.iv_bg_alpha);
//
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_boot_anim, container, false);
//    }
//
//    @Override
//    public String fragmentDescription() {
//        return "启动页面";
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        DebugLog.i("testJS","LaunchActivity   |   onCreate :"+ System.currentTimeMillis());
//
//        AnalyticsConfig.enableEncrypt(true);//日志加密
//        setAppDefaultValue();
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    private void setAppDefaultValue(){
//        SharedPreferencesHelper helper = new SharedPreferencesHelper(getActivity());
//        helper.setPositionTranslate(false);
//        helper.setLoadPosition(false);
//
//    }
//}
