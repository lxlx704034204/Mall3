package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: 自定义SeekBar
 *
 * @author 袁秉勇
 * @since 2016年11月18日
 */
public class DoubleSeekBar extends View {
    private final static String TAG = DoubleSeekBar.class.getSimpleName();
    private Context mContext;

    private final static int[] DEFAULT_ATTRS = {android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom};

    private int topThumbWidth; // 上滑块的宽度
    private int topThumbHeight; // 上滑块的高度
    private int bottomThumbHeight; // 下滑块的高度
    private int bottomThumbWidth; // 下滑块的宽度
    private int marginBetweenTextAndTopThumb; // 刻度字和上滑动块之间的间距
    private int scaleTextMinHeight; // 刻度字的最小高度
    private int marginBetweenTextAndSeekBarScaleLine; // 刻度字和SeekBar刻度线之间的间距
    private int seekBarScaleLineHeight; // 刻度线的高度
    private int seekBarMinHeight; // 滑动条的宽度
    private int marginBetweenSeekBarScaleLineAndBottomThumb; // SeekBar和底部滑动块之间的间距
    private boolean hasScaleText; // 是否有刻度数字;
    private Bitmap topThumb; // 上滑块
    private Bitmap bottomThumb; // 下滑块

    private Paint mPaint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int padding;

    private int offsetLeft;
    private int offsetRight;

    private int topScrollX;
    private int bottomScrollX;
    private boolean topThumbScroll; // 上滑块是否可以滑动
    private boolean bottomThumbScroll;  // 下滑块是否可以滑动
    private int bottomThumbsTop; // 下滑块的顶部距离控件顶部的高度
    private int seekBarTop; // 滑动条的top值
    private int scaleTextTop;
    private int topScaleLineTop;
    private int bottomScaleLineTop;

    private int sectionNum; // 等分的个数
    private int maxCurrentSection; // 当前最小等分值
    private int minCurrentSection; // 当前最大等分值
    private static final int LOWINFINITE = -2;
    private static final int HIGHTINFINITE = -1;

    private ArrayList< String > sectionText = new ArrayList<>();

