package com.hxqc.mall.auto.config;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 07
 * FIXME
 * Todo 车辆信息flag
 */
public class AutoInfoContants {

    public static final int KEYBOARD_MODE_NUMBER_LETTER = 0;
    public static final int KEYBOARD_MODE_CITY = 1;
    public static final int AUTO_TYPE_BACK_DATA = 2;
    public static final int MY_AUTO_DATA = 3;
    public static final int REQUEST_ADD_OR_EDIT = 4;
    public static final int ADD_OR_EDIT_SUCCEESS = 5;
    public static final int GET_AUTO_DATA = 6;
    public static final int AUTO_DETAIL = 7;//常规保养页面flag
    public static final int ADD_AUTO = 8;
    public static final int EDIT_AUTO = 9;
    public static final int SHOP_QUOTE = 10;//店铺报价页面flag
    public static final int HOME_PAGE = 11;//首页页面flag
    public static final int MEMBER_CENTER = 12;//会员中心flag
    public static final int CHANGE_PAGE = 17;
    public static final int FOURS_SHOP = 18;

    public static final int ADD_PAGE = 13;
    public static final int EDIT_PAGE = 14;

    public static final int CHOOSE_SUCCESS = 15;

    public static final int RESERVE_MAINTAIN = 16;

    public static final int FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE = 19;

    public static final int FLAG_ACTIVITY_APPOINTMENT_LIST = 20;
    public static final int FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL = 21;

    public static final int FLAG_ACTIVITY_APPOINTMENT_HOME = 22;
    public static final int FLAG_ACTIVITY_APPOINTMENT_4S = 23;
    public static final int FLAG_ACTIVITY_APPOINTMENT_AUTO_DETAIL = 24;

    public static final int FLAG_MODULE_MAINTAIN = 25;
    public static final int FLAG_MODULE_APPOINTMENT = 26;

    public static final boolean IS_TEST = false;

    public static final long SIXTY_DAY = 60 * 24 * 60 * 60 * 1000;

    private static int time = 0;

    public static void setTime() {
        time++;
    }

    public static int getTime() {
        return time;
    }

    public static void clearTime() {
        time = 0;
    }

    public static final String LOG_J = "Log.J";
    public static final String LOG_TEST_J = "Log.test.J";

}
