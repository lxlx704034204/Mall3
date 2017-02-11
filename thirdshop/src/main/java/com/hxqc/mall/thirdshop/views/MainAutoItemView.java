package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.AutoBaseInfoThirdShop;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 主推车辆的列表item
 */
public class MainAutoItemView extends LinearLayout implements View.OnClickListener {
    private TextView title, normalPrice, guidePrice, autoType, testDrive, inquiry;
    private ImageView pic;
    private AutoBaseInfoThirdShop autoBaseInfoThirdShop;

    public void setAutoBaseInfoThirdShop(AutoBaseInfoThirdShop autoBaseInfoThirdShop) {
        this.autoBaseInfoThirdShop = autoBaseInfoThirdShop;
        initData();
    }

    public MainAutoItemView(Context context) {
        this(context, null);
    }

    public MainAutoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainAutoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_item_view_main_auto, this);
        title = (TextView) findViewById(R.id.main_auto_item_title);
        normalPrice = (TextView) findViewById(R.id.main_auto_item_normal_price);
        guidePrice = (TextView) findViewById(R.id.main_auto_item_guide_price);
        autoType = (TextView) findViewById(R.id.main_auto_item_auto_type);
        testDrive = (TextView) findViewById(R.id.main_auto_item_test_drive);
        inquiry = (TextView) findViewById(R.id.main_auto_item_inquiry);
        pic = (ImageView) findViewById(R.id.main_auto_item_pic);

        testDrive.setOnClickListener(this);
        inquiry.setOnClickListener(this);
        findViewById(R.id.root_view).setOnClickListener(this);

        initData();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.root_view) {
            if (null != onClickListener)
                onClickListener.onNormalClick();

        } else if (i == R.id.main_auto_item_test_drive) {
            if (null != onClickListener)
                onClickListener.onClickTestDrive();

        } else if (i == R.id.main_auto_item_inquiry) {
            if (null != onClickListener)
                onClickListener.onClickInquiry();

        }
    }

    public interface OnClickListener {
        void onClickTestDrive();

        void onClickInquiry();

        void onNormalClick();
    }

    private MainAutoItemView.OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void initData() {
        if (null != autoBaseInfoThirdShop) {
            title.setText(autoBaseInfoThirdShop.itemName);
            normalPrice.setText(String.format(getContext()
                            .getString(R.string.price_normal),
                    OtherUtil.amountFormat(autoBaseInfoThirdShop.itemPrice)));
            guidePrice.setText(String.format(getContext()
                            .getString(R.string.price_guide),
                    OtherUtil.amountFormat(autoBaseInfoThirdShop.itemOrigPrice)));
            autoType.setText(String.format(getContext()
                    .getString(R.string.auto_type), autoBaseInfoThirdShop.itemDescription));
            ImageUtil.setImage(getContext(), pic, autoBaseInfoThirdShop.itemThumb);
        }

    }
}
