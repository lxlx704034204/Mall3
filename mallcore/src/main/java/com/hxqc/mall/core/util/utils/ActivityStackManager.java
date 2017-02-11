package com.hxqc.mall.core.util.utils;

import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

import com.hxqc.mall.config.application.SampleApplicationContext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Activity堆栈管理
 * Created by zhaofan
 */

public class ActivityStackManager {

    private Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();

    private static volatile ActivityStackManager mActivityManager;

    private ActivityStackManager() {

    }

    public static ActivityStackManager getInstance() {
        if (mActivityManager == null)
            synchronized (ActivityStackManager.class) {
                if (mActivityManager == null) {
                    mActivityManager = new ActivityStackManager();
                }
            }
        return mActivityManager;
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = null;
        if (!activitys.isEmpty()) {
            activity = activitys.peek().get();
        }
        return activity;
    }


    public void addActivity(Activity ac) {
        activitys.push(new WeakReference<Activity>(ac));
    }


    /**
     * 从activity堆栈中移除一个activity
     */
    public void removeActivityFromStack(Activity activity) {
        Iterator<WeakReference<Activity>> stackIterator = activitys.iterator();
        while (stackIterator.hasNext()) {
            Activity wActivity = stackIterator.next().get(); //WeakReference中的activity
            //移除无效的弱引用Activity
            if (wActivity == null) {
                stackIterator.remove();
            }
            // 获取当前位置的activity
            if (activity != null && wActivity != null && wActivity.equals(activity)) {
                stackIterator.remove();
            }
        }
    }

    public ArrayList<Activity> getAllOpenedActivities() {
        ArrayList<Activity> activities = new ArrayList<Activity>();
        Iterator<WeakReference<Activity>> activityStackIterator = activitys.iterator();
        while (activityStackIterator.hasNext()) {
            Activity activity = activityStackIterator.next().get();
            if (activity != null)
                activities.add(activity);
        }
        return activities;
    }


    public void finshActivity(Class<?> cls) {
        for (WeakReference<Activity> task : activitys) {
            if (task.get().getClass().equals(cls))
                if (!task.get().isFinishing()) {
                    task.get().finish();
                }
        }
    }


    public void finshAllActivity() {
        for (WeakReference<Activity> task : activitys) {
            if (!task.get().isFinishing()) {
                task.get().finish();
            }
        }
        activitys.clear();
    }


    public String getTopActivityAction() {
        android.app.ActivityManager manager = (android.app.ActivityManager) SampleApplicationContext.context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }


}
