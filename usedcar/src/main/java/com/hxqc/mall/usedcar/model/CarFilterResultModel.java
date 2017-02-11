package com.hxqc.mall.usedcar.model;

import java.util.ArrayList;

/**
 * 车辆筛选返回的 全部数据
 * Created by huangyi on 15/10/21.
 */
public class CarFilterResultModel {

    public String total; //符合条件的车辆总数
    public ArrayList<UsedCarBase> data; //车辆数据
}
