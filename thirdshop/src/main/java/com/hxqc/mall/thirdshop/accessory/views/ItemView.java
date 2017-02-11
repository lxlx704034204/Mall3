package com.hxqc.mall.thirdshop.accessory.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.ScreenUtil;

/**
 * 固定样式的Item
 * Created by huangyi on 16/2/25.
 */
public class ItemView extends LinearLayout {

    TextView mItemName;

    TextView mItemValue;
    int mItemValueColor;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        Drawable mItemIcon = mTypedArray.getDrawable(R.styleable.ItemView_itemIcon);
        String mItemNameStr = mTypedArray.getString(R.styleable.ItemView_itemNameStr);
        float mItemNameSize = mTypedArray.getDimension(R.styleable.ItemView_itemNameSize, 0);
        int mItemNameColor = mTypedArray.getColor(R.styleable.ItemView_itemNameColor, getResources().getColor(R.color.cursor_orange));
        String mItemValueStr = mTypedArray.getString(R.styleable.ItemView_itemValueStr);
        float mItemValueSize = mTypedArray.getDimension(R.styleable.ItemView_itemValueSize, 0);
        mItemValueColor = mTypedArray.getColor(R.styleable.ItemView_itemValueColor, getResources().getColor(R.color.cursor_orange));
        Drawable mItemArrow = mTypedArray.getDrawable(R.styleable.ItemView_itemArrow);
        mTypedArray.recycle();

        int px16 = ScreenUtil.dip2px(context, 16);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(px16, ScreenUtil.dip2px(context, 12), px16, ScreenUtil.dip2px(context, 12));

        if (null != mItemIcon) {
            ImageView icon = new ImageView(context);
            icon.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.dip2px(context, 32), ScreenUtil.dip2px(context, 32)));
            icon.setPadding(0, 0, px16, 0);
            icon.setImageDrawable(mItemIcon);
            addView(icon);
        }

        mItemName = new TextView(context);
        mItemName.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
        mItemName.setText(mItemNameStr);
        if (mItemNameSize == 0) {
            mItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } else {
            mItemName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemNameSize);
        }
        mItemName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mItemName.setTextColor(mItemNameColor);
        addView(mItemName);

        mItemValue = new TextView(context);
        mItemValue.setPadding(px16, 0, 0, 0);
        mItemValue.setText(mItemValueStr);
        mItemValue.setMaxWidth(ScreenUtil.getScreenWidth(context) /2);
        mItemValue.setEllipsize(TextUtils.TruncateAt.END);
        mItemValue.setSingleLine();
        if (mItemValueSize == 0) {
            mItemValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            mItemValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemValueSize);
        }
        addView(mItemValue);

        if (null != mItemArrow) {
            ImageView arrow = new ImageView(context);
            arrow.setPadding(px16, 0, 0, 0);
            arrow.setImageDrawable(mItemArrow);
            addView(arrow);
        }
    }

    public void setName(String name) {
        mItemName.setText(name);
    }

    public void setValue(String value) {
        mItemValue.setText(value);
        mItemValue.setTextColor(mItemValueColor);
    }

    public void setValue() {
        mItemValue.setText("不限");
        mItemValue.setTextColor(getResources().getColor(R.color.text_color_subheading));
    }

}
