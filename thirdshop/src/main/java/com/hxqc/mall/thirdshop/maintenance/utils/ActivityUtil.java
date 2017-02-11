//package com.hxqc.mall.thirdshop.maintenance.utils;
//
//import android.app.Activity;
//
//
//import java.util.ArrayList;
//
///**
// * Author:胡仲俊
// * Date: 2016 - 04 - 21
// * FIXME
// * Todo 页面的操作工具
// */
//public class ActivityUtil {
//
//    private static ActivityUtil mInstance;
//    private ArrayList<Activity> activities;
//
//    public static synchronized ActivityUtil getInstance() {
//        if (mInstance == null) {
//            synchronized (ActivityUtil.class) {
//                if (mInstance == null) {
//                    mInstance = new ActivityUtil();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public void addActivity(Activity activity) {
//        if (activities == null) {
//            activities = new ArrayList<Activity>();
//        }
//        activities.add(activity);
//    }
//
//    public void killActivity() {
//        if (activities != null) {
//            for (int i = 0; i < activities.size(); i++) {
//                activities.get(i).finish();
//                activities.clear();
//            }
//        }
//    }
//
//    public void killInstance() {
//        if (mInstance != null) {
//            mInstance = null;
//        }
//    }
//
//
//}
