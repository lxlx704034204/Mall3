package com.hxqc.pay.util;//package com.hxqc.hxqcmall.pay.util;
//
//import android.app.Activity;
//import android.content.Context;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
///**
// * Author: wanghao
// * Date: 2015-03-13
// * FIXME
// * Todo
// */
//public class InputUtil {
//
//    /**
//     * 关闭输入框
//     * @param context
//     */
//    public static void closeInput(Context context) {
//        View view = ((Activity) context).getWindow().peekDecorView();
//        if (view != null) {
//            InputMethodManager inputManger = (InputMethodManager) context
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    /**
//     * 打开输入框
//     * @param view
//     * @param context
//     */
//    public static void showInput(View view,Context context) {
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
//    }
//
//
//
//}
