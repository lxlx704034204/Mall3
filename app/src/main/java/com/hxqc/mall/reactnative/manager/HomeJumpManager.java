package com.hxqc.mall.reactnative.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.model.pojos.InfoType;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.config.router.RouteOpenActivityUtil;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.reactnative.model.HomeSlideADModel_RN;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.xiaoneng.ChatManager;

import java.util.ArrayList;

/**
 * Author:  wh
 * Date:  2017/1/9
 * FIXME
 * Todo   首页跳转处理
 */

public class HomeJumpManager {

    //网上4s店
    private static final String NET_4S_SHOP = "NET_4S_SHOP";
    //主页切换tab
    private static final String MAIN_TAB = "MAIN_TAB";
    //热门关键字
    private static final String HOT_KEY = "HOT_KEY";
    //搜索框
    private static final String SEARCH_BOX = "SEARCH_BOX";
    //消息
    private static final String MESSAGE = "MESSAGE";
    //广告栏
    private static final String AD_SLIDER = "AD_SLIDER";
    //在用按钮
    private static final String USE_MODULE = "USE_MODULE";
    //常规保养
    private static final String NORMAL_MAINTENANCE = "NORMAL_MAINTENANCE";
    //关于恒信
    private static final String ABOUT_US = "ABOUT_US";
    //去买车   二手车
    private static final String USED_CAR_TO_BUY_CAR = "BUY_USED_CAR";
    //去卖车   二手车
    private static final String USED_CAR_TO_SALE_CAR = "SALE_USED_CAR";
    //二手车估价
    private static final String USED_CAR_EVALUATE = "USED_CAR_EVALUATE";
    //    二手车置换
    private static final String USED_CAR_Metathesis = "USED_CAR_Metathesis";
    //我的二手车
    private static final String MY_USED_CAR = "MY_USED_CAR";

    /**
     * 车服务
     */
    //违章查缴
    private static final String BREAK_RULES = "BREAK_RULES";
    //驾驶证换证
    private static final String CHANGE_CERTIFICATE = "CHANGE_CERTIFICATE";
    //车辆年审
    private static final String ANNUAL_AUDIT = "ANNUAL_AUDIT";
    //周边 地图服务
    private static final String AROUND_MAP = "AROUND_MAP";
    //洗车
    private static final String CAR_WASH = "CAR_WASH";
    //代驾
    private static final String DESIGNATED_DRIVER = "DESIGNATED_DRIVER";
    //驾考
    private static final String DRIVER_EXAM = "DRIVER_EXAM";
    /**
     * 会员中心
     */
    //会员规则
    private static final String VIP_RULE = "VIP_RULE";
    //我的钱包
    private static final String MY_WALLET = "MY_WALLET";
    //我的车辆列表
    private static final String MY_CARS = "MY_CARS";

    //其他未实现
    private static final String OTHER_S = "OTHER_S";
    //用品销售
    private static final String Accessory_Sell = "Accessory_Sell";
    //客户投诉
    private static final String Customer_Complaint = "Customer_Complaint";
    //消费记录
    private static final String Consumption_Record = "Consumption_Record";
    //在线客服
    private static final String Online_Service = "Online_Service";
    //团购汇
    private static final String Group_purchase = "Group_purchase";

    //从路由跳转
    private static final String router_for_turn = "router_for_turn";
    //修车预约
    private static final String RESERVE_MAINTAIN = "reserve_maintain";

    /**
     * 资讯列表
     */
    private static final String zixun_more = "zixun_more";
    private static final String zixun_btn = "zixun_btn";
    private static final String zixun_list_item = "zixun_list_item";

    //------------------------------------------TAG  END------------------------------------------------------------

    private String Tag = "test_sv";


    private boolean isFromRNClick = false;//是否是rn的点击
    private MainActivity currentActivity;
    private boolean isMoved = false;
    private ArrayList<String> tempJumpInfo = new ArrayList<>();

    private static HomeJumpManager mInstance;

    public void setMoving(boolean moving) {
        DebugLog.w(Tag, "setMoving 移动中: " + moving);
        isMoved = moving;
    }

