package com.hxqc.mall.core.model.order;

import android.text.TextUtils;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/9.
 * 维修订单详情 Bean
 */
public class RepairOrderDetailsBean extends BaseOrderStatus.RepairOrderStatus{
   public String  orderId;//订单ID string
    public String  serviceType;//服务类型 string
    public String  serviceName;//服务类型 string
    public String appintmentDate;//预约时间 string
    public String name;//联系人 string
    public String  plateNumber;//车牌号 string
    public String  phone;//联系人手机号 string
    public String  autoModel;//车型名称 string
    public String  serviceAdviserName;//服务顾问名称 string
    public String   mechanicName;//维修技师名称 string
    public RepairShopPoint shopPoint;//店铺地点 { }
    public String workOrderID;//维修工单号 string
   public String hasComment;// 是否有评论 String
    public String shopPhoto;
    public String remark;//备注
  public boolean getHasComment() {
   return hasComment.equals("1")?true:false;
  }

 /**
  * 是否可以取消订单
  * @return
  */
 public boolean isCancel(){
  if((orderStatus.equals(ORDER_YYCG) && TextUtils.isEmpty(workOrderID)) || (orderStatus.equals(ORDER_DSL) && TextUtils.isEmpty(workOrderID)))
   return true;
  return false;
 }

    public  static class RepairShopPoint{
       public String id;//店铺ID string
        public String shopName;//店铺名称 string
        public String address;//地址 string
        public String tel;//电话 string
        public String latitude;//纬度 number
        public String longitude;//经度 number
        public String erpShopCode;

    }
}
