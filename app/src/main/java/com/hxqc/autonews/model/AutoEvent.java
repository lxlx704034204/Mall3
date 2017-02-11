package com.hxqc.autonews.model;

import android.os.Bundle;

/**
 * Author:李烽
 * Date:2016-11-10
 * FIXME
 * Todo 汽车资讯EventBus的事件
 */

public class AutoEvent {
    public AutoEvent(int eventCode) {
        this.eventCode = eventCode;
    }

    public int eventCode;
    public Bundle bundle;
}
