//package com.hxqc.mall.views.thirdpartshop;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import hxqc.mall.R;
//import com.hxqc.mall.core.model.auto.PickupPointT;
//
//import java.util.ArrayList;
//
///**
// * Function:
// *
// * @author 袁秉勇
// * @since 2015年12月09日
// */
//@Deprecated
//public class ThirdAutoDetailPickupPoint extends LinearLayout {
//    ArrayList<PickupPointT> mPickupPoints;
//    TextView mTitleView;
//
//    public ThirdAutoDetailPickupPoint(Context context) {
//        super(context);
//    }
//
//    public ThirdAutoDetailPickupPoint(final Context context, AttributeSet attrs) {
//        super(context, attrs);
//        LayoutInflater.from(context).inflate(R.layout.view_auto_detail_pickup_point, this);
//        mTitleView = (TextView) findViewById(R.id.pickup_title);
//
//    }
//
//    public void setPickupPoints(ArrayList<PickupPointT> pickupPoints) {
//        this.mPickupPoints = pickupPoints;
//        if (pickupPoints.size() <= 0) {
//            //无自提点
//            return;
//        }
//
//        mTitleView.setText(pickupPoints.get(0).address);
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivitySwitcherThirdPartShop.toPickupPoint(mPickupPoints, getContext());
//            }
//        });
//    }
//    public void setPickupPoint(PickupPointT pickupPoint) {
//        mTitleView.setText(pickupPoint.name);
//    }
//}
