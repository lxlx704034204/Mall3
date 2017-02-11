package com.hxqc.autonews.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hxqc.autonews.adapter.AllCommentAdapter;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.fragments.AllCommentFragment;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hxqc.mall.R;

/**
 * 全部评价
 * Created by huangyi on 16/10/17.
 */
public class AllCommentActivity extends BackActivity implements View.OnClickListener, AllCommentAdapter.OnClickListener {

    public static final String INFO_ID = "info_id";
    public static final String COUNT = "count";
    public String infoID;
    public AutoInformationApiClient client;
    Dialog mDialog;
    EditText mEditView;
    String mPCommentID = "";

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);

        infoID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(INFO_ID);
        int count = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(COUNT);
        if (0 != count) getSupportActionBar().setTitle("全部评价 (" + count + ")");
        client = new AutoInformationApiClient();
        AllCommentFragment fragment = new AllCommentFragment();
        fragment.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.comment_list, fragment).commit();
        findViewById(R.id.comment_dialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.comment_dialog) {
            showDialog();

        } else if (id == R.id.comment_cancel && null != mDialog) {
            mDialog.dismiss();

        } else if (id == R.id.comment_send && null != mDialog) {
            final String content = mEditView.getText().toString();
            if (content.length() != 0) {
                if (content.length() < 10) {
                    //字数少了
                    ToastHelper.showYellowToast(AllCommentActivity.this, "评价长度不符");

                } else if (content.length() < 200) {
                    UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            send(content);
                        }
                    });

                } else {
                    //字数多了
                    ToastHelper.showYellowToast(AllCommentActivity.this, "评价长度不符");
                }

            } else {
                //空的
                ToastHelper.showYellowToast(AllCommentActivity.this, "评价长度不符");
            }
        }
    }

    @Override
    public void onReply(String pCommentID) {
        this.mPCommentID = pCommentID;
        showDialog();
    }

    @Override
    public void onLook(String pCommentID) {
        ActivitySwitchAutoInformation.toMessageCommentDetail(this, infoID, pCommentID);
    }

    private void showDialog() {
        if (null == mDialog) {
            View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_all_comment, null);
            mEditView = (EditText) mDialogView.findViewById(R.id.comment_edit);
            mEditView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (!resetText) {
                        cursorPos = mEditView.getSelectionEnd();
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
                                mEditView.setText(inputAfterText);
                                CharSequence text = mEditView.getText();
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
            });
            mDialogView.findViewById(R.id.comment_cancel).setOnClickListener(this);
            mDialogView.findViewById(R.id.comment_send).setOnClickListener(this);

            mDialog = new Dialog(this, R.style.FullWidthDialog);
            mDialog.setContentView(mDialogView);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mPCommentID = "";
                }
            });
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog宽高
            mDialog.getWindow().setGravity(Gravity.BOTTOM); //设置dialog显示的位置
        }

        mEditView.setText("");
        mDialog.show();
    }

    private void send(String content) {
        client.sendAutoInfoComment(infoID, mPCommentID, "", content, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(AllCommentActivity.this, "评价成功");
                mDialog.dismiss();
            }
        });
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
