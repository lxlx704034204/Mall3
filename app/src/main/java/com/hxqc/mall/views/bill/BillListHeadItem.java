package com.hxqc.mall.views.bill;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.bill.BalanceBill;
import com.hxqc.mall.core.model.bill.Bill;
import com.hxqc.mall.core.model.bill.ScoreBill;
import com.hxqc.mall.core.util.OtherUtil;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-02
 * FIXME
 * Todo 账单列表的头部显示信息
 */

public class BillListHeadItem extends LinearLayout {
    private TextView labelType;
    private TextView number;
    //    private TextView unit;
    private TextView totalCharge;
    private TextView totalCost;
    private LinearLayout emptyLayout;
    private ImageView iconEmpty;
    private TextView textEmpty;

    private Context mContext;
    private TextView totalChargeLabel;
    private TextView unit1;
    private TextView totalCostLabel;
    private TextView unit2;
    private TextView unit3;

    public BillListHeadItem(Context context) {
        this(context, null);
    }

    public BillListHeadItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BillListHeadItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_bill_list_head_item, this);
        initView();
    }

    public void addData(Bill bill) {
        if (bill instanceof ScoreBill) {
            bindData((ScoreBill) bill);
        } else if (bill instanceof BalanceBill) {
            bindData((BalanceBill) bill);
        }
    }

    /**
     * 消费头部
     *
     * @param balanceBill
     */
    private void bindData(BalanceBill balanceBill) {
        String yuan = mContext.getString(R.string.yuan);
//        labelType.setText(mContext.getString(R.string.account_balance));
        labelType.setText("账   户  余   额：");
        totalChargeLabel.setText(mContext.getString(R.string.total_charge));
        totalCostLabel.setText(mContext.getString(R.string.total_cost));
        if (balanceBill.curPage != null)
            if (balanceBill.curPage.equals("1")) {
                totalCharge.setText(OtherUtil.reformatPriceNoEndZero(balanceBill.prepaidAmount));
                totalCost.setText(OtherUtil.reformatPriceNoEndZero(balanceBill.expendamount));
                number.setText(OtherUtil.reformatPriceNoEndZero(balanceBill.balance));
            }
//        if (!TextUtils.isEmpty(balanceBill.balance))
//            number.setText(OtherUtil.reformatPriceNoEndZero(balanceBill.balance));
//                   number.setText(com.hxqc.mall.usedcar.utils.OtherUtil.formatBalance(balanceBill.balance));
        unit1.setText(yuan);
        unit2.setText(yuan);
        unit3.setText(yuan);

        if (balanceBill.billCount == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            iconEmpty.setImageResource(R.drawable.ic_balance);
            textEmpty.setText(R.string.empty_balance);
        } else emptyLayout.setVisibility(View.GONE);
    }

    /**
     * 积分头部
     *
     * @param scoreBill
     */
    private void bindData(ScoreBill scoreBill) {
        String cent = mContext.getString(R.string.wallet_cent);
//        labelType.setText(mContext.getString(R.string.account_score));
        labelType.setText("可   用  积   分：");
        totalChargeLabel.setText(mContext.getString(R.string.total_charge_score));
        totalCostLabel.setText(mContext.getString(R.string.total_cost_score));
        if (scoreBill.curPage != null)
            if (scoreBill.curPage.equals("1")) {
                totalCharge.setText(OtherUtil.reformatPriceNoEndZero(scoreBill.obtain));
                totalCost.setText(OtherUtil.reformatPriceNoEndZero(scoreBill.expendamount));
                number.setText(OtherUtil.reformatPriceNoEndZero(scoreBill.balance));
            }
//        if (scoreBill.balance != -1)
//            number.setText(scoreBill.balance + "");
        unit1.setText(cent);
        unit2.setText(cent);
        unit3.setText(cent);

        if (scoreBill.billCount == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            iconEmpty.setImageResource(R.drawable.ic_integration);
            textEmpty.setText(R.string.empty_score);
        } else emptyLayout.setVisibility(View.GONE);
    }

    private void initView() {
        totalChargeLabel = (TextView) findViewById(R.id.total_charge_label);
        totalCharge = (TextView) findViewById(R.id.total_charge);
        unit1 = (TextView) findViewById(R.id.unit1);
        totalCostLabel = (TextView) findViewById(R.id.total_cost_label);
        totalCost = (TextView) findViewById(R.id.total_cost);
        unit2 = (TextView) findViewById(R.id.unit2);
        labelType = (TextView) findViewById(R.id.label_type);
        number = (TextView) findViewById(R.id.number);
        unit3 = (TextView) findViewById(R.id.unit3);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        iconEmpty = (ImageView) findViewById(R.id.icon_empty);
        textEmpty = (TextView) findViewById(R.id.text_empty);
    }
}
