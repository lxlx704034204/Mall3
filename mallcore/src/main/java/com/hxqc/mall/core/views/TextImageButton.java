package com.hxqc.mall.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hxqc.mall.core.R;

/**
 * 说明:图文按钮
 *
 * author: 吕飞
 * since: 2015-03-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class TextImageButton extends RelativeLayout {

    private ImageView mImageView;
    private TextView mTextView;

    public TextImageButton(Context context) {
        super(context);
    }

    public TextImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextImageButton);
//        int marginLeft = (int)ta.getDimension(R.styleable.TextImageButton_marginLeft, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_text_image_button, this);
        mImageView = (ImageView) findViewById(R.id.btn_image);
        mTextView = (TextView) findViewById(R.id.btn_text);
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(marginLeft, 0, 0, 0);
//        mImageView.setLayoutParams(params);
    }

    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        mImageView.setImageResource(resId);
    }

    /**
     * 设置显示的文字
     */
    public void setTextViewText(String text) {
        mTextView.setText(text);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
