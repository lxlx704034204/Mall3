package com.hxqc.mall.usedcar.views;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.Detail;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:质保标准popupwindow
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class QAStandardDialog extends Dialog {
    private ListView mListView;

    public QAStandardDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_qa_standard);
        mListView = (ListView) findViewById(R.id.list_view);
        ImageView mCloseView = (ImageView) findViewById(R.id.close);
        setCanceledOnTouchOutside(false);
        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showDialog(ArrayList<Detail> details) {
        QuickAdapter<Detail> quickAdapter = new QuickAdapter<Detail>(getContext(), R.layout.item_dialog_qa_standard, details) {
            @Override
            protected void convert(BaseAdapterHelper helper, Detail item) {
                helper.setText(R.id.detail, item.detail_text);
            }
        };
        mListView.setAdapter(quickAdapter);
        show();
    }
}
