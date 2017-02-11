package com.hxqc.mall.usedcar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.QA;
import com.hxqc.mall.usedcar.model.Table;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.QAStandardDialog;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:质保涵盖零部件及最高赔付标准
 *
 * @author: 吕飞
 * @since: 2016-06-21
 * Copyright:恒信汽车电子商务有限公司
 */
public class QAStandardActivity extends BackActivity {
    TextView mPrice1View, mPrice2View, mPrice3View;
    ListView mPriceListView;
    ArrayList<Table> mTables = new ArrayList<>();
    ArrayList<QA> mQAs = new ArrayList<>();
    QAStandardDialog mQAStandardDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTables = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(UsedCarActivitySwitcher.TABLE);
        mQAs = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(UsedCarActivitySwitcher.QA);
        setContentView(R.layout.activity_qa_standard);
        mQAStandardDialog = new QAStandardDialog(this);
        mQAStandardDialog.setCanceledOnTouchOutside(true);
        mPrice1View = (TextView) findViewById(R.id.price_1);
        mPrice2View = (TextView) findViewById(R.id.price_2);
        mPrice3View = (TextView) findViewById(R.id.price_3);
        mPriceListView = (ListView) findViewById(R.id.price_list);
        mPrice1View.setText(mQAs.get(0).item_name + "\n" + OtherUtil.reformatPrice(mQAs.get(0).item_price) + "/年");
        mPrice2View.setText(mQAs.get(1).item_name + "\n" + OtherUtil.reformatPrice(mQAs.get(1).item_price) + "/年");
        mPrice3View.setText(mQAs.get(2).item_name + "\n" + OtherUtil.reformatPrice(mQAs.get(2).item_price) + "/年");
        QuickAdapter<Table> quickAdapter = new QuickAdapter<Table>(this, R.layout.item_qa_standard, mTables) {
            @Override
            protected void convert(BaseAdapterHelper helper, final Table item) {
                helper.setText(R.id.name, item.key);
                helper.setText(R.id.price1, "¥" + OtherUtil.getCommaPrice(item.values.get(0).price));
                helper.setVisible(R.id.price1, !TextUtils.isEmpty(item.values.get(0).price));
                helper.setVisible(R.id.particulars1, !TextUtils.isEmpty(item.values.get(0).price));
                helper.setVisible(R.id.none1, TextUtils.isEmpty(item.values.get(0).price));
                helper.setText(R.id.price2, "¥" + OtherUtil.getCommaPrice(item.values.get(1).price));
                helper.setVisible(R.id.price2, !TextUtils.isEmpty(item.values.get(1).price));
                helper.setVisible(R.id.particulars2, !TextUtils.isEmpty(item.values.get(1).price));
                helper.setVisible(R.id.none2, TextUtils.isEmpty(item.values.get(1).price));
                helper.setText(R.id.price3, "¥" + OtherUtil.getCommaPrice(item.values.get(2).price));
                helper.setVisible(R.id.price3, !TextUtils.isEmpty(item.values.get(2).price));
                helper.setVisible(R.id.particulars3, !TextUtils.isEmpty(item.values.get(2).price));
                helper.setVisible(R.id.none3, TextUtils.isEmpty(item.values.get(2).price));
                helper.setOnClickListener(R.id.particulars1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQAStandardDialog.showDialog(item.values.get(0).detail);
                    }
                });
                helper.setOnClickListener(R.id.particulars2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQAStandardDialog.showDialog(item.values.get(1).detail);
                    }
                });
                helper.setOnClickListener(R.id.particulars3, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQAStandardDialog.showDialog(item.values.get(2).detail);
                    }
                });
            }
        };
        mPriceListView.setAdapter(quickAdapter);
    }
}
