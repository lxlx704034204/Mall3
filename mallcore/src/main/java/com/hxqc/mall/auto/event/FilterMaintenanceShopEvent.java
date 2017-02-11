package com.hxqc.mall.auto.event;

import com.hxqc.mall.auto.model.MyAuto;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 19
 * Des:
 * FIXME
 * Todo
 */

public class FilterMaintenanceShopEvent {

    public MyAuto myAuto;

    public FilterMaintenanceShopEvent(MyAuto myAuto) {
        this.myAuto = myAuto;
    }
}
