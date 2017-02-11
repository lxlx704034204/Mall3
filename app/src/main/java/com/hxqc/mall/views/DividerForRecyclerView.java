package com.hxqc.mall.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hxqc.util.DisplayTools;

import hxqc.mall.R;

/**
 * 说明:RecyclerView的分割线
 *
 * author: 吕飞
 * since: 2015-03-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class DividerForRecyclerView extends RecyclerView.ItemDecoration {
    Context mContext;
    int mColor;
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DividerForRecyclerView(Context mContext) {
        this.mContext = mContext;
        mColor = mContext.getResources().getColor(R.color.divider);
    }

    public DividerForRecyclerView(Context mContext, int mColor) {
        this.mContext = mContext;
        this.mColor = mColor;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0, size = parent.getChildCount(); i < size; i++) {
            View child = parent.getChildAt(i);
            c.drawRect(child.getLeft(), child.getBottom() - DisplayTools.dip2px(mContext, 1), child.getRight(), child.getBottom(), mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
