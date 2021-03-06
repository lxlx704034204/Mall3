package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年01月04日
 */
public class ThirdPickupPoint extends LinearLayout {
    String mLabelString;
    int mTagDrawable;
    int mIconDrawable;

    protected ArrayList< PickupPointT > mPickupPoints;
    public TextView mTitleView;

    public ThirdPickupPoint(Context context) {
        super(context);
    }

    public ThirdPickupPoint(final Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoDetailTab);
        mLabelString = typedArray.getString(R.styleable.AutoDetailTab_tabLabel);
        mTagDrawable = typedArray.getResourceId(R.styleable.AutoDetailTab_tabTagDrawable, 0);
        mIconDrawable = typedArray.getResourceId(R.styleable.AutoDetailTab_tabIconDrawable, 0);
        typedArray.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_auto_detail_pickup_point, this);
        mTitleView = (TextView) findViewById(R.id.pickup_title);

        if (!TextUtils.isEmpty(mLabelString)) {
            TextView mLabelView = (TextView) findViewById(R.id.auto_tab_label);
            mLabelView.setText(mLabelString);
        }
    }

    public void setPickupPoints(ArrayList< PickupPointT > pickupPoints) {
        this.mPickupPoints = pickupPoints;
        if (pickupPoints.size() <= 0) {
            //无自提点
            return;
        }

        mTitleView.setText(pickupPoints.get(0).name);
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toAMapNvai(getContext(), 0, mPickupPoints.get(0));
            }
        });
    }

    public void setPickupPoint(PickupPointT pickupPoint) {
        mTitleView.setText(pickupPoint.name);
    }
}
