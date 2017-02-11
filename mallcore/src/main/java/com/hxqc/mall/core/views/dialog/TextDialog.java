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
import com.hxqc.mall.core.anim.dialog.OptAnimationLoader;


/**
 * 提示信息文本对话框
 * 廖贵龙
 * 2016年12月6日 14:03:31
 */
public  class TextDialog extends Dialog {
    protected TextView mTitleView;//标题
    protected TextView mContentView;//内容
    protected View mDialogView;
    private AnimationSet mModalInAnim;

    public TextDialog(Context context, String content) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogContent(content);
    }

    public TextDialog(Context context, String title, String content) {
        super(context, R.style.submitDIalog);
        initView();
        setDialogTitle(title);
        setDialogContent(content);
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
        setContentView(R.layout.dialog_text);
        mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleView = (TextView) findViewById(R.id.title);
        mContentView = (TextView) findViewById(R.id.content_text);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(mModalInAnim);
    }

}
