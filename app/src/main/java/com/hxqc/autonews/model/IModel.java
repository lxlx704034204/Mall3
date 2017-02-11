package com.hxqc.autonews.model;

import com.hxqc.mall.core.interfaces.CacheDataListener;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯部分数据管理模型
 */
public interface IModel<T> {
    void getData(LoadDataCallBack<T> callBack);
    void getData(CacheDataListener<T> callBack);
}
