//package com.hxqc.mall.paymethodlibrary.view.input;
//
//import android.content.Context;
//import android.text.InputType;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.RelativeLayout;
//
//import com.hxqc.mall.paymethodlibrary.R;
//import com.hxqc.mall.paymethodlibrary.adapter.PayInputAdapter;
//
///**
// * Author: wanghao
// * Date: 2016-03-29
// * FIXME  密码输入
// * Todo
// */
//public class PasswordInput extends RelativeLayout {
//
//    Context c;
//    public EditText editText;
//    View underline;
//    PayPwdViewManager manager;
//
//    public PasswordInput(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.c = context;
//        init();
//    }
//
//    void init() {
//        LayoutInflater.from(c).inflate(R.layout.view_password_input, this);
//        editText = (EditText) findViewById(R.id.pwd_input);
//        editText.setInputType(InputType.TYPE_NULL);
//        underline = findViewById(R.id.v_underline);
//        setUp();
//    }
//
//    public void setInitData(PayPwdViewManager manager, String hint) {
//        if (manager != null) {
//            this.manager = manager;
//        }
//
//        if (editText != null) {
//            editText.setHint(hint);
//        }
//    }
//
//    public String getText() {
//        if (editText != null) {
//            return editText.getText().toString();
//        } else {
//            return "";
//        }
//    }
//
//    void setUp() {
//        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                    if (manager != null) {
//                        manager.showNumberKeyboard(editText, editText);
//                    }
//
//                    underline.setBackgroundColor(getResources().getColor(R.color.main_orange_et));
//                } else {
//                    underline.setBackgroundColor(getResources().getColor(R.color.Gray_yjf));
//                }
//            }
//        });
//
//        editText.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (manager != null) {
//                    manager.showNumberKeyboard(editText, editText);
//                }
//            }
//        });
//    }
//}
