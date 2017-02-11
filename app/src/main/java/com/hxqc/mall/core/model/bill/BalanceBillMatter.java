package com.hxqc.mall.core.model.bill;

import android.text.TextUtils;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 账单内容（余额）
 */
public class BalanceBillMatter extends BillMatter{
    public String billID;
    public String payTime;//交易时间 yyyy-mm-dd hh:mm:ss
    public String url;//特殊类型使用h5，为了扩展方便
    public String description;//内容信息
    public double number;//账单金额
    public String costType;// 消费类型 0默认|10新车|40 4s店预订|50用品|60保养|70维修
    private String transactionType;//账单类型 10充值|20消费|30退款

    /**
     * 获取账单的类型
     *
     * @return
     */
    public String getTransactionType() {
        if (TextUtils.isEmpty(transactionType))
            return "";
        if (transactionType.equals("10"))
            return "充值";
        else if (transactionType.equals("20"))
            return "消费";
        else if (transactionType.equals("30"))
            return "退款";
        else
            return "";
    }

    public boolean isNull() {
        return !TextUtils.isEmpty(billID)  && TextUtils.isEmpty(payTime) && TextUtils.isEmpty(url)
                && TextUtils.isEmpty(description) && number == 0 && TextUtils.isEmpty(costType)
                && TextUtils.isEmpty(transactionType);
    }
}
