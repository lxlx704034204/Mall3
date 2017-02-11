package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


import com.hxqc.mall.thirdshop.R;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月22日
 */
public class HeaderHideOrShowBehavior extends CoordinatorLayout.Behavior {
    private Context mContext;
    private int target;
    private View tipFragmentView;
    private TextView tipTestView;

    public HeaderHideOrShowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            if (typedArray.getIndex(i) == R.styleable.TipView_show) {
                target = typedArray.getResourceId(typedArray.getIndex(i), -1);
            }
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == target;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if (coordinatorLayout.getTop() > 0) {
            ((PtrFrameLayout)coordinatorLayout.getParent()).refreshComplete();
            coordinatorLayout.requestFocus();
        }
        if (tipTestView == null) tipTestView = (TextView) child.findViewById(R.id.tip_car_name);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 1;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 || dyUnconsumed > 0) {
            if (isCanScollVertically((RecyclerView) target)) {
                if (child.getVisibility() == View.GONE && tipTestView!= null && !TextUtils.isEmpty(tipTestView.getText())) child.setVisibility(View.VISIBLE);
            }
        }
        if (tipFragmentView == null) tipFragmentView = coordinatorLayout.findViewById(R.id.tip_fragment);
        if (tipFragmentView.getVisibility() == View.VISIBLE) tipFragmentView.setVisibility(View.GONE);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY > 0 ) {
            if (isCanScollVertically((RecyclerView) target)) {
                if (child.getVisibility() == View.GONE && tipTestView!= null && !TextUtils.isEmpty(tipTestView.getText())) child.setVisibility(View.VISIBLE);
            }
        }
        if (tipFragmentView == null) tipFragmentView = coordinatorLayout.findViewById(R.id.tip_fragment);
        if (tipFragmentView.getVisibility() == View.VISIBLE) tipFragmentView.setVisibility(View.GONE);
        return  true;
    }

    private boolean isCanScollVertically(RecyclerView recyclerView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
        } else {
            return ViewCompat.canScrollVertically(recyclerView, 1);
        }
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        Log.e(getClass().getName(), "-------- onDependentViewChanged --------");
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
