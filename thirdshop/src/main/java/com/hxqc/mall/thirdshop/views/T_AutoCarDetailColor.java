package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ColorInfo;
import com.hxqc.mall.thirdshop.views.adpter.ColorDeatailItemAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.GridViewNoSlide;

import java.util.ArrayList;

/**
 * Function: 车身color展示view
 *
 * @author 袁秉勇
 * @since 2015年12月02日
 */
public class T_AutoCarDetailColor extends LinearLayout {
    private final static String DEBUG = T_AutoCarDetailColor.class.getSimpleName();
    Context mContext;
    GridViewNoSlide mGridViewNoSlide;
    ArrayList<ColorInfo> colorInfos = new ArrayList<>();
    ColorDeatailItemAdapter colorDeatailItemAdapter;

    public T_AutoCarDetailColor(Context context) {
        this(context, null);
    }

    public T_AutoCarDetailColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.t_view_auto_color_show, this);
        mGridViewNoSlide = (GridViewNoSlide) findViewById(R.id.color_gridView);
    }

    /**
     * 初始化车身颜色数据
     */
    public void initData(ArrayList<ColorInfo> colorInfos) {
        this.colorInfos = colorInfos;
        colorDeatailItemAdapter = new ColorDeatailItemAdapter(mContext, colorInfos);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                int num = (int) (getWidth(mGridViewNoSlide) / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        40, getResources().getDisplayMetrics()));
                //    DebugLog.e("getWidth", getWidth(mGridViewNoSlide) + "");
                //   DebugLog.e("applyDimension",TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()) + "");
                //   DebugLog.e(getClass().getName(), num + "" + "measurWidth : " + mGridViewNoSlide.getMeasuredWidth() + "  " + mGridViewNoSlide.getWidth() + " " +  getWidth());
                mGridViewNoSlide.setNumColumns(num);
                mGridViewNoSlide.setAdapter(colorDeatailItemAdapter);
            }
        }, 50);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DebugLog.e(DEBUG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        DebugLog.e(DEBUG, "onLayout");
        DebugLog.e(DEBUG, mGridViewNoSlide.getWidth() + " " + mGridViewNoSlide.getMeasuredWidth());
    }

    /*
             * 获取控件宽
             */
    public int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }
}
