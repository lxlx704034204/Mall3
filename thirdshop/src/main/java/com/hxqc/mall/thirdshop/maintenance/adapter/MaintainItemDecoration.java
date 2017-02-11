package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Author : 钟学东
 * @Since : 2016-11-02
 * FIXME
 * Todo RecyclerView item 间距
 */

public class MaintainItemDecoration extends RecyclerView.ItemDecoration {

    private int topSpace;
    private int bottomSpace;
    private int rightSpace;
    private int leftSpace;

    public MaintainItemDecoration(int topSpace,int bottomSpace,int rightSpace,int leftSpace) {
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
        this.rightSpace = rightSpace;
        this.leftSpace = leftSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;
        outRect.top = topSpace;
    }

}
