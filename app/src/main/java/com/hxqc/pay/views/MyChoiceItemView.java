package com.hxqc.pay.views;//package com.hxqc.hxqcmall.pay.views;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.hxqc.hxqcmall.pay.R;
//
///**
// *
// * 选择样式的text view
// * Created by CPR062 on 2015/3/10.
// */
//
//
//public class MyChoiceItemView extends RelativeLayout {
//
//
//    TextView mGetPayMethod;
//    View mUnderline;
//
//
//    public MyChoiceItemView(Context context) {
//        super(context);
//        LayoutInflater.from(context).inflate( R.layout.mychoice_layout,this);
//        mGetPayMethod = (TextView) findViewById(R.id.tv_get_pay_method);
//        mUnderline = findViewById(R.id.v_underline);
//
//    }
//
//    public MyChoiceItemView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        LayoutInflater.from(context).inflate(R.layout.mychoice_layout,this);
//        mGetPayMethod = (TextView) findViewById(R.id.tv_get_pay_method);
//        mUnderline = findViewById(R.id.v_underline);
//
//    }
//
//    public void setValue(String pay_method) {
//        mGetPayMethod.setText(pay_method);
//    }
//
//    public void setViewColor(int color){
//        mUnderline.setBackgroundColor(color);
//    }
//
//
//
//
//    public void setClickListener(OnClickListener onClickListener) {
//        this.setOnClickListener(onClickListener);
//        mUnderline.setBackgroundColor(getResources().getColor(R.color.cursor_orange));
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//}
