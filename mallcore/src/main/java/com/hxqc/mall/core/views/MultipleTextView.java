package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleTextView extends RelativeLayout {

    ListAdapter mAdapter;
    private int wordMargin;
    private int lineMargin;
    private int layout_width;
    private OnMultipleTVItemClickListener listener;

    public MultipleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.MultipleTextView);
        wordMargin = array.getDimensionPixelSize(
                R.styleable.MultipleTextView_textWordMargin, 0);
        lineMargin = array.getDimensionPixelSize(
                R.styleable.MultipleTextView_textLineMargin, 0);
        array.recycle();

    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layout_width = getMeasuredWidth();
        setTextViews();
    }

    protected void setTextViews() {
        if (mAdapter == null) {
            return;
        }
        int size = mAdapter.getCount();
        if (size == 0) return;
        // 每一行拉伸
        int line = 0;
        Map< Integer, List< View > > lineMap = new HashMap<>();
        lineMap.put(0, new ArrayList<View>());
        int x = 0;
        int y = 0;
        for (int i = 0; i < size; i++) {
            View view = mAdapter.getView(i, null, null);
            view.setTag(i);// 标记position
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onMultipleTVItemClick(v, (Integer) v.getTag());
                    }
                }
            });

            int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            int tvh = view.getMeasuredHeight();
            int tvw = getMeasuredWidth(view);

            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            if (x + tvw > layout_width) {
                x = 0;
                y = y + tvh + lineMargin;
                // 拉伸处理
                line++;
                lineMap.put(line, new ArrayList< View >());
            }
            lp.leftMargin = x;

            lp.topMargin = y;
            x = x + tvw + wordMargin;

            view.setLayoutParams(lp);
            // 拉伸处理
            lineMap.get(line).add(view);
        }
//        // 每一行拉伸
        for (int i = 0; i <= line; i++) {
//
//            // 该行最后一个位置
//            int len = lineMap.get(i).size();
//            View tView = lineMap.get(i).get(len - 1);
//
//            LayoutParams lp = (LayoutParams) tView.getLayoutParams();
//            int right = lp.leftMargin + getMeasuredWidth(tView);
//            int emptyWidth = layout_width - right;
//            int padding = emptyWidth / (len * 2);
//
//            int leftOffset = 0;
            for (int j = 0; j < lineMap.get(i).size(); j++) {
                View tView2 = lineMap.get(i).get(j);
//
//                LayoutParams lp2 = (LayoutParams) tView2.getLayoutParams();
//                lp2.leftMargin = lp2.leftMargin + leftOffset;
//                leftOffset = (j + 1) * 2 * padding;
//
//                tView2.setPadding(tView2.getPaddingLeft() + padding,
//                        tView2.getPaddingTop(), tView2.getPaddingRight()
//                                + padding, tView2.getPaddingBottom());
//
                addView(tView2);
            }
        }
    }

    public int getMeasuredWidth(View v) {
        return v.getMeasuredWidth();
    }

    public OnMultipleTVItemClickListener getOnMultipleTVItemClickListener() {
        return listener;
    }

    public void setOnMultipleTVItemClickListener(
            OnMultipleTVItemClickListener listener) {
        this.listener = listener;
    }

    public void setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void notifyData() {
        this.removeAllViews();
        setTextViews();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    public interface OnMultipleTVItemClickListener {
        void onMultipleTVItemClick(View view, int position);
    }
}
