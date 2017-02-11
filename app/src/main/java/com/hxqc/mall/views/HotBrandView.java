package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.BrandGroup;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-06-17
 * FIXME
 * Todo  新车销售品牌 头
 */
@Deprecated
public class HotBrandView extends LinearLayout implements View.OnClickListener {
    LinearLayout mBrandLayout;

    OnBrandClickListener mOnBrandClickListener;

    public void setOnBrandClickListener(OnBrandClickListener onBrandClickListener) {
        this.mOnBrandClickListener = onBrandClickListener;
    }

    public interface OnBrandClickListener {
        void brandClick(HotBrandView hotBrandView, View rootView, View clickView, Brand brand);

    }

    public HotBrandView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_hot_brand, this);
        mBrandLayout = (LinearLayout) findViewById(R.id.hot_brand_layout);
        findViewById(R.id.filter_action).setOnClickListener(this);

    }

    public HotBrandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_hot_brand, this);
        mBrandLayout = (LinearLayout) findViewById(R.id.hot_brand_layout);
    }

    public void setBrandGroup(BrandGroup brandGroup) {
        if(brandGroup==null)return;
        int size = brandGroup.group.size();
        if (size == 0) {
            this.setVisibility(View.GONE);
        }
        if (size > 5) size = 5;
        Brand brand;
        HotBrandChildView brandChildView;
        for (int i = 0; i < size; i++) {
            brand = brandGroup.group.get(i);
            brandChildView = new HotBrandChildView(getContext()).setBrand(brand);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            brandChildView.setLayoutParams(params);
            brandChildView.setTag(R.id.brand_name, i);
            brandChildView.setTag(R.id.brand_thumb, brand);
            mBrandLayout.addView(brandChildView);
            brandChildView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBrandClickListener != null) {
                        mOnBrandClickListener.brandClick(HotBrandView.this, mBrandLayout, v,
                                (Brand) v.getTag(R.id.brand_thumb));

                    }
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_action:
                //按条件找车
                ActivitySwitcher.toAutoFilter(getContext(),"10");
                break;
        }
    }

    //    int grideWidth;//空格宽度
    public HotBrandAnimation mHotBrandAnimation;

    public HotBrandAnimation getmHotBrandAnimation(View rootView, View clickView, int blankWidth) {
        if(mHotBrandAnimation==null){
            mHotBrandAnimation = new HotBrandAnimation(rootView, clickView, blankWidth);
        }
        return mHotBrandAnimation;
    }

    public void setmHotBrandAnimation(HotBrandAnimation mHotBrandAnimation) {
        this.mHotBrandAnimation = mHotBrandAnimation;
    }

    public class HotBrandAnimation {
        View rootView;
        View childView;//车牌
        int childViewARG[] = new int[4];
        int index;
        int moveLoc;//移动距离
        int padding;//车牌paddin距离

        /**
         * @param rootView
         * @param clickView
         * @param blankWidth
         */
        public HotBrandAnimation(View rootView, View clickView, int blankWidth) {
            this.rootView = rootView;
            this.childView = ((HotBrandChildView) clickView).getmPriceView();
            this.index = (int) clickView.getTag(R.id.brand_name);
            int[] childLoc = new int[2];
            childView.getLocationInWindow(childLoc);
            childViewARG[0] = childLoc[0];//左
            childViewARG[1] = childView.getWidth();//宽
            //所点图标的右边界在屏幕的位置
            childViewARG[2] = rootView.getWidth() - childView.getWidth() - childViewARG[0];
            //计算所点击的图标移动到菜单距离屏幕边界的中间
            childViewARG[3] = moveLoc = blankWidth / 2 - (childViewARG[0] + childViewARG[1] / 2);
            padding = (blankWidth - childViewARG[1]) / 2;
        }


        public void openMenu(boolean rotation) {
            ObjectAnimator.ofFloat(rootView, "translationX", childViewARG[3]).setDuration(
                    300).start();
            if (rotation) {
                ObjectAnimator anim2 = ObjectAnimator.ofFloat(childView, "rotation", 0, -30, 0, 30,
                        0, -15, 0, 15, 0).setDuration(250);
                anim2.setStartDelay(300);
                anim2.start();
            }

        }

        /**
         * @param openRatio
         * @param offsetPixels
         *         菜单当前位置
         * @param toX
         *         移动距离
         */
        public void moveMenu(float openRatio, int offsetPixels, int toX) {
//            DebugLog.i("Tag", "moveLoc   " + moveLoc + "   " + (moveLoc - toX) + "   " + childViewARG[2] + "   " + offsetPixels);
            int moveX = moveLoc - toX;
            if (Math.abs(offsetPixels) < childViewARG[2] - padding) moveX = 0;//菜单位置在车牌右侧
            if (moveX > 0) moveX = 0;
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", moveLoc, moveX);
            ObjectAnimator.ofPropertyValuesHolder(rootView, pvhX).setDuration(200).start();
            moveLoc = moveX;
        }

        public void closeMenu() {
            DebugLog.e("Tag", "close Menu");
            ObjectAnimator.ofFloat(rootView, "x", 0).setDuration(600).start();
        }
    }
}