    private OnSeekBarChangeListener onSeekBarChangeListener;


    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }


    public DoubleSeekBar(Context context) {
        this(context, null);
    }


    public DoubleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DoubleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initSeekBar();

        TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);
        if (defAttrs != null) {
            padding = defAttrs.getDimensionPixelSize(0, 0);
            paddingLeft = defAttrs.getDimensionPixelSize(1, 0);
            paddingTop = defAttrs.getDimensionPixelSize(2, 0);
            paddingRight = defAttrs.getDimensionPixelSize(3, 0);
            paddingBottom = defAttrs.getDimensionPixelSize(4, 0);

            if (padding > 0) {
                paddingLeft = paddingTop = paddingRight = paddingBottom = padding;
            }
        }
        defAttrs.recycle();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DoubleSeekBar);
        marginBetweenTextAndTopThumb = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_marginBetweenTextAndTopThumb, marginBetweenTextAndTopThumb);
        scaleTextMinHeight = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_scaleTextSize, scaleTextMinHeight);
        marginBetweenTextAndSeekBarScaleLine = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_marginBetweenTextAndSeekBarScaleLine, marginBetweenTextAndSeekBarScaleLine);
        seekBarScaleLineHeight = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_seekBarScaleLineHeight, seekBarScaleLineHeight);
        seekBarMinHeight = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_scaleTextMinHeight, seekBarMinHeight);
        marginBetweenSeekBarScaleLineAndBottomThumb = a.getDimensionPixelSize(R.styleable.DoubleSeekBar_marginBetweenSeekBarScaleLineAndBottomThumb, marginBetweenSeekBarScaleLineAndBottomThumb);
        topThumb = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.DoubleSeekBar_topThumbRes, R.drawable.top_thumb));
        bottomThumb = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.DoubleSeekBar_bottomThumbRes, R.drawable.bottom_thumb));

        sectionNum = a.getInt(R.styleable.DoubleSeekBar_sectionNum, -1);
        if (sectionNum > 0) {
            hasScaleText = true;
            sectionText = new ArrayList<>();
            for (int i = 0; i < sectionNum + 1; i++) {
                sectionText.add(i + "");
            }
        }

        topThumbWidth = topThumb.getWidth();
        topThumbHeight = topThumb.getHeight();
        bottomThumbWidth = bottomThumb.getWidth();
        bottomThumbHeight = bottomThumb.getHeight();

        mPaint.setTextSize(scaleTextMinHeight);

        a.recycle();

        this.post(new Runnable() {
            @Override
            public void run() {
                totalLength = getWidth() - paddingRight - topThumbWidth / 2 - paddingLeft - bottomThumbWidth / 2 - offsetLeft - offsetRight;

                if (sectionNum != sectionText.size() - 1) sectionNum = sectionText.size() - 1;

                sectionLength = totalLength / sectionNum;

                topScrollX = totalLength + offsetLeft + offsetRight;

                bottomScrollX = 0;

                minCurrentSection = LOWINFINITE;
                maxCurrentSection = HIGHTINFINITE;
            }
        });
    }


    public void setSectionText(List< String > sectionText) {

        DebugLog.e("test", "setSectionText");
        if (sectionText == null || sectionText.size() < 2) throw new IllegalArgumentException("sectionText's size must bigger than 1");
        if (this.sectionText.size() > 0) this.sectionText.clear();
        this.sectionText.addAll(sectionText);

        hasScaleText = true;
        initDistance();
    }


    private void initDistance() {
        seekBarTop = hasScaleText ? paddingTop + topThumbHeight + marginBetweenTextAndTopThumb + scaleTextMinHeight + marginBetweenTextAndSeekBarScaleLine + seekBarScaleLineHeight : paddingTop + topThumbHeight + marginBetweenSeekBarScaleLineAndBottomThumb;
        bottomThumbsTop = hasScaleText ? paddingTop + topThumbHeight + marginBetweenTextAndTopThumb + scaleTextMinHeight + marginBetweenTextAndSeekBarScaleLine + seekBarMinHeight + marginBetweenSeekBarScaleLineAndBottomThumb + 2 * seekBarScaleLineHeight : paddingTop + topThumbHeight + seekBarMinHeight + marginBetweenSeekBarScaleLineAndBottomThumb * 2;
        scaleTextTop = paddingTop + topThumbHeight + marginBetweenTextAndTopThumb;
        topScaleLineTop = scaleTextTop + scaleTextMinHeight + marginBetweenTextAndSeekBarScaleLine;
        bottomScaleLineTop = topScaleLineTop + seekBarScaleLineHeight + seekBarMinHeight;
        DebugLog.e("test", scaleTextMinHeight + " " + scaleTextTop + " " + marginBetweenTextAndSeekBarScaleLine + " " + topScaleLineTop + " " + topThumbHeight + " " + topThumbWidth);
    }


    private void initSeekBar() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        marginBetweenTextAndTopThumb = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
        scaleTextMinHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, dm);
        marginBetweenTextAndSeekBarScaleLine = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, dm);
        seekBarScaleLineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, dm);
        seekBarMinHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, dm);
        marginBetweenSeekBarScaleLineAndBottomThumb = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
        offsetLeft = offsetRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DebugLog.e("test", "onMeasure");
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(widthMeasureSpec);

        setMeasuredDimension(width, height);
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.AT_MOST) {

        } else if (specMode == MeasureSpec.EXACTLY) {

        }
        return specSize;
    }


    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            result = Math.max(specSize, getMinimumHeight());
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = Math.min(specSize, getMinimumHeight());
        }
        return result;
    }


    @Override
    public int getMinimumHeight() {
        if (hasScaleText) {
            return paddingTop + paddingBottom + topThumbHeight + marginBetweenTextAndTopThumb + scaleTextMinHeight + marginBetweenTextAndSeekBarScaleLine + 2 * seekBarScaleLineHeight + seekBarMinHeight + marginBetweenSeekBarScaleLineAndBottomThumb + bottomThumbHeight;
        } else {
            return paddingTop + paddingBottom + topThumbHeight + marginBetweenSeekBarScaleLineAndBottomThumb * 2 + bottomThumbHeight;
        }
    }


    @Override
    protected void onFinishInflate() {
        DebugLog.e("test", "onFinishInflate");
        super.onFinishInflate();
    }


    public void resetSeekBar() {
        maxCurrentSection = HIGHTINFINITE;
        minCurrentSection = LOWINFINITE;
        topScrollX = totalLength + offsetLeft + offsetRight;
        bottomScrollX = 0;

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画上滑块
        Rect rect = new Rect(topScrollX + paddingLeft, paddingTop, topScrollX + paddingLeft + topThumbWidth, paddingTop + topThumbHeight);
        canvas.drawBitmap(topThumb, null, rect, null);

        // 画刻度字和刻度线
        if (hasScaleText) {
            drawText(canvas);
        }

        // 画SeekBar背景
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(bottomThumbWidth / 2 + paddingLeft, seekBarTop, getWidth() - topThumbWidth / 2 - paddingRight, seekBarTop + seekBarMinHeight, mPaint);

        // 画SeekBar上层
        mPaint.setColor(Color.RED);
        canvas.drawRect(bottomScrollX + paddingLeft + bottomThumbWidth / 2, seekBarTop, topScrollX + paddingLeft + bottomThumbWidth / 2 + mPaint.getStrokeWidth(), seekBarTop + seekBarMinHeight, mPaint);

        // 画下滑块
        Rect rect1 = new Rect(bottomScrollX + paddingLeft, bottomThumbsTop, bottomScrollX + paddingLeft + bottomThumbWidth, bottomThumbsTop + bottomThumbHeight);
        canvas.drawBitmap(bottomThumb, null, rect1, null);

        mPaint.setColor(Color.WHITE);

        changedText();
        // 画下滑块中的文字
        canvas.drawText(bottomStr, bottomScrollX + paddingLeft + bottomThumbWidth / 2 - measureText(bottomStr) / 2, bottomThumbsTop + bottomThumbHeight / 2 + mPaint.getTextSize() / 2, mPaint);
        // 画上滑块中的文字
        canvas.drawText(topStr, topScrollX + paddingLeft + bottomThumbWidth / 2 - measureText(topStr) / 2, paddingTop + topThumbHeight / 2 + mPaint.getTextSize() / 3, mPaint);
    }


    private int sectionLength;
    private int totalLength;
    private String bottomStr;
    private String topStr;


    private void changedText() {
        if (minCurrentSection == LOWINFINITE) {
            bottomStr = "0";
        } else {
            bottomStr = sectionText.get(minCurrentSection);
        }

        if (maxCurrentSection == HIGHTINFINITE) {
            topStr = sectionText.get(sectionNum) + "+";
        } else {
            topStr = sectionText.get(maxCurrentSection);
        }
    }


    private void drawText(Canvas canvas) {
        sectionLength = totalLength / (sectionNum);

        for (int i = 0; i < sectionText.size(); i++) {
            int posLineX, posTextX;
            posLineX = paddingLeft + bottomThumbWidth / 2 + i * sectionLength + offsetLeft;
            posTextX = paddingLeft + bottomThumbWidth / 2 + i * sectionLength - measureText(sectionText.get(i)) / 2 + offsetLeft;

            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(scaleTextMinHeight);
            canvas.drawText(sectionText.get(i), posTextX, scaleTextTop + mPaint.getTextSize(), mPaint);

            if (posLineX < bottomScrollX + paddingLeft + bottomThumbWidth / 2 || posLineX > topScrollX + paddingLeft + bottomThumbWidth / 2) {
                mPaint.setColor(Color.GRAY);
            } else {
                mPaint.setColor(Color.RED);
            }

            if (i == sectionText.size() - 1) {
                posLineX = (int) (totalLength + paddingLeft + bottomThumbWidth / 2 + offsetLeft - mPaint.getStrokeWidth() * 3 / 2);
            } else {
                posLineX += mPaint.getStrokeWidth() / 2;

            }

            canvas.drawLine(posLineX, topScaleLineTop, posLineX, topScaleLineTop + seekBarScaleLineHeight, mPaint);
            canvas.drawLine(posLineX, bottomScaleLineTop, posLineX, bottomScaleLineTop + seekBarScaleLineHeight, mPaint);
        }
    }


    private int measureText(String text) {
        return (int) (mPaint.measureText(text) + 0.5f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        DebugLog.e(TAG, " x : " + x + "  y : " + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DebugLog.e(TAG, y + "");
                if (y - paddingTop < topThumbHeight) {
                    topThumbScroll = true;
                    DebugLog.e(TAG, "这是第一个");
                }

                if (y > bottomThumbsTop && y < bottomThumbsTop + bottomThumbHeight) {
                    bottomThumbScroll = true;
                    DebugLog.e(TAG, "这是第二个");
                }

                if (topThumbScroll || bottomThumbScroll) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (topThumbScroll) {
                    topScrollX = x > getWidth() - paddingRight - topThumbWidth / 2 ? getWidth() - paddingRight - topThumbWidth / 2 - paddingLeft - bottomThumbWidth / 2 : x - paddingLeft - bottomThumbWidth / 2;
                    if (topScrollX > sectionNum * sectionLength + offsetLeft) {
                        topScrollX = totalLength + paddingLeft + bottomThumbWidth / 2 + offsetLeft;
                        maxCurrentSection = HIGHTINFINITE;
                    } else {
                        maxCurrentSection = (int) (topScrollX * 1.0 / sectionLength + 0.5f) <= (minCurrentSection < 0 ? 0 : minCurrentSection) ? (minCurrentSection < 0 ? 1 : minCurrentSection + 1) : (int) (topScrollX * 1.0 / sectionLength + 0.5f);
                        topScrollX = maxCurrentSection * sectionLength + offsetLeft;
                    }
                    DebugLog.e("test", maxCurrentSection + "");
                    topScrollX = topScrollX < bottomScrollX + sectionLength ? bottomScrollX + sectionLength : topScrollX;
                }


                if (bottomThumbScroll) {
                    bottomScrollX = x < paddingLeft + bottomThumbWidth / 2 + offsetLeft ? 0 : x - paddingLeft - bottomThumbWidth / 2;
                    if (bottomScrollX == 0) {
                        DebugLog.e(TAG, " text ==========================");
                        bottomScrollX = 0;
                        minCurrentSection = LOWINFINITE;
                    } else {
                        minCurrentSection = (int) ((bottomScrollX - offsetLeft) * 1.0 / sectionLength) >= (maxCurrentSection < 0 ? sectionNum : maxCurrentSection) ? ((maxCurrentSection < 0) ? sectionNum - 1 : maxCurrentSection - 1) : (int) ((bottomScrollX - offsetLeft) * 1.0 / sectionLength);
                        bottomScrollX = minCurrentSection * sectionLength + offsetLeft;
                    }
                    DebugLog.e("test", minCurrentSection + "  " + bottomScrollX);
                    bottomScrollX = topScrollX - sectionLength < bottomScrollX ? topScrollX - sectionLength : bottomScrollX;
                }

                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                topThumbScroll = bottomThumbScroll = false;

                if (minCurrentSection == LOWINFINITE) {
                    bottomStr = "0";
                } else {
                    bottomStr = sectionText.get(minCurrentSection);
                }

                if (maxCurrentSection == HIGHTINFINITE) {
                    topStr = sectionText.get(sectionNum) + "+";
                } else {
                    topStr = sectionText.get(maxCurrentSection);
                }
                if (onSeekBarChangeListener != null) onSeekBarChangeListener.onProgressChanged(minCurrentSection, bottomStr, maxCurrentSection, topStr);
                break;
        }
        return true;
    }


    public interface OnSeekBarChangeListener {
        void onProgressChanged(int minCurrentSection, String leftText, int maxCurrentSection, String rightText);
    }
}