    public static HomeJumpManager getInstance() {
        if (mInstance == null) {
            synchronized (HomeJumpManager.class) {
                if (mInstance == null) {
                    mInstance = new HomeJumpManager();
                }
            }
        }
        return mInstance;
    }

    public void touchUp() {
        DebugLog.w(Tag, "touchUp  moving  点击抬起: " + isMoved);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                upOp();
            }
        }, 210);
    }

    private void upOp() {
        if (tempJumpInfo == null)
            return;

        if (tempJumpInfo.size() < 3)
            return;
        DebugLog.w(Tag, "touchUp  未移动  跳转");
        DebugLog.w(Tag, "touchUp  跳转数据：" + "title--" + tempJumpInfo.get(0) + "type--" + tempJumpInfo.get(1) + "json--" + tempJumpInfo.get(2));
        jumpRule(tempJumpInfo.get(0), tempJumpInfo.get(1), tempJumpInfo.get(2));

        if (tempJumpInfo != null) {
            tempJumpInfo.clear();
            DebugLog.w(Tag, "touchUp  数据 清空 完成-------------");
        }
    }

    //rn直接调用跳转
    public void onlyRNJump(MainActivity mCA, String title, String type, String jsonStr) {
        this.currentActivity = mCA;
        jumpRule(title, type, jsonStr);
    }

    //跳转
    public void jump(MainActivity mCA, String title, String type, String jsonStr) {
        this.currentActivity = mCA;
        DebugLog.w(Tag, "jump  点击 准备-------------");
        DebugLog.w(Tag, "jump  准备数据：" + "title--" + title + "type--" + type + "json--" + jsonStr);

        if (tempJumpInfo == null) {
            tempJumpInfo = new ArrayList<>();
        }

        tempJumpInfo.clear();
        tempJumpInfo.add(title);
        tempJumpInfo.add(type);
        tempJumpInfo.add(jsonStr);
        DebugLog.w(Tag, "jump  数据更新完成-------------");
    }


    //所有跳转规则
    private void jumpRule(String title, String type, String jsonStr) {

        switch (type) {
            //新加从路由跳转
            case router_for_turn:
                RouteOpenActivityUtil.linkToActivity(currentActivity, jsonStr);
                break;
            case NET_4S_SHOP:
//                ToastHelper.showRedToast(context, "网上4s店");
                ActivitySwitcherThirdPartShop.to4S(currentActivity,0);
                break;
            case MAIN_TAB:
                switcherMainTab(jsonStr);
                break;
            case AD_SLIDER:
                getAD(jsonStr);
                break;
            case USE_MODULE:
                switcher(jsonStr);
                break;
            case NORMAL_MAINTENANCE:
                ActivitySwitchAutoInfo.toFlowMaintain(currentActivity);
                break;
            case ABOUT_US:
                toAboutUs(jsonStr);
                break;
            case OTHER_S:
                ToastHelper.showRedToast(currentActivity, title + " : " + type);
                break;
            case SEARCH_BOX:
                ActivitySwitcher.toSearch(currentActivity);
                break;
            case MESSAGE:
                ActivitySwitcher.toMyMessageActivity(currentActivity);
                break;
            case USED_CAR_TO_BUY_CAR:
                // * toBuyCarFromHome
                UsedCarActivitySwitcher.toBuyCarFromHome(currentActivity);
                break;
            case USED_CAR_TO_SALE_CAR:
                //  * toSellCarFromHome
                UsedCarActivitySwitcher.toSellCarFromHome(currentActivity);
                break;
            case USED_CAR_EVALUATE:
//                ToastHelper.showRedToast(context, "二手车估价");
                UsedCarActivitySwitcher.toValuation(currentActivity);
                break;
            case HOT_KEY:
                break;
            case AROUND_MAP:
                ActivitySwitchAround.toAroundServiceMapList(currentActivity, Integer.parseInt(jsonStr));
                break;
            case CAR_WASH:
//                ToastHelper.showRedToast(context, "洗车");
//                CarWashActivitySwitcher.toCarWashShopList(context);
                CarWashActivitySwitcher.toWashCar(currentActivity);
                break;
            case DESIGNATED_DRIVER:
                ActivitySwitcher.toDiDi(currentActivity);
                break;
            case VIP_RULE:
                ActivitySwitchBase.toH5Activity(currentActivity, "会员手册", new UserApiClient().accountRule());
                break;
            case MY_WALLET:
                UserInfoHelper.getInstance().loginAction(currentActivity, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMyWallet(currentActivity);
                    }
                });
                break;
            case MY_CARS:
                UserInfoHelper.getInstance().loginAction(currentActivity, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitchBase.toCenterAutoInfo(currentActivity, "");
                    }
                });
                break;
            case Accessory_Sell:
