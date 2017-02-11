package com.hxqc.mall.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * liaoguilong
 * Created by CPR113 on 2016/5/31.
 * 修车 完成弹框
 */
public abstract class CompleteDialog extends Dialog implements View.OnClickListener{
    private Button cancelView,sendcommentView;
    private TextView contenView;
    public CompleteDialog(Context context,String content) {
        super(context,R.style.submitDIalog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView(context,content);
    }
    private void initView(Context context,String content){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_complete_order, null);
        contenView= (TextView) view.findViewById(R.id.content);
        cancelView= (Button)view.findViewById(R.id.cancel);
        sendcommentView= (Button)view.findViewById(R.id.sendcomment);
        contenView.setText(content);
        cancelView.setOnClickListener(this);
        sendcommentView.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sendcomment)
        {
            sendComment();
        }
        dismiss();
    }

    // 执行按下确定键之后的事情
    protected abstract void sendComment();
}
