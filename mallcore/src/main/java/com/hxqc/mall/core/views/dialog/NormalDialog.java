package com.hxqc.mall.core.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.anim.Techniques;
import com.hxqc.mall.core.anim.YoYo;
import com.hxqc.mall.core.anim.dialog.OptAnimationLoader;


/**
 * 说明:更新对话框
 *
 * author: 吕飞
 * since: 2015-03-24
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class NormalDialog extends Dialog implements View.OnClickListener {
    protected TextView mTitleView;//标题
    protected TextView mContentView;//内容
    public TextView mCancelView;//取消
    public TextView mOkView;//确定
    ViewGroup mRootView;
    protected View mDialogView;
    private AnimationSet mModalInAnim;

    public NormalDialog(Context context) {
        super(context, R.style.submitDIalog);
        initView();
    }

    public NormalDialog(Context context, String title, String content) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogTitle(title);
        setDialogContent(content);
    }
    public NormalDialog(Context context, String title, String content,String confirm) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogTitle(title);
        setDialogContent(content);
        mOkView.setText(confirm);
    }
    public NormalDialog(Context context, String content) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogContent(content);
    }

    public NormalDialog(Context context, String title, View contentView) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogTitle(title);
        setBodyView(contentView);
    }

    public void setBodyView(View contentView) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, R.id.content);
        ViewGroup view = (ViewGroup) findViewById(R.id.content_layout);
        view.addView(contentView, lp);
    }

    public void setDialogTitle(String title) {
        mTitleView.setText(title);
    }

    public void setDialogContent(String content) {
        mContentView.setText(content);
    }



    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_normal);
        mRootView = (ViewGroup) findViewById(R.id.root);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleView = (TextView) findViewById(R.id.title);
        mContentView = (TextView) findViewById(R.id.content_text);
        mCancelView = (TextView) findViewById(R.id.cancel);
        mOkView = (TextView) findViewById(R.id.ok);
        mCancelView.setOnClickListener(this);
        mOkView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel) {
            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(com.nineoldandroids.animation.Animator animator) {
                    dismiss();
                }
            }).playOn(mDialogView);

        } else if (i == R.id.ok) {
            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public void call(com.nineoldandroids.animation.Animator animator) {
                    dismiss();
                    doNext();
                }
            }).playOn(mDialogView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(mModalInAnim);
    }


    // 执行按下确定键之后的事情
    protected abstract void doNext();

}
