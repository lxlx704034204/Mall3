package com.hxqc.mall.core.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.adapter.ListDialogAdapter;
import com.hxqc.mall.core.anim.dialog.OptAnimationLoader;

/**
 * 说明:列表样式的dialog
 *
 * author: 吕飞
 * since: 2015-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class ListDialog extends Dialog implements AdapterView.OnItemClickListener {
    ListView mListView;//列表
    TextView mCancelView;//取消
    TextView mTitleView;//标题
    private View mDialogView;
    private AnimationSet mModalInAnim;

    // 拨电话的dialog
    public ListDialog(Context context, String title, String[] itemText) {
        super(context,R.style.submitDIalog);
        initView(title);
        ListDialogAdapter mListDialogAdapter = new ListDialogAdapter(context, itemText);
        showList(mListDialogAdapter);
    }

    // 自定义图标的dialog
    public ListDialog(Context context, String title, String[] itemText, int[] imgResId) throws Exception {
        super(context,R.style.submitDIalog);
        if (itemText.length == imgResId.length) {
            initView(title);
            ListDialogAdapter mListDialogAdapter = new ListDialogAdapter(context, itemText, imgResId);
            showList(mListDialogAdapter);
        }else {
            throw new Exception(context.getResources().getString(R.string.exception_array_length));
        }
    }

    private void showList(ListDialogAdapter mListDialogAdapter) {
        mListView.setAdapter(mListDialogAdapter);
        mListView.setOnItemClickListener(this);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView(String title) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mListView = (ListView) findViewById(R.id.list_view);
        mCancelView = (TextView) findViewById(R.id.cancel);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(mModalInAnim);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        doNext(position);
    }

    protected abstract void doNext(int position);
}
