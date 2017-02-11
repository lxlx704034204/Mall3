package com.hxqc.mall.core.views.Order;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.ColorSquare;

/**
 * 说明:用于订单的描述
 *
 * author: 吕飞
 * since: 2015-06-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderDescription extends RelativeLayout {
    BaseOrder mBaseOrder;
    Context mContext;
    ImageView mAutoImageView;
    TextView mAutoModelView;
    TextView mAutoPriceView;
    TextView mAutoNumView;
    TextView mInteriorTipText;
    RelativeLayout mDescriptionView;//整个展示区
    ColorSquare mAppearanceView;//车身颜色
    ColorSquare mInteriorView;//内饰颜色
    TextView mCaptchaView;//验证码
    boolean mIsUserCommentActivity=false;//是否是UserCommentActivity
    public OrderDescription(Context context) {
        super(context);
    }

    public OrderDescription(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_order_description, this);
        mAutoImageView = (ImageView) findViewById(R.id.auto_image);
        mAutoModelView = (TextView) findViewById(R.id.auto_model);
        mAutoPriceView = (TextView) findViewById(R.id.auto_price);
        mAutoNumView = (TextView) findViewById(R.id.auto_num);
        mAppearanceView = (ColorSquare) findViewById(R.id.color_square_appearance);
        mInteriorView = (ColorSquare) findViewById(R.id.color_square_interior);
        mDescriptionView = (RelativeLayout) findViewById(R.id.description);
        mInteriorTipText = (TextView) findViewById(R.id.interior_tip_text);
        mCaptchaView = (TextView) findViewById(R.id.auto_captcha);

    }

    public void initOrderDescription(Context context, BaseOrder mBaseOrder,String mCaptcha) {
        this.mContext = context;
        this.mBaseOrder = mBaseOrder;
        if(!TextUtils.isEmpty(mCaptcha)) {
            mCaptchaView.setVisibility(View.VISIBLE);
            mCaptchaView.setText("验证码："+mCaptcha);
        }else {
            mCaptchaView.setVisibility(View.GONE);
        }
        showOrderDescription();
    }


    public void initOrderDescription(Context context, BaseOrder mBaseOrder,boolean mIsUserCommentActivity) {
        this.mContext = context;
        this.mBaseOrder = mBaseOrder;
        this.mIsUserCommentActivity=mIsUserCommentActivity;
        showOrderDescription();
    }

    private void showOrderDescription() {
        if (mIsUserCommentActivity) {
            mDescriptionView.setBackgroundResource(R.color.white);
            mAutoModelView.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
        }
        ImageUtil.setImageNormalSize(mContext, mAutoImageView, mBaseOrder.itemThumb);
        mAutoModelView.setText(mBaseOrder.getItemName());
        mAutoPriceView.setText(OtherUtil.amountFormat(mBaseOrder.itemPrice,true));
        /**
         * @update  2015年10月30日 yuanbingyong
         * 不显示购买数量
         */
//        mAutoNumView.setText("×" + mBaseOrder.itemCount);
        mAppearanceView.setColors(mBaseOrder.getItemColor());
        if (mBaseOrder.getItemInterior().length != 0 && !TextUtils.isEmpty(mBaseOrder.getItemInterior()[0])) {
            mInteriorView.setColors(mBaseOrder.getItemInterior());
            mInteriorTipText.setVisibility(VISIBLE);
        } else {
            mInteriorTipText.setVisibility(GONE);
        }
    }

    /** 用于显示需要展示商品数量的情况 */
    public void showNumView() {
        mAutoNumView.setText("×" + mBaseOrder.itemCount);
    }
}