//                ToastHelper.showRedToast(context, "用品销售");
                ActivitySwitcherAccessory4S.toAccessorySaleFromHome(currentActivity);
                break;
            case BREAK_RULES:
//                ToastHelper.showRedToast(context, "违章查缴");
                ActivitySwitchAround.toIllegalQueryActivity(currentActivity);
                break;
            case CHANGE_CERTIFICATE:
                ActivitySwitchAround.toDriversLicenseChangeActivity(currentActivity);
//                ToastHelper.showRedToast(context, "驾驶证换证");
                break;
            case ANNUAL_AUDIT:
                ActivitySwitchAround.toVehicleInspectionActivity(currentActivity);
//                ToastHelper.showRedToast(context, "车辆年审");
                break;
            case DRIVER_EXAM:
//                ToastHelper.showRedToast(context, "考驾照");
                ActivitySwitcherExam.toExamHomeActivity(currentActivity);
                break;
            case USED_CAR_Metathesis:
//                ToastHelper.showRedToast(context, "二手车置换");
//                UsedCarActivitySwitcher.toExchange(context);
                UsedCarActivitySwitcher.toExchangeEntrance(currentActivity);
                break;
            case MY_USED_CAR:
                UserInfoHelper.getInstance().loginAction(currentActivity, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        UsedCarActivitySwitcher.toSellCarInfo(currentActivity);
                    }
                });
                break;
            case Customer_Complaint:
//                ToastHelper.showRedToast(context, "客户投诉");
                ActivitySwitcher.toComplaints2(currentActivity);
                break;
            case Consumption_Record:
//                ToastHelper.showRedToast(context, "消费记录");
                UserInfoHelper.getInstance().loginAction(currentActivity, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        ActivitySwitcher.toMyBalanceBillList(currentActivity);
                    }
                });
                break;
            case Online_Service:
