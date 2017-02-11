package com.hxqc.pay.util;

/**
 * Created by CPR062 on 2015/3/4.
 */
public interface ConstantValue {

    //order_id  订单id
    String PAY_MAIN_ORDER_ID = "order_id";
    //订单第四步到上传分期资料界面
    String SWITCH_FROM_STEP_4 = "switch_from_step_4";

    String v_from_step_4 = "step4";
    //订单详情 model
    String ORDER_PAY_REQUEST = "orderPayRequest";
    //aorder_data 订金id 时间model
    String ORDER_DATA = "order_data";

    String ORDER_STATUS = "orderStatus";

    //test token
    String identifier = "11112222-1";

    //-----------------------------------------导航栏------------------------------------------------------------------
    //----合同编辑页---------
    int OS1_EDIT_CONT_FRAG = 0;

    //----合同确认页---------
    int OS1_CONFIRM_CONT_FRAG = 1;

    //----订金支付页---------
    int OS2_DEPOSIT_FRAG = 2;

    //----在线支付页---------
    int OS2_PAY_ONLINE_FRAG = 3;

    //----完善信息页---------
    int OS3_COMPLETE_INFO_FRAG = 4;

    //----完成订单页---------
    int OS4_COMPLETE_FRAG = 5;

    //------------------recycle view 支付方式列表------------------------------------------------------------------------------

    //增加支付方式
    int PAY_METHOD_ITEM_ADD = 0;
    //删除支付方式
    int PAY_METHOD_ITEM_DEL = 1;
    //完善信息按钮
    int COMPLETE_INFO = 2;
    //选择的银行
    int PAY_METHOD_ITEM_SELECT_BANK = 3;
    //去支付
    int GO_TO_PAY = 4;


    //-----------------------------------支付列表 item 的支付方式---------------------------------------------------------------------
    //已支付
    int PAY_ONLINE_ALREADY_PAID = 6;
    //未支付
    int PAY_ONLINE_NOT_PAID = 7;

    //-------------------------完成 界面 分类---------------------------------------------------------------------------

    //线上支付  未全部支付

    int ORDER4_ONLINE_PARTIAL_PAYMENT = 0;

    //线上付款  已经支付完成   完成支付状态

    int ORDER4_ONLINE_COMPLETE_PAY = 1;

    //全款-线下支付

    int ORDER4_OFFLINE_PAYMENT = 2;

    //分期付款

    int ORDER4_INSTALLMENT = 3;

    //----------------------------------------------------------------------------------------------------------


}
