package com.hxqc.autonews.util;

import android.content.Context;

import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * Author:李烽
 * Date:2016-09-30
 * FIXME
 * Todo 点击汽车资讯的item进入下一级界面的工具，不同的类型调不同的界面
 */

public class ToAutoInfoDetailUtil {

    public static void onToNextPage(String infoID,  AutoInformation.Type type, Context mContext) {
        switch (type) {
            case Images:
                //图集
                ActivitySwitchAutoInformation.toAutoGallery(mContext, infoID);
                break;
            case textAndImage:
                //图文
                ActivitySwitchAutoInformation.toAutoInfoDetail(mContext, infoID);
                break;
            case promotion:
                ActivitySwitcherThirdPartShop.toSalesItemDetail(infoID, mContext);
                break;
            case news:
                ActivitySwitcherThirdPartShop.toSalesNewsDetail(infoID, mContext);
                break;
            case autoCalendar:
                ActivitySwitchAutoInformation.toAutoCalendarInfoDetail(mContext, infoID);
                break;
            default:
                ActivitySwitchAutoInformation.toAutoInfoDetail(mContext, infoID);
                break;
        }
    }
}
