package com.hxqc.autonews.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxqc.autonews.model.AutoInfoCommentModel;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.view.IView;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.util.ToastHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hxqc.mall.R;


/**
 * Author:李烽
 * Date:2016-10-18
 * FIXME
 * Todo 资讯详情底部横条（填写评论）
 */
public class WriteEvaluationPopWindow extends PopupWindow implements View.OnClickListener, IView<Error>, TextWatcher {
    private Activity activity;
    private String infoID;
    private final LinearLayout pop_layout;
    private final EditText inputEvaluation;
    private Presenter mPresenter;
    private AutoInfoCommentModel model;
    private String evaluationTxt;

    public WriteEvaluationPopWindow(Activity activity, String infoID) {
        this.infoID = infoID;
        this.activity = activity;
        mPresenter = new Presenter();
        model = new AutoInfoCommentModel(activity);
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.view_write_evaluation_popwindow, null);
        pop_layout = (LinearLayout) view.findViewById(R.id.poplayout);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.send).setOnClickListener(this);
        inputEvaluation = (EditText) view.findViewById(R.id.input_evaluation);
        inputEvaluation.addTextChangedListener(this);
//        int height = ScreenUtil.dip2px(activity, 195);
        int height = activity.getResources().getDimensionPixelOffset(R.dimen.write_evaluation_window_hight);
        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(height);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.WriteEvaluationPopWindow);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = pop_layout.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (y < height)
                        dismiss();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.send:
                send();
                break;
        }
    }

    private void send() {
        //发送评价
        if (canSend())
            mPresenter.sendAutoInfoComment(infoID, evaluationTxt, this, model);
    }

    private boolean canSend() {
        evaluationTxt = inputEvaluation.getText().toString();
        if (TextUtils.isEmpty(evaluationTxt)) {
            ToastHelper.showRedToast(activity, activity.getString(R.string.info_comment_empty));
            return false;
        }
        if (evaluationTxt.length() < 10) {
            ToastHelper.showRedToast(activity, activity.getString(R.string.info_comment_length));
            return false;
        }
        return true;
    }

    @Override
    public void onDataBack(Error data) {
        dismiss();
        inputEvaluation.setText("");
        ToastHelper.showGreenToast(activity, R.string.auto_info_comment_send_succed);
    }

    @Override
    public void onDataNull(String message) {
//        dismiss();
//        ToastHelper.showRedToast(activity, message);
    }

    private boolean resetText;
    private int cursorPos;
    private String inputAfterText = "";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!resetText) {
            cursorPos = inputEvaluation.getSelectionEnd();
            inputAfterText = s.toString();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!resetText) {
            try {
                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                if (isEmoji(input.toString())) {
                    resetText = true;
                    //是表情符号就将文本还原为输入表情符号之前的内容
                    inputEvaluation.setText(inputAfterText);
                    CharSequence text = inputEvaluation.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, spanText.length());
                    }
                }
            } catch (Exception e) {
            }
        } else {
            resetText = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 判断是否有输入法表情
     *
     * @param string
     * @return
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
