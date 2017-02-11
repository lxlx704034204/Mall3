package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-06-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellCarChoose {
    @Expose
    public ArrayList<CarColor> color;
    @Expose
    public ArrayList<CarLevel> level;
    @Expose
    public ArrayList<CarGearbox> gearbox;
    @Expose
    public String readme_example;
}
