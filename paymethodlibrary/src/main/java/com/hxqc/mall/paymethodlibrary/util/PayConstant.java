package com.hxqc.mall.paymethodlibrary.util;

import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * Author: wanghao
 * Date: 2015-04-07
 * FIXME
 * 支付类型 的 标识
 */
public interface PayConstant {

    /**
     * 正常线上付款款流程
     * 标识requestOrder ， OrderPayRequest  +  flag
     * 进 PayMainActivity
     */
    int PAY_FLOW_NORMAL_ONLINE = 0;

    /**
     * 正常 线下 分期 付款款流程
     * 标识requestOrder ， OrderPayRequest  +  flag
     * 进 PayMainActivity
     */
    int PAY_FLOW_DEPOSIT_ONLINE = 1;

    //普通订单 分期线下 已签合同未付订金
    int NORMAL_HAD_CONTRACT_UNDERLINE = 11;

    //普通订单 线上已签合同 未付款
    int NORMAL_HAD_CONTRACT_ONLINE = 12;

    /**
     * 特卖 未签合同 线下 分期
     * 特卖支付:进 DepositActivity
     * 特卖订单:
     * 如果
     * 1.已付订金 flag+order_id 进PayMainActivity
     * 2.未付订金 flag+orderIDResponse 进DepositActivity
     */
    int PAY_ONLY_DEPOSIT_UNDERLINE = 3;

    /**
     * 特卖 未签合同 线上
     * 特卖支付:进 DepositActivity
     * 特卖订单:
     * 如果
     * 1.已付订金 flag+order_id 进PayMainActivity
     * 2.未付订金 flag+orderIDResponse 进DepositActivity
     */
    int PAY_ONLY_DEPOSIT_ONLINE = 5;

    // 信息已完善过  直接线上支付余款 flag+order_id 进PaySpareMoneyActivity
    int PAY_ONLY_ONLINE_PAID = 4;

    //未支付 或支付部分   进入第二步支付
    int uncomplete_info_pay_part = 13;

    //已支付订金或者 全部支付 未选择配送方式 进入完善信息 flag+order_id 进 PayMainActivity
    int JUST_COMPLETE_INFO = 6;

    //特卖 只付了订金 已签合同 线上 flag+order_id 进 PayMainActivity
    int SECKILL_HAD_CONTRACT_ONLINE = 7;
    //特卖 只付了订金 已签合同 线下 分期 flag+order_id 进 PayMainActivity
    int SECKILL_HAD_CONTRACT_UNDERLINE = 8;

    int pay_false = 202;


    //支付状态绑定 标识
    String PAY_STATUS_FLAG = "pay_flag";

    //支付页面中得flag
    //支付列表
    int PAY_LIST = 9;
    //订金
    int PAY_DEPOSIT = 10;

    //是否是选择地址
    int CHOOSE_ADDRESS = 111;
    String CHOOSE_ADDRESS_FLAG = "choose_address";

    String YIJIPAY = "YIJIPAY";//易极付
    String ALIPAY = "ALIPAY";//支付宝
    String YEEPAY = "YEEPAY";//易宝
    String WEIXIN = "WEIXIN";//微信支付
    String BALANCE = "BALANCE";//余额支付
    String INSHOP = "INSHOP";//到店支付
    String DISCOUNT = "DISCOUNT";//优惠抵扣
    /**
     * ---------------------------------------- 返回值 error code-------------------------------------------
     */
    /*
    易宝支付回调
     */
    int YEEPAY_SUCCESS = 9000;//支付成功
    int YEEPAY_fail = 4004;//支付失败

    /*
    支付宝回调
     */
    int ALIPAY_SUCCESS = 9000;//支付成功
    int ALIPAY_CONFIRMING = 8000;//支付确认中
    int ALIPAY_CANCEL = 6001;//交易放弃
    int ALIPAY_FAIL = 7000;//支付失败

    /*
    易极付回调
     */
    int YJF_PAY_REQUEST_CODE = 129;//易极付的请求码
    /**
     * 易极付状态码
     * 300 支付被取消
     * <p/>
     * 201 交易正在处理
     * <p/>
     * 200 支付成功
     * <p/>
     * 400 支付失败
     * <p/>
     * 401 数据异常
     */
    int YJF_PAY_CANCEL = 300;
    int YJF_TRADE_PROCESS = 201;
    int YJF_PAY_SUCCESS = 200;
    int YJF_PAY_FAIL = 400;
    int YJF_PAY_DATA_EXCEPTION = 401;

    /*
    微信支付回调
     */
    int WECHAT_SUCCESS = BaseResp.ErrCode.ERR_OK;
    int WECHAT_FAIL = BaseResp.ErrCode.ERR_SENT_FAILED;
    int WECHAT_CANCEL = BaseResp.ErrCode.ERR_USER_CANCEL;


}
