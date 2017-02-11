package com.hxqc.mall.core.adapter.me;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hxqc.mall.core.model.bill.ScoreBill;
import com.hxqc.mall.core.model.bill.ScoreBillList;
import com.hxqc.mall.core.model.bill.ScoreBillMatter;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.recyclerview.VRecyclerViewFooter;

import java.util.ArrayList;
import java.util.HashMap;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 账单列表的适配器
 */
public class MyScoreBillListAdapter_1 extends MyBillListAdapter_1 {
    private ArrayList<ScoreBillList> mData = new ArrayList<>();
    private ArrayList<HashMap<String, String>> groupName;
    private ArrayList<ScoreBillMatter> childData;
    private ScoreBill scoreBill;


    public MyScoreBillListAdapter_1(VRecyclerViewFooter vRecyclerViewFooter,
                                    ScoreBill scoreBill, ArrayList<ScoreBillList> mData, Context mContext) {
        super(vRecyclerViewFooter);
        this.scoreBill = scoreBill;
        this.mData = mData;
        this.mContext = mContext;
        initData();
    }

    public void notifyData(ScoreBill scoreBill, ArrayList<ScoreBillList> mData) {
        this.scoreBill = scoreBill;
        this.mData.clear();
        this.mData.addAll(mData);
        initData();
        notifyDataSetChanged();
    }

    /**
     * 数据分层
     */
    private void initData() {
        groupName = new ArrayList<>();
        childData = new ArrayList<>();
        if (mData != null)
            if (mData.size() > 0)
                for (int i = 0; i < mData.size(); i++) {
                    ScoreBillList list = mData.get(i);
                    if (i > 1) {
                        if (TextUtils.isEmpty(list.month)) {
                            mData.get(i).month = mData.get(i - 1).month;
                        }
                    }
                    if (list.matter.size() > 0)
                        for (ScoreBillMatter matter : list.matter) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("month", list.month);
                            map.put("subtotal", list.subtotal);
                            groupName.add(map);
                            childData.add(matter);
                        }
                }
    }


    @Override
    public long getHeaderId(int position) {
        if (position == 0)
            return -1;
        if (position == getItemCount() - 1)
            return -1;
        for (int i = 0; i < mData.size(); i++) {
            String month = mData.get(i).month;
            if (!TextUtils.isEmpty(month)) {
                if (month.equals(groupName.get(position - 1).get("month")))
                    return i;
            } else
                return i - 1;
        }
        return -1;
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() - 1) {
            ViewHeadHolder headHolder = (ViewHeadHolder) holder;
            HashMap<String, String> map = groupName.get(position - 1);
//            headHolder.month.setText(OtherUtil.singleMonth(map.get("month")));
            headHolder.month.setText(map.get("month"));
            headHolder.totalCost.setText(mContext.getString(R.string.this_month_cost)
                    + map.get("subtotal") + mContext.getString(R.string.score));
        }
    }


    @Override
    public void onVBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String cent = mContext.getString(R.string.wallet_cent);
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.label_type.setText(mContext.getString(R.string.usable_score));
                if (scoreBill.curPage != null)
                    if (scoreBill.curPage.equals("1")) {
                        headerHolder.total_charge.setText
                                (mContext.getString(R.string.bill_total_get) + ((int) scoreBill.obtain) + cent);
                        headerHolder.total_cost.setText
                                (mContext.getString(R.string.bill_total_cost) + ((int) scoreBill.expendamount) + cent);
//                        headerHolder.total_charge.setVisibility(View.INVISIBLE);
                        if (scoreBill.obtain == -1) headerHolder.total_charge.setText("");
                        if (scoreBill.expendamount == -1) headerHolder.total_cost.setText("");
                    }
                if (scoreBill.balance != -1)
                    headerHolder.number.setText(scoreBill.balance + "");
                headerHolder.unit.setText(cent);

                if (scoreBill.billCount == 0) {
                    headerHolder.empty_layout.setVisibility(View.VISIBLE);
                    headerHolder.icon_empty.setImageResource(R.drawable.ic_integration);
                    headerHolder.text_empty.setText(R.string.empty_score);
                } else headerHolder.empty_layout.setVisibility(View.GONE);
                break;
            case TYPE_NORMAL:
                ViewChildHolder childHolder = (ViewChildHolder) holder;
                ScoreBillMatter matter = childData.get(position - 1);
                String payTime = matter.payTime;
                if (!TextUtils.isEmpty(payTime))
//                    payTime = payTime.substring(0, 10);
                    childHolder.date.setText(payTime);
                childHolder.description.setText(matter.description);
                childHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySwitcher.toScoreBillDetail(mContext, childData.get(position - 1).billID + "");
                    }
                });
        /*文字的颜色*/
                if (matter.getTransactioinType().equals("获得")) {
                    childHolder.number.setTextColor(Color.parseColor("#FF7D53"));
                } else if (matter.getTransactioinType().equals("使用")) {
                    childHolder.number.setTextColor(Color.parseColor("#889DA7"));
                } else {
                    childHolder.number.setTextColor(Color.parseColor("#333333"));
                }
                childHolder.number.setText
                        (String.format("%s%s%s", matter.getTransactioinType(), Math.abs(matter.number), mContext.getString(R.string.wallet_cent)));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return childData == null ? 0 : childData.size() + 2;
    }

}
