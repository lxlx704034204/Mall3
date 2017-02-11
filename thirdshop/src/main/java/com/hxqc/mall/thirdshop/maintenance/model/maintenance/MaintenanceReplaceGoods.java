package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.io.Serializable;

/**
 * @Author : 钟学东
 * @Since : 2016-03-09
 * FIXME
 * Todo 可替换的配件
 */
public class MaintenanceReplaceGoods extends MaintenanceBaseGoods implements Serializable {

    public MaintenanceBaseGoods addition; // 可添加的商品
    public boolean isAddAddGoods = false ; //是否添加 可以添加的商品
    public  String isLinkage = "";
    public  String additionTag;
    public boolean isSelect = false; //用于第四版 4s店流程
}
