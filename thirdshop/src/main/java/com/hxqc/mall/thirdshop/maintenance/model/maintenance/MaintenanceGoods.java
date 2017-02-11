package com.hxqc.mall.thirdshop.maintenance.model.maintenance;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-22
 * FIXME
 * Todo 配件单品
 */
public class MaintenanceGoods  extends  MaintenanceReplaceGoods implements Serializable{

    public  ArrayList<MaintenanceReplaceGoods>  replaceable ;
}
