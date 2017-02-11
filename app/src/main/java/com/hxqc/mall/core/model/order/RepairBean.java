package com.hxqc.mall.core.model.order;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 维修预约Bean
 */
public class RepairBean extends BaseOrderStatus.RepairOrderStatus {
	public String orderID;//订单ID string
	public String shopName;//店铺名称 string
	public String shopID;//店铺ID string
	public String apppintmentDate;//预约时间 string
	public String serviceType;//服务类型 string
	public String shopPhoto;//
	public int hasComment = 0;//

	public boolean getHasComment() {
		return hasComment == 1 ? true : false;
	}
}
