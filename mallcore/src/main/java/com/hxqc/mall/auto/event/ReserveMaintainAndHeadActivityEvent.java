package com.hxqc.mall.auto.event;

import com.hxqc.mall.auto.model.MyAuto;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 19
 * Des:
 * FIXME
 * Todo
 */

public class ReserveMaintainAndHeadActivityEvent {

    public MyAuto myAuto;

    public ReserveMaintainAndHeadActivityEvent(MyAuto myAuto) {
        this.myAuto = myAuto;
    }
}
