package com.hxqc.mall.auto.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 09 - 23
 * Des: 完善车辆信息弹窗工具
 * FIXME
 * Todo
 */

public class CompleteAutoDialogUtils {

    /**
     * 完善车辆信息弹窗操作
     */
    public static void completeAutoInfoDialog(final Context context) {
        if (UserInfoHelper.getInstance().isLogin(context)) {
            if (AutoSPControl.getDialogCount() == 0) {
                AutoInfoControl.getInstance().requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                    @Override
                    public void onSuccess(ArrayList<MyAuto> response) {
                        if (AutoSPControl.getDialogCount() != 1) {
                            AutoSPControl.saveDialogCount(1);
                        }
                        //判断是否弹窗过
                        if (response != null && !response.isEmpty()) {
                            AutoHelper.getInstance().createAutoLocal(context, response, AutoHelper.AUTO_INFO);
                            for (MyAuto hmMyAuto : response) {
                                if (TextUtils.isEmpty(hmMyAuto.autoModel)) {
                                    dialogAutoInfoComplete(context);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailed(boolean offLine) {

                    }
                });
            } else if (AutoSPControl.getDialogCount() != 0 && System.currentTimeMillis() -
                    UserInfoHelper.getInstance().getTokenTime(context) > AutoInfoContants.SIXTY_DAY) {
                AutoInfoControl.getInstance().requestAutoInfo(context, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
                    @Override
                    public void onSuccess(ArrayList<MyAuto> response) {
                        if (response != null && !response.isEmpty()) {
                            for (MyAuto hmMyAuto : response) {
                                if (TextUtils.isEmpty(hmMyAuto.autoModel)) {
                                    dialogAutoInfoComplete(context);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailed(boolean offLine) {

                    }
                });
            }
        }
    }

    /**
     * 完善车辆信息弹窗的方法
     */
    public static void dialogAutoInfoComplete(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_auto_info_complete, null);
        final AlertDialog imAlertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        imAlertDialog.setCanceledOnTouchOutside(false);
        imAlertDialog.show();
        ImageView cancelView = (ImageView) view.findViewById(R.id.dialog_cancel);
        Button completeView = (Button) view.findViewById(R.id.dialog_complete);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imAlertDialog.dismiss();
            }
        });

        completeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfoHelper.getInstance().isLogin(context)) {
                    ActivitySwitchAutoInfo.toCenterAutoInfo(context, "");
                    imAlertDialog.dismiss();
                }
            }
        });
        if (AutoSPControl.getDialogCount() != 1) {
            AutoSPControl.saveDialogCount(1);
        }
    }

    /**
     * 完善车辆信息弹窗的方法
     */
    public static void dialogAutoInfoComplete(final Context context, final MyAuto myAuto, final int flagActivity) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_auto_info_complete, null);
        final AlertDialog imAlertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();
        imAlertDialog.setCanceledOnTouchOutside(false);
        imAlertDialog.show();
        ImageView cancelView = (ImageView) view.findViewById(R.id.dialog_cancel);
        Button completeView = (Button) view.findViewById(R.id.dialog_complete);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagActivity == AutoInfoContants.AUTO_DETAIL) {
                    ActivitySwitchAutoInfo.toAddAuto(context, null, -1);
                    if (AutoSPControl.getDialogCount() != 1) {
                        AutoSPControl.saveDialogCount(1);
                    }
                } else {
                    imAlertDialog.dismiss();
                }

            }
        });

        completeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfoHelper.getInstance().isLogin(context)) {
//                    ActivitySwitchAutoInfo.toAutoInfo(getActivity());
//                    ActivitySwitchBase.toAutoInfo(getActivity(), "", AutoInfoContants.AUTO_DETAIL);
                    ActivitySwitchAutoInfo.toChooseBrandActivity(context, myAuto, AutoInfoContants.AUTO_DETAIL, false);
                    imAlertDialog.dismiss();
//                    activity.finish();
                }
            }
        });
    }
}
