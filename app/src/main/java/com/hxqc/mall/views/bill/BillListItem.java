package com.hxqc.mall.views.bill;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.bill.BalanceBillMatter;
import com.hxqc.mall.core.model.bill.BillMatter;
import com.hxqc.mall.core.model.bill.ScoreBillMatter;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DateUtil;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-02
 * FIXME
 * Todo
 */

public class BillListItem extends LinearLayout {
    private TextView billWeek;
    private TextView billDate;
    private TextView billType;
    private TextView billNumber;
    private TextView billContent;

    private String yuan;
    private String fen;

    public BillListItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_my_bill_list_item_child, this);
        billWeek = (TextView) findViewById(R.id.bill_week);
        billDate = (TextView) findViewById(R.id.bill_date);
        billType = (TextView) findViewById(R.id.bill_type);
        billNumber = (TextView) findViewById(R.id.bill_number);
        billContent = (TextView) findViewById(R.id.bill_content);
        yuan = context.getString(R.string.yuan);
        fen = context.getString(R.string.wallet_cent);
    }

    public void addData(BillMatter billMatter) {
        if (billMatter instanceof BalanceBillMatter) {
            bindData((BalanceBillMatter) billMatter);
        } else if (billMatter instanceof ScoreBillMatter) {
            bindData((ScoreBillMatter) billMatter);
        }
    }

    private void bindData(ScoreBillMatter billMatter) {
        billContent.setText(billMatter.description);
        if (billMatter.getTransactioinType().equals("获得")) {
            billNumber.setTextColor(Color.parseColor("#FF7D53"));
            billNumber.setText("+" + Math.abs(billMatter.number) + fen);
            billType.setTextColor(Color.parseColor("#FE7143"));
            billType.setBackgroundResource(R.drawable.bg_bill_list_type_charge);
        } else if (billMatter.getTransactioinType().equals("使用")) {
            billNumber.setTextColor(Color.parseColor("#66828F"));
            billNumber.setText("-" + Math.abs(billMatter.number) + fen);
            billType.setTextColor(Color.parseColor("#66828F"));
            billType.setBackgroundResource(R.drawable.bg_bill_list_type_cost);
        } else {
            if (billMatter.transactionType.equals("30")) {
                billNumber.setTextColor(Color.parseColor("#e02c36"));
                billNumber.setText("+" + Math.abs(billMatter.number) + fen);
                billType.setTextColor(Color.parseColor("#e02c36"));
                billType.setBackgroundResource(R.drawable.bg_bill_list_type_refund);
            } else {
                billNumber.setTextColor(Color.parseColor("#e02c36"));
                billNumber.setText("-" + Math.abs(billMatter.number) + fen);
                billType.setTextColor(Color.parseColor("#e02c36"));
                billType.setBackgroundResource(R.drawable.bg_bill_list_type_refund);
            }
        }
        billType.setText(billMatter.getTransactioinType());
        billDate.setText(getDate(billMatter.payTime));
        billWeek.setText(DateUtil.getWeekOfDate(DateUtil.str2Date(billMatter.payTime, "yyyy-MM-dd HH:mm:ss")));
    }

    private void bindData(BalanceBillMatter billMatter) {
        billContent.setText(billMatter.description);
        if (billMatter.getTransactionType().equals("充值")) {
            billNumber.setTextColor(Color.parseColor("#FE7143"));
            billNumber.setText("+" + OtherUtil.reformatPriceNoEndZero(billMatter.number) + yuan);
            billType.setTextColor(Color.parseColor("#FE7143"));
            billType.setBackgroundResource(R.drawable.bg_bill_list_type_charge);
        } else if (billMatter.getTransactionType().equals("消费")) {
            billNumber.setTextColor(Color.parseColor("#66828F"));
            billNumber.setText("-" + OtherUtil.reformatPriceNoEndZero(billMatter.number) + yuan);
            billType.setTextColor(Color.parseColor("#66828F"));
            billType.setBackgroundResource(R.drawable.bg_bill_list_type_cost);
        } else {
            billNumber.setTextColor(Color.parseColor("#e02c36"));
            billNumber.setText("+" + OtherUtil.reformatPriceNoEndZero(billMatter.number) + yuan);
            billType.setTextColor(Color.parseColor("#e02c36"));
            billType.setBackgroundResource(R.drawable.bg_bill_list_type_refund);
        }
        billType.setText(billMatter.getTransactionType());
        billDate.setText(getDate(billMatter.payTime));
        billWeek.setText(DateUtil.getWeekOfDate(DateUtil.str2Date(billMatter.payTime, "yyyy-MM-dd HH:mm:ss")));
    }

    private String getDate(String date) {
        if (TextUtils.isEmpty(date))
            return "";
        if (date.length() < 10)
            return "";
        return date.substring(5, 10);
    }
}