//                ToastHelper.showRedToast(context, "在线客服");
                ChatManager.getInstance().startChatWithNothing();
                break;
            case zixun_btn:
                DebugLog.w("js_test", jsonStr);
                autoInformationTabChangeTo(jsonStr);
                break;
            case zixun_list_item:
                DebugLog.w("js_test", jsonStr);
                AutoInformation autoInformation = JSONUtils.fromJson(jsonStr, new TypeToken<AutoInformation>() {
                });
                ToAutoInfoDetailUtil.onToNextPage(autoInformation.infoID, autoInformation.getType(), currentActivity);
                break;
            case Group_purchase:
                ActivitySwitcherThirdPartShop.toGroupBuyMerge(currentActivity, new Bundle());
                break;
            //首页修车预约跳转方法
            case RESERVE_MAINTAIN:
                AutoInfoControl.getInstance().toActivityInter(currentActivity, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, null);
                break;
            default:
                break;
        }
    }

    /**
     * 咨询tab 跳转
     */
    private void autoInformationTabChangeTo(String json) {
        DebugLog.w("js_test", "autoInformationTabChangeTo");
        if (TextUtils.isEmpty(json)) {
            DebugLog.w("js_test", "autoInformationTabChangeTo  1111");
            switcherMainTab("1");
        } else {
            DebugLog.w("js_test", "autoInformationTabChangeTo  2222");
            InfoType infoType = JSONUtils.fromJson(json, new TypeToken<InfoType>() {
            });
            if (infoType != null && !TextUtils.isEmpty(infoType.guideName)) {
                DebugLog.w("js_test", "autoInformationTabChangeTo  3333");
                if (currentActivity != null) {
                    Message message = Message.obtain();
                    message.what = MainActivity.AUTO_INFOMATION_CHANGE_TAB;
                    message.obj = infoType.guideName;
                    currentActivity.mHandler.sendMessage(message);
                }
            } else {
                DebugLog.w("js_test", "autoInformationTabChangeTo  4444");
                switcherMainTab("1");
            }
        }
    }

    /**
     * 界面跳转
     *
     * @param clickToActivityString 跳转参数
     */
    private void switcher(String clickToActivityString) {
        DebugLog.e("js_test", clickToActivityString);
        if (clickToActivityString.contains("保险")) {
            ActivitySwitcher.toH5Activity(currentActivity, "买保险", ApiUtil.getInsuranceURL("/Insurance/Index/index.html"));
        } else if (clickToActivityString.contains("贷款")) {
            DebugLog.e("js_test", "贷款");
        } else if (clickToActivityString.contains("二手车竞拍")) {
            verdifyLogin(clickToActivityString);
        } else {
            String[] stringArray = clickToActivityString.split("\\|");
            ActivitySwitcher.toWhere(currentActivity, stringArray[0], obtainBundleParams(stringArray));
        }
    }

    /**
     * 判断是否登录
     */
    private void verdifyLogin(final String dataStr) {

        if (UserInfoHelper.getInstance().isLogin(currentActivity)) {
            String[] stringArray = dataStr.split("\\|");
            ActivitySwitcher.toWhere(currentActivity, stringArray[0], obtainBundleParams(stringArray));
        } else {
            UserInfoHelper.getInstance().loginAction(currentActivity, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    String[] stringArray = dataStr.split("\\|");
                    ActivitySwitcher.toWhere(currentActivity, stringArray[0], obtainBundleParams(stringArray));
                }
            });
        }

    }

    /**
     * 切换主页tab
     *
     * @param position tab位置
     */
    private void switcherMainTab(String position) {
        if (!TextUtils.isEmpty(position)) {
//            int i = Integer.parseInt(position);
            if (currentActivity != null) {
                Message message = Message.obtain();
                message.what = MainActivity.MAIN_CHANGE_TAB;
                message.obj = position;
                currentActivity.mHandler.sendMessage(message);
            }
        }
    }

    /**
     * 获取用户跳转到某界面时传入的参数，跳转时参数格式如下：
     * clickToActivityAction="com.hxqc.mall.activity.auto.XXXActivity|key1:value1,key2:value2...”
     *
     * @param stringArr 分割成数组的跳转参数
     * @return 如果跳转中包含参数，返回包含key：value的bundle，否则返回null
     */
    private Bundle obtainBundleParams(String[] stringArr) {
        String paramsCollection = stringArr.length > 1 ? stringArr[1] : null;
        if (TextUtils.isEmpty(paramsCollection)) {
            return null;
        }
        Bundle bundle = new Bundle();
        String[] argumentsArray = paramsCollection.split(",");
        for (String anArgumentsArray : argumentsArray) {
            String[] params = anArgumentsArray.split(":");
            if (!TextUtils.isEmpty(params[0])) {
                if (params[1].contains("http")) {
                    if (!TextUtils.isEmpty(params[2]))
                        bundle.putString(params[0], params[1] + ":" + params[2]);
                } else {
                    bundle.putString(params[0], params[1]);
                }
            }
        }
        return bundle;
    }


    /**
     * 获取广告items
     */
    private synchronized void getAD(String response) {

        DebugLog.e("js_test", response);

        if (TextUtils.isEmpty(response)) {
            DebugLog.e("js_test", "getAD response 为空");
        } else {
            HomeSlideADModel_RN model = JSONUtils.fromJson(response, new TypeToken<HomeSlideADModel_RN>() {
            });

            if (model == null) {
                DebugLog.e("js_test", "生成 HomeSlideADModel_RN 失败 model 为空");
                return;
            }

            model.activitySwitch(currentActivity);
        }

    }

    /**
     * 关于我们
     */
    private void toAboutUs(String url) {
        DebugLog.e("js_test", "toAboutUs: " + url);
    }

}
