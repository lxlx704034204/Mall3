package com.hxqc.mall.views.bill;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.util.DateUtil;

import java.util.Map;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-02
 * FIXME
 * Todo
 */
public class BillListGroupItem extends LinearLayout {
    private TextView month;
    private TextView totalCost;
    private Context mContext;

    public BillListGroupItem(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_my_bill_list_head, this);
        month = (TextView) findViewById(R.id.month);
        totalCost = (TextView) findViewById(R.id.total_cost);
    }

    public void addData(Map<String, String> map, String unit) {
        month.setText(getMonth(map.get("month")));
//        totalCost.setText(String.format("%s%s%s", mContext.getString(R.string.this_month_cost),
//                map.get("subtotal"), unit));
        if (unit.equals("元"))
            totalCost.setText(String.format("%s%s%s%s%s%s%s", "消费", map.get("expendMonth"),
                    unit, "/", "充值", map.get("prepaidMonth"), unit));
        else totalCost.setText(String.format("%s%s%s%s%s%s%s", "使用", map.get("expendMonth"),
                unit, "/", "获得", map.get("prepaidMonth"), unit));
    }

    private String getMonth(String month) {
        if (TextUtils.isEmpty(month))
            return "";
        if (!month.contains("-")) {
            return month;
        }
        String[] split = month.split("-");
        if (split.length < 2)
            return month;
        String year = split[0];
        String m = split[1];
        int currentYear = DateUtil.getCurrentYear();
        int currentMonth = DateUtil.getcurrentMonth() + 1;
        if (year.equals(currentYear + "")) {
            try {
                int monthInt = Integer.parseInt(m);
                if (monthInt == currentMonth)
                    return "本月";
                else
                    return m + "月";
            } catch (NumberFormatException e) {
                if (m.equals(currentMonth + "")) {
                    return "本月";
                } else
                    return m + "月";
            }
        } else return year + "年" + m + "月";
    }
}
