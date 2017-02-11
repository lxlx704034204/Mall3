package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年09月30日
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {

    private int space;


    public MyItemDecoration(int space) {
        this.space = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //所有的格子都设置一个上边距
        outRect.top = space;
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为space，右边距设为1/2 space
        if (parent.getChildLayoutPosition(view) % 2 != 0) {
            outRect.right = space;
            outRect.left = space / 2;
        } else {
            outRect.right = space / 2;
            outRect.left = space;
        }
    }
}