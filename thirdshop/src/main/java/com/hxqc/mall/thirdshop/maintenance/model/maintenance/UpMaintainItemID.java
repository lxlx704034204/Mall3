package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2017-01-04
 * FIXME
 * Todo
 */

public class UpMaintainItemID {
    @Expose
    public String itemID;

    @Expose
    public ArrayList<String> goodsID = new ArrayList<>();
}
