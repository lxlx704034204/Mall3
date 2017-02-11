package com.hxqc.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:李烽
 * Date:2016-06-06
 * FIXME
 * Todo api23以上的权限管理
 */
public class PermissionUtil {

    private static final int REQUEST_PERMISSION_SETTING = 0x002;
    public static final int REQUEST_STATUS_CODE = 0x001;

    /**
     * 检查权限
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean checkPermission(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(final Activity activity, String[] permissions, String[] permissionNames) {
        if (isAppFirstRun(activity)) {
//            if (!showRationaleUI(activity, permission))
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_STATUS_CODE);
            return;
        }
        List<String> unAllowPermissions = new ArrayList<>();//用户拒绝了但是没有勾选不在询问
        List<String> unArginPermissions = new ArrayList<>();//用户拒绝了并且勾选不在询问
//        List<String> unAllowPermissionNames = new ArrayList<>();
//        List<String> unArginPermissionNames = new ArrayList<>();
        String unArginPermissionNames = "";
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            boolean showRationaleUI = showRationaleUI(activity, permission);
            if (!showRationaleUI) {
                unArginPermissions.add(permission);
                StringBuilder stringBuilder = new StringBuilder(unArginPermissionNames);
                stringBuilder.append(permissionNames[i]);
                if (i < permissionNames.length - 1)
                    stringBuilder.append(",");
                unArginPermissionNames = stringBuilder.toString();
            } else {
                unAllowPermissions.add(permission);
//                unAllowPermissionNames.add(permissionNames[i]);
            }
        }
        String[] pers = new String[unAllowPermissions.size()];
        unAllowPermissions.toArray(pers);
        if (pers != null && pers.length > 0)
            ActivityCompat.requestPermissions(activity, pers, REQUEST_STATUS_CODE);
        new AlertDialog.Builder(activity).setMessage("程序运行需要" + unArginPermissionNames + "权限，请点击授予权限")
                .setCancelable(false).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toSetting(activity);
                dialog.dismiss();
            }
        })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        activity.finish();
                        dialog.dismiss();
                    }
                }).create().show();

//        if (showRationaleUI(activity, permission)) {
//            ActivityCompat.requestPermissions(activity, permissions, REQUEST_STATUS_CODE);
//        } else {
//            new AlertDialog.Builder(activity).setMessage("程序运行需要" + permissionName + "权限，请点击授予权限")
//                    .setCancelable(false).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    toSetting(activity);
//                    dialog.dismiss();
//                }
//            })
//                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            activity.finish();
//                            dialog.dismiss();
//                        }
//                    }).create().show();
//
//        }
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permission
     */
    public static void requestPermission(final Activity activity, String permission, String permissionName) {
        if (isAppFirstRun(activity)) {
            if (!showRationaleUI(activity, permission))
                ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_STATUS_CODE);
            return;
        }
        if (showRationaleUI(activity, permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_STATUS_CODE);
        } else {
            new AlertDialog.Builder(activity).setMessage("程序运行需要" + permissionName + "权限，请点击授予权限")
                    .setCancelable(false).setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    toSetting(activity);
                    dialog.dismiss();
                }
            })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            activity.finish();
                            dialog.dismiss();
                        }
                    }).create().show();

        }
    }

    private static void toSetting(Activity activity) {
        // 进入App设置页面
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    /**
     * 关于shouldShowRequestPermissionRationale函数的一点儿注意事项：
     * ***1).应用安装后第一次访问，则直接返回false；
     * ***2).请求权限时，如果用户Deny了，再次调用shouldShowRequestPermissionRationale()，则返回true；
     * ***3).请求权限时，如果用户Deny了，并选择了“dont ask me again”的选项时，再次调用shouldShowRequestPermissionRationale()时，返回false；
     * ***4).设备的系统设置中，禁止了应用获取这个权限的授权，则调用shouldShowRequestPermissionRationale()，返回false。
     */
    public static boolean showRationaleUI(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * 用来判断，App是否是首次启动：
     * ***由于每次调用shouldShowRequestPermissionRationale得到的结果因情况而变，
     * 因此必须判断一下App是否首次启动，才能控制好出现Dialog和SnackBar的时机
     */
    public static boolean isAppFirstRun(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (sp.getBoolean("first_run", true)) {
            editor.putBoolean("first_run", false);
            editor.apply();
            return true;
        } else {
            editor.putBoolean("first_run", false);
            editor.apply();
            return false;
        }
    }

    /**
     * 检查权限
     *
     * @param activity
     * @return 返回的是没被授权的权限
     */
    public static String[] checkPermissions(Activity activity, String[] permissions) {
        ArrayList<String> denidPermissionsArray = new ArrayList<>();//存放没有被授权的权限
        for (String permission : permissions) {
            int grantCode = ActivityCompat.checkSelfPermission(activity, permission);
            if (grantCode == PackageManager.PERMISSION_DENIED) {
                denidPermissionsArray.add(permission);
            }
        }
        return denidPermissionsArray.toArray(new String[denidPermissionsArray.size()]);
    }
}
