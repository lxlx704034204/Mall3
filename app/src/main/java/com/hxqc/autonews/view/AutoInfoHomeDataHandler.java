package com.hxqc.autonews.view;

import com.hxqc.autonews.model.pojos.AutoInfoHomeData;

/**
 * Author:李烽
 * Date:2016-10-14
 * FIXME
 * Todo 汽车资讯首页数据获取后视图回调
 */

public interface AutoInfoHomeDataHandler {
    void onHomeDataResponse(AutoInfoHomeData homeData);

    void onHomeDataNull(String msg);
}
