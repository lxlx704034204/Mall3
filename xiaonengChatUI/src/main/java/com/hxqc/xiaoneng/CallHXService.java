//package com.hxqc.xiaoneng;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.xiaoneng.xnchatui.R;
//
//
///**
// * Author:胡俊杰
// * Date: 2016/1/23
// * FIXME
// * Todo
// */
//public class CallHXService extends LinearLayout {
//    public CallHXService(Context context) {
//        super(context);
//    }
//
//    public CallHXService(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.view_call_hx_servive, this);
//        findViewById(R.id.button).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent dialIntent = new Intent();
//                dialIntent.setAction(Intent.ACTION_DIAL);
//                dialIntent.setData(Uri.parse("tel:400-1868-555"));
//                getContext().startActivity(dialIntent);
//            }
//        });
//    }
//}
