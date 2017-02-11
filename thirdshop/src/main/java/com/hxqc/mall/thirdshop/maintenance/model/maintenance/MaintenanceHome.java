package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-22
 * FIXME
 * Todo 维修首页数据
 */
public class MaintenanceHome {
    public String maintenanceManual;
    public MyAuto myAuto;
    public ArrayList<MaintenancePackage> maintenancePackage;
    public MaintenancePackage recommendProgram;
    public ArrayList<MaintenancePackage> baseMaintenancePackage;//基本套餐
    public ArrayList<Mechanic> mechanic;//技师
    public ArrayList<ServiceAdviser> serviceAdviser;//服务顾问
}
