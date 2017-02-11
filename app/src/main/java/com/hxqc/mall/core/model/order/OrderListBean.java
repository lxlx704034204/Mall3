package com.hxqc.mall.core.model.order;

import com.hxqc.mall.thirdshop.model.ThirdOrderModel;

/**
 * liaoguilong
 * Created by CPR113 on 2016/2/17.
 * 订单列表Bean
 */
public class OrderListBean {


    /**
     * 订单类型
     * maintenance 保养
     * accessory 用品
     * newAuto 新车自营
     * repair 维修
     * newEnerg 新能源
     * shopPormotion 门店活动
     */
    public MaintenanceBean maintenance4SShop;//4S保养
    public MaintenanceBean maintenanceFastrepair;//快修保养
    public AccessoryBean accessory;//用品
    public AccessoryBean accessory4SShop;//4S店用品
    public OrderModel newAuto;//新车自营
    public RepairBean repair;//预约维修
    public OrderModel newEnerg;//新能源
    public ThirdOrderModel shopPormotion;//4S店促销活动
    public WzServiceBean wzService;//违章处理
    public ChangeLicenceBean changeLicence;//驾驶证换证
    public AnnualnspectionBean annualnspection;//车辆年检服务
    public ShopSeckillAutoBean shopSeckillAuto;//4s店特价车
    public CarWashBean carWash;//洗车

    public String orderID;//订单号，做去重用


    @Override
    public int hashCode() {
        return orderID.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
            return true;
        }
        if (o.getClass() == OrderListBean.class)
        {
            OrderListBean n = (OrderListBean)o;
            return n.orderID.equals(orderID);
        }
        return false;
    }
}
