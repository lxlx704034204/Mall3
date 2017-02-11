package com.hxqc.mall.thirdshop.maintenance.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.util.DebugLog;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-03-08
 * FIXME
 * Todo 确认订单的订单详情View
 */
public class ConfirmOrderDetail extends LinearLayout {

    private TextView mItemNumView;
    private TextView mGoodsNumView;
    private ImageView mArrowView;
    private LinearLayout mParentView;
    private TextView mText4View;

    private Context context;

    private boolean isDown = true;

    private ValueAnimator animator;

    private int AnimeTime = 300;

    public ConfirmOrderDetail(Context context) {
        super(context);
        this.context = context;
    }

    public ConfirmOrderDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_confirm_order_detail, this);
        initView();
        initEvent();
    }


    public void seMaintenanceItemNtDate(ArrayList<MaintenanceItemGroup> maintenanceItemNs, String shopID, String flag) {
        mParentView.removeAllViews();

        for (int i = 0; i < maintenanceItemNs.size(); i++) {
            LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_first_layer_v5, mParentView);
            FourSShopMaintenanceFirstChildV5 firstChild = (FourSShopMaintenanceFirstChildV5) mParentView.getChildAt(i);
            firstChild.initDate(maintenanceItemNs.get(i), "2", i);

            mItemNumView.setText(maintenanceItemNs.size() + "");
            mGoodsNumView.setVisibility(View.GONE);
            mText4View.setVisibility(View.GONE);
        }

    }


    private void initEvent() {
        mArrowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDown) {
                    isDown = false;
                    mParentView.setVisibility(View.VISIBLE);

                    DebugLog.i("TAG", "mParentView.getHeight() : " + getTargetHeight(mParentView));

                    animator = slideAnimator(0, getTargetHeight(mParentView), mParentView);
                    animator.start();
                    mArrowView.setImageResource(R.drawable.maintain_arrow_up);
                } else {
                    isDown = true;

                    ValueAnimator mAnimator = slideAnimator(getTargetHeight(mParentView), 0, mParentView);
                    mAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mParentView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    mAnimator.start();

                    mArrowView.setImageResource(R.drawable.maintain_arrow_down);
                }
            }
        });
    }

    //测量view的高宽
    private int getTargetHeight(View v) {
        try {
            Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
                    int.class);
            m.setAccessible(true);
            m.invoke(v, MeasureSpec.makeMeasureSpec(
                    ((View) v.getParent()).getMeasuredWidth(),
                    MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED));
        } catch (Exception e) {

        }
        return v.getMeasuredHeight();
    }


    private ValueAnimator slideAnimator(int start, int end, final LinearLayout contentLayout) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(AnimeTime);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = contentLayout.getLayoutParams();
                layoutParams.height = value;

                contentLayout.setLayoutParams(layoutParams);
                contentLayout.invalidate();
                contentLayout.requestLayout();
            }
        });
        return animator;
    }

    private void initView() {
        mItemNumView = (TextView) findViewById(R.id.item_num);
        mGoodsNumView = (TextView) findViewById(R.id.goods_num);
        mArrowView = (ImageView) findViewById(R.id.iv_arrow);
        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
        mText4View = (TextView) findViewById(R.id.tv4);
    }

}
