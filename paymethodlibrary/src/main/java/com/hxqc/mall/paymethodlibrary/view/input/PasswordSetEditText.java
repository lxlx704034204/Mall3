//package com.hxqc.mall.paymethodlibrary.view.input;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.inputmethodservice.KeyboardView;
//import android.os.Build;
//import android.text.method.TransformationMethod;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.MotionEvent;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//
//import com.hxqc.mall.paymethodlibrary.R;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.ScreenUtil;
//
///**
// * Author: wanghao
// * Date: 2016-03-29
// * FIXME  设置密码对应 edit text
// * Todo
// */
//public class PasswordSetEditText extends  EditText{
//
//    /**
//     * 间隔
//     */
//    private final int PWD_SPACING = 0;
//
//    /**
//     * 密码大小
//     */
//    private final int PWD_SIZE = 5;
//    /**
//     * 密码长度
//     */
//    private final int PWD_LENGTH = 6;
//
//    /**
//     * 宽度
//     */
//    private int mWidth;
//    /**
//     * 高度
//     */
//    private int mHeight;
//
//    /**
//     * 密码画笔
//     */
//    private Paint mPwdPaint;
//    /**
//     * 输入的密码长度
//     */
//    private int mInputLength;
//
//
//    public PasswordSetEditText(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        // 初始化密码画笔
//        mPwdPaint = new Paint();
//        mPwdPaint.setColor(Color.BLACK);
//        mPwdPaint.setStyle(Paint.Style.FILL);
//        mPwdPaint.setAntiAlias(true);
//
//        setCursorVisible(true);
//
//    }
//
////    @Override
////    protected void onDraw(Canvas canvas) {
////
////        mWidth = getWidth();
////        mHeight = getHeight();
////
////        super.onDraw(canvas);
////
////        // 计算每个密码框宽度
////        int rectWidth = 20;
////        // 绘制密码
////        for (int i = 0; i < mInputLength; i++) {
////            int cx = rectWidth / 2 + (rectWidth + PWD_SPACING) * i;
////            int cy = mHeight / 2;
////            canvas.drawCircle(cx, cy, PWD_SIZE, mPwdPaint);
////        }
////    }
////
////    @Override
////    protected void onTextChanged(CharSequence text, int start,
////                                 int lengthBefore, int lengthAfter) {
////        super.onTextChanged(text, start, lengthBefore, lengthAfter);
////        this.mInputLength = text.toString().length();
////        invalidate();
////    }
//
//    KeyboardUtil keyboardUtil;
//    KeyboardView kbv;
//
//    public void setKeyboard(KeyboardView keyboardView) {
//        this.kbv = keyboardView;
//    }
//
//    public KeyboardUtil getKeyboardUtil(){
//        return this.keyboardUtil;
//    }
//
//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//
//        if (!focused) {
//            if (keyboardUtil != null) {
//                keyboardUtil.hideKeyboard();
//                keyboardUtil = null;
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        this.setFocusable(true);
//        this.setFocusableInTouchMode(true);
//
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
//        }
//
//        if (event.getAction() != MotionEvent.ACTION_HOVER_MOVE && event.getAction() != MotionEvent.ACTION_MOVE) {
//            if (kbv != null) {
//                keyboardUtil = new KeyboardUtil(kbv, getContext(), PasswordSetEditText.this);
//                keyboardUtil.showKeyboard();
//            }
//        }
//
//        return false;
//    }
//
//}
