package com.hxqc.mall.thirdshop.utils;

import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by zhaofan
 */
public class OnScrollListenerHelper {
    private ListView lv;
    private int alpha = 0;

    public OnScrollListenerHelper(ListView lv) {
        this.lv = lv;
    }


    /**
     * @param hight     设置为0时  按第一个item的高度 设置透明度
     * @param mListener
     */
    public void setOnToolbarAppearListener(final int hight, final OnToolbarAppearListener mListener) {
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv.getChildAt(0) == null)
                    return;
                if (lv.getCount() > 1 && firstVisibleItem == 0) {
                    alpha = lv.getChildAt(0).getTop();

                    if (alpha > 0)
                        alpha = 0;
                    else {
                        int hight2 = hight != 0 ? hight : lv.getChildAt(0).getHeight();
                        alpha = (-alpha) * 255 / (hight2 == 0 ? 255 : hight2);
                    }

                    if (mListener != null)
                        mListener.OnToolbarAppear(alpha > 255 ? 255 : alpha);
                } else {
                    if (mListener != null) {
                        mListener.OnToolbarAppear(255);
                    }
                }
            }
        });
    }

    public interface OnToolbarAppearListener {
        void OnToolbarAppear(int alpha);  //alpha值 0 - 255

    }


}
