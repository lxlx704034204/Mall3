package com.hxqc.mall.launch.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.launch.activity.ChangePasswordActivity;
import com.hxqc.mall.launch.activity.CodeLoginActivity;
import com.hxqc.mall.launch.activity.ForgetPasswordActivity;
import com.hxqc.mall.launch.activity.LoginActivity;
import com.hxqc.mall.launch.activity.RegisterActivity;
import com.hxqc.util.DebugLog;

/**
 * Author:胡俊杰
 * Date: 2016/1/14
 * FIXME
 * Todo  用户认证跳转
 */
public class ActivitySwitchAuthenticate extends ActivitySwitchBase {
    public static final String ENTRANCE = "entrance";
    public static final String WROTE_NUM = "wrote_num";
    public static final String REGISTER_AGREEMENT = "register_agreement";


    public static void toLogin(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), CodeLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fragment.startActivity(intent);
    }

    public static void toLogin(Context context) {
        toCodeLogin(context, 50);
    }

    /**
     * 登陆
     *
     * @param entrance
     *         注册入口 默认为新车 50
     */
    public static void toLogin(Context context, int entrance) {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ENTRANCE, entrance);
        context.startActivity(intent);
    }

//
//    /**
//     * 登陆
//     *
//     * @param context
//     */
//    public static void toLogin(Context context, Bundle bundle) {
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (bundle != null) intent.putExtras(bundle);
//        if (context instanceof Activity) {
//            ((Activity) context).startActivityForResult(intent, 200);
//        }
//    }

    /**
     * 验证码登陆
     *
     * @param context
     */
    public static void toCodeLogin(Context context, int entrance) {
        Intent intent = new Intent(context, CodeLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ENTRANCE, entrance);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 200);
        }
    }

    /**
     * 找回密码
     */
    public static void toForgetPassword(Context context, String wroteNum) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WROTE_NUM, wroteNum);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /**
     * 注册
     *
     * @param context
     * @param entrance
     *         注册入口 默认为新车
     *         10：新车 |  20：4s店，
     */
    public static void toRegister(Context context,  int entrance) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt(ActivitySwitchAuthenticate.ENTRANCE, entrance);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 注册协议
     *
     * @param context
     * @param url
     */
    public static void toRegisterAgreement(Context context, String url) {
        DebugLog.e("test_bug", "toRegisterAgreement: " + url);
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.title_activity_register_agreement));
        bundle.putString(WebActivity.URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 修改密码
     *
     * @param context
     */
    public static void toChangePassword(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
//        intent.setAction(context.getResources().getString(R.string.action_change_password));
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
