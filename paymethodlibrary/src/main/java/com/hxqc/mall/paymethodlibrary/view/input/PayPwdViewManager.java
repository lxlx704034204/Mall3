package com.hxqc.mall.paymethodlibrary.view.input;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.paymethodlibrary.R;
import com.hxqc.util.OtherUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: wanghao
 * Date: 2016-03-17
 * FIXME
 * Todo
 */
public class PayPwdViewManager implements PayNumberKeyboard.OnKeyboardClickListener {

    Context ctx;

    View backView;
    RelativeLayout pwdFrameView;
    public PopupWindow popupWindow;
    EditText inputView;

    public PayPwdViewManager(Context context) {
        this.ctx = context;
        initPopupWindow();
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(ctx);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
    }


    public void dismiss() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    /**
     * 获取键盘按键回调
     *
     * @param n 按键数
     */
    @Override
    public void OnKeyClick(String n) {

        if (inputView == null)
            return;

        Editable editable = inputView.getText();
        int start = inputView.getSelectionStart();

        if (n.equals("backspace")) {

            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }

        } else if (n.equals("overLength")) {
        } else if (TextUtils.isEmpty(n)) {
        } else {
            if (isNum(n))
                editable.insert(start, n);
        }

    }

    /**
     * 判断是否为数字
     *
     * @param arg 参数
     * @return 返回值
     */
    public boolean isNum(String arg) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(arg);
        return isNum.matches();
    }

    /**
     * 余额支付输入
     *
     * @param relativeView 外层view
     */
    public void showBalanceNumberKeyboard(View relativeView, String money, final OnPwdFinishListener listener) {

        View contentView = View.inflate(ctx, R.layout.view_pop_pwd_input, null);
        backView = contentView.findViewById(R.id.bg_click_pwd);
        pwdFrameView = (RelativeLayout) contentView.findViewById(R.id.pwd_frame);

        PayNumberKeyboard keyboard = (PayNumberKeyboard) contentView.findViewById(R.id.keyboard);
        keyboard.setKeyboardClickListener(this);

        ImageView mCancel = (ImageView) contentView.findViewById(R.id.iv_dialog_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.inputCancel();
                }

                if (inputView != null) {
                    inputView.setText("");
                }

                dismiss();
            }
        });

        TextView mMoney = (TextView) contentView.findViewById(R.id.pwd_input_money);
        String depositM = "¥" + money;
        mMoney.setText(OtherUtil.amountFormat(money, true));

        PwdEditText mPwd = (PwdEditText) contentView.findViewById(R.id.pet_pwd);
        this.inputView = mPwd;
        mPwd.setInputType(InputType.TYPE_NULL);
        mPwd.setOnInputFinishListener(new PwdEditText.OnInputFinishListener() {
            @Override
            public void onInputFinish(String password) {
                if (listener != null) {
                    listener.pwdText(password);
                    if (inputView != null) {
                        inputView.setText("");
                    }
                    dismiss();
                }
            }
        });
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(relativeView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 只显示 数字键盘
     *
     * @param relativeView 页面最外层view
     */
    public void showNumberKeyboard(View relativeView, EditText et) {
        this.inputView = et;

        PayNumberKeyboard keyView = new PayNumberKeyboard(ctx);
        keyView.setFocusable(true);
        keyView.setKeyboardClickListener(this);
        popupWindow.setContentView(keyView);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.update();
        popupWindow.showAtLocation(relativeView, Gravity.BOTTOM, 0, 0);
    }

    public interface OnPwdFinishListener {
        void pwdText(String pwd);

        void inputCancel();
    }

}
