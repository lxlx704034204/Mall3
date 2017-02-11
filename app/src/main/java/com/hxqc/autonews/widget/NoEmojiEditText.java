package com.hxqc.autonews.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:李烽
 * Date:2016-12-20
 * FIXME
 * Todo 禁止输入emoji表情（效果有问题，暂不使用，原因待查）
 */
@Deprecated
public class NoEmojiEditText extends EditText {
    private boolean resetText;
    private int cursorPos;
    private NoEmojiEditText thiz;
    private String inputAfterText;

    public NoEmojiEditText(Context context) {
        this(context, null);
    }

    public NoEmojiEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoEmojiEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        thiz = this;
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                watcher.beforeTextChanged(s, start, count, after);
                if (!resetText) {
                    cursorPos = thiz.getSelectionEnd();
                    inputAfterText = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                watcher.onTextChanged(s, start, before, count);
                if (!resetText) {
                    try {
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (isEmoji(input.toString())) {
                            resetText = true;
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            thiz.setText(inputAfterText);
                            CharSequence text = thiz.getText();
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
                watcher.afterTextChanged(s);
            }
        });
    }

    private TextWatcher watcher;

    public void addTextChangedListener(TextWatcher watcher) {
        this.watcher = watcher;
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
