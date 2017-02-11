package com.hxqc.mall.thirdshop.accessory.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ListView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.LaborCost;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:工时费popupwindow
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class LaborCostDialog extends Dialog {
    ArrayList<LaborCost> mLaborCosts;
    ListView mListView;

    public LaborCostDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_labor_cost);
        mListView = (ListView) findViewById(R.id.list_view);
        setCanceledOnTouchOutside(true);
    }

    public void showDialog(ArrayList<LaborCost> laborCosts) {
        mLaborCosts = laborCosts;
        QuickAdapter<LaborCost> quickAdapter = new QuickAdapter<LaborCost>(getContext(), R.layout.item_labor_cost, mLaborCosts) {
            @Override
            protected void convert(BaseAdapterHelper helper, LaborCost item) {
                helper.setText(R.id.name, item.itemName);
                helper.setText(R.id.cost, OtherUtil.amountFormat(item.cost, true));
            }
        };
        mListView.setAdapter(quickAdapter);
        show();
    }
}
