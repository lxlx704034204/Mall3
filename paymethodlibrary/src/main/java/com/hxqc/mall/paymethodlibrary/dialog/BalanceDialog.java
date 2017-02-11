//package com.hxqc.hxqcmall.paymethodlibrary.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.hxqc.mall.paymethodlibrary.R;
//
//
///**
// * 余额支付弹框
// */
//public abstract class BalanceDialog extends Dialog implements View.OnClickListener {
//
//    TextView mTitle;
//    TextView mMoney;
//    ImageView mCancel;
//
//    onPasswordCompleteListener onPasswordCompleteListener;
//
//    interface onPasswordCompleteListener{
//        void  getInputPassword(String pwd);
//    }
//
//    public BalanceDialog(Context context) {
//        super(context, R.style.balanceDialog);
//        initView();
//    }
//
//    public BalanceDialog(Context context, String title) {
//        super(context, R.style.balanceDialog);
//        initView();
//    }
//
//    public BalanceDialog(Context context, String title, String money) {
//        super(context, R.style.balanceDialog);
//        initView();
//    }
//
//
//    private void initView() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.view_pop_pwd_input);
////        mRootView = (ViewGroup) findViewById(R.id.root);
////        mTitleView = (TextView) findViewById(R.id.title);
////        mContentView = (TextView) findViewById(R.id.content);
////        mCancelView = (TextView) findViewById(R.id.cancel);
////        mOkView = (TextView) findViewById(R.id.ok);
////        mCancelView.setOnClickListener(this);
////        mOkView.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
////        int i = v.getId();
////        if (i == R.id.cancel) {
////            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
////                @Override
////                public void call(com.nineoldandroids.animation.Animator animator) {
////                    dismiss();
////                }
////            }).playOn(mDialogView);
////
////        } else if (i == R.id.ok) {
////            YoYo.with(Techniques.ZoomOutDown).duration(375).onEnd(new YoYo.AnimatorCallback() {
////                @Override
////                public void call(com.nineoldandroids.animation.Animator animator) {
////                    dismiss();
////                    doNext();
////                }
////            }).playOn(mDialogView);
////        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
////        mDialogView.startAnimation(mModalInAnim);
//    }
//
//
//}
