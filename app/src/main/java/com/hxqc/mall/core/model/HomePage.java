package com.hxqc.mall.core.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:首页数据
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class HomePage {
    @Expose
    public ArrayList<AccessoryForHome> accessory;
    @Expose
    public ArrayList<NewAutoForHome> autos;
    @Expose
    public ArrayList<FinanceForHome> finance;
    @Expose
    public ArrayList<InsuranceForHome> insurance;
    @Expose
    public ArrayList<ModuleForHome> modules;
    @Expose
    public ArrayList<PromotionForHome> promotion;
    @Expose
    public ArrayList<SpecialOfferForHome> seckill;
    @Expose
    public ArrayList<UsedAutoForHome> usedCar;
}
