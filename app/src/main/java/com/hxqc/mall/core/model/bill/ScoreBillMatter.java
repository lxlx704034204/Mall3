package com.hxqc.mall.core.model.bill;

import android.text.TextUtils;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 账单内容（积分）
 */
public class ScoreBillMatter extends BillMatter {
    public String billID;
    public String payTime;//交易时间 yyyy-mm-dd hh:mm:ss
    public String url;//特殊类型使用h5，为了扩展方便
    public String description;//内容信息
    public int number;//账单金额
	public String transactionType;//账单类型 10消费获得 20使用 30取消订单返回 40充值退还

    /**
     * 获取类型
     *
     * @return
     */
    public String getTransactioinType() {
//        if (number > 0)
//            return "获得";
//        else
//            return "使用";
	    if (TextUtils.isEmpty(transactionType))
		    return "";
	    switch (transactionType) {
		    case "10":
                return "获得";
            case "20":
                return "使用";
            case "30":
                return "退";
            case "40":
                return "退";
            default:
                return "";

        }
    }

    public boolean isNull() {
        return !TextUtils.isEmpty(billID) && TextUtils.isEmpty(payTime) && TextUtils.isEmpty(url)
                && TextUtils.isEmpty(description) && number == 0;
    }
//    public ScoreBillMatter match() {
//        ScoreBillMatter scoreBillMatter = new ScoreBillMatter();
//        scoreBillMatter.billID = this.billID;
//        scoreBillMatter.payTime = this.payTime;
//        scoreBillMatter.url = this.url;
//        scoreBillMatter.description = this.description;
//        scoreBillMatter.number = this.number;
//        return scoreBillMatter;
//    }
}
