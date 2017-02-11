package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;

/**
 * Author: 胡俊杰
 * Date: 2015-12-21
 * FIXME
 * Todo
 */
public class ThirdFunctionButton extends RelativeLayout {


    private String innerText;
    private float textSize;
    private Drawable icon;
    private Drawable tfBackground;

    private RelativeLayout mBackgroundView;
    private ImageView mIconView;
    private TextView mButtonText;


    public ThirdFunctionButton(Context context) {
        super(context);
        initViews();
    }

    public ThirdFunctionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TSFunctionButton);
        innerText = typedArray.getString(R.styleable.TSFunctionButton_innerButtonText);
        textSize = typedArray.getDimension(R.styleable.TSFunctionButton_innerTextSize, 12f);
        icon = typedArray.getDrawable(R.styleable.TSFunctionButton_innerIcon);
        tfBackground = typedArray.getDrawable(R.styleable.TSFunctionButton_backgroundTF);
        setXMLData();
        typedArray.recycle();
    }

    public void setInnerText(String innerText) {
        this.innerText = innerText;
        setXMLData();
    }

//    public void setTextSize(float textSize) {
//        this.textSize = textSize;
//        setXMLData();
//    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        setXMLData();
    }

    public void setmBackgroundView(RelativeLayout mBackgroundView) {
        this.mBackgroundView = mBackgroundView;
        setXMLData();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.t_function_button, this);
        mBackgroundView = (RelativeLayout) findViewById(R.id.t_function_background);
        mIconView = (ImageView) findViewById(R.id.iv_t_function_icon);
        mButtonText = (TextView) findViewById(R.id.tv_t_function_text);
    }

    private void setXMLData() {
        mBackgroundView.setBackgroundDrawable(tfBackground);
        mIconView.setBackgroundDrawable(icon);
        mButtonText.setText(innerText);
        mButtonText.setTextSize(0,textSize);
    }
}
