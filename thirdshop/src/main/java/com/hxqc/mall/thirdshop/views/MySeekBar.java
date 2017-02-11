package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
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
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年11月23日
 */
public class MySeekBar extends View {
    private final static String TAG = MySeekBar.class.getSimpleName();
    private Context mContext;

    private final static int[] DEFAULT_ATTRS = {android.R.attr.padding, android.R.attr.paddingLeft, android.R.attr.paddingTop, android.R.attr.paddingRight, android.R.attr.paddingBottom};

    private int seekBarHeight;
    private int thumbHeight;
    private int thumbWidth;
    private int marginBetweenTextAndSeekBar;
    private int scaleTextSize;

    private Bitmap thumb;

    private int padding;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int sectionNum;
    private ArrayList< String > sectionData = new ArrayList<>();

    private Paint mPaint;

    private int thumbScrollX = 0;

    private int totalLength;
    private int sectionLength;

    private int scaleTextTop;
    private boolean thumbScroll;

    private onSeekBarChangeListener onSeekBarChangeListener;


    public void setOnSeekBarChangeListener(MySeekBar.onSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }


    public MySeekBar(Context context) {
        this(context, null);
    }


    public MySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MySeekBar);
        seekBarHeight = a.getDimensionPixelSize(R.styleable.MySeekBar_seekBarHeight, seekBarHeight);
        marginBetweenTextAndSeekBar = a.getDimensionPixelSize(R.styleable.MySeekBar_marginBetweenTextAndSeekBar, marginBetweenTextAndSeekBar);
        scaleTextSize = a.getDimensionPixelSize(R.styleable.MySeekBar_scaleTextSize, scaleTextSize);
        thumb = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.MySeekBar_thumbRes, R.drawable.seekbar_thumb));

        thumbWidth = thumb.getWidth();
        thumbHeight = thumb.getHeight();

        scaleTextTop = paddingTop + thumbHeight + marginBetweenTextAndSeekBar;

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(scaleTextSize);

        this.post(new Runnable() {
            @Override
            public void run() {
                DebugLog.e("test", "post");


            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        DebugLog.e("test", "onFinishInflate");
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        DebugLog.e("test", "onWindowVisibilityChanged");
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        DebugLog.e("test", "onWindowFocusChanged");
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        DebugLog.e("test", "onAttachedToWindow");
    }


    private void initSeekBar() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        seekBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);
        marginBetweenTextAndSeekBar = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm);
        scaleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, dm);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DebugLog.e("test", "onMeasure");
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        DebugLog.e("test", "height : " + height);

        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        DebugLog.e("test", "onLayout");
        super.onLayout(changed, left, top, right, bottom);

        totalLength = getWidth() - paddingLeft - paddingRight - thumbWidth;

        sectionLength = totalLength / sectionNum;
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        DebugLog.e("test", " width : " + MeasureSpec.toString(measureSpec));

        if (specMode == MeasureSpec.AT_MOST) {

        } else if (specMode == MeasureSpec.EXACTLY) {

        }

        return specSize;
    }


    private int measureHeight(int measureHeight) {
        int specMode = MeasureSpec.getMode(measureHeight);
        int specSize = MeasureSpec.getSize(measureHeight);
        int result = 0;

        DebugLog.e("test", "height : " + MeasureSpec.toString(measureHeight));

        if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(specSize, getMinimumHeight());
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(specSize, getMinimumHeight());
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = getMinimumHeight();
        }
        return result;
    }


    @Override
    public int getMinimumHeight() {
        return paddingTop + thumbHeight + marginBetweenTextAndSeekBar + scaleTextSize + paddingBottom;
    }


    public void setSectionData(List< String > sectionData) {
        if (sectionData == null || sectionData.size() < 2) {
            throw new IllegalArgumentException("sectionNum must more than 2");
        }

        if (this.sectionData.size() > 0) this.sectionData.clear();
        this.sectionData.addAll(sectionData);

        sectionNum = sectionData.size() - 1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.GRAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(paddingLeft + thumbWidth / 2, (thumbHeight - seekBarHeight) / 2 + paddingTop, getWidth() - paddingRight - thumbWidth / 2, (thumbHeight + seekBarHeight) / 2 + paddingTop, 2, seekBarHeight / 2, mPaint);
        } else {
            canvas.drawRect(paddingLeft + thumbWidth / 2, (thumbHeight - seekBarHeight) / 2 + paddingTop, getWidth() - paddingRight - thumbWidth / 2, (thumbHeight + seekBarHeight) / 2 + paddingTop, mPaint);
        }

        mPaint.setColor(Color.RED);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(paddingLeft + thumbWidth / 2, (thumbHeight - seekBarHeight) / 2 + paddingTop, paddingLeft + thumbWidth / 2 + thumbScrollX, (thumbHeight + seekBarHeight) / 2 + paddingTop, 2, seekBarHeight / 2, mPaint);
        } else {
            canvas.drawRect(paddingLeft + thumbWidth / 2, (thumbHeight - seekBarHeight) / 2 + paddingTop, paddingLeft + thumbWidth / 2 + thumbScrollX, (thumbHeight + seekBarHeight) / 2 + paddingTop, mPaint);
        }

        Rect rect = new Rect(paddingLeft + thumbScrollX, paddingTop, paddingLeft + thumbScrollX + thumbWidth, paddingTop + thumbHeight);
        canvas.drawBitmap(thumb, null, rect, mPaint);

        drawText(canvas);
    }


    private int currentSection = 0;


    private void drawText(Canvas canvas) {
        for (int i = 0; i < sectionData.size(); i++) {
            int posX = sectionLength * i + paddingLeft + thumbWidth / 2;

            if (currentSection == i) {
                mPaint.setColor(Color.RED);
            } else {
                mPaint.setColor(Color.GRAY);
            }

            if (i == 0) {
                posX = paddingLeft + thumbWidth / 2;
            } else if (i == sectionData.size() - 1) {
                posX = getWidth() - paddingRight - thumbWidth / 2 - measureText(sectionData.get(i));
            } else {
                posX = posX - measureText(sectionData.get(i)) / 2;
            }

            canvas.drawText(sectionData.get(i), posX, scaleTextTop + scaleTextSize, mPaint);
        }
    }


    private int measureText(String string) {
        return (int) mPaint.measureText(string);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        DebugLog.e("test", "x : " + x + "  y: " + y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInControlPos(x, y)) {
                    thumbScroll = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (thumbScroll) {
                    thumbScrollX = x - paddingLeft - thumbWidth / 2 < 0 ? 0 : x > totalLength + paddingLeft + thumbWidth / 2 ? totalLength : x - paddingLeft - thumbWidth;

                    DebugLog.e("test", thumbScrollX + "");

                    currentSection = (int) (thumbScrollX / (sectionLength * 1.0f) + 0.5f);

                    thumbScrollX = currentSection * sectionLength;
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (thumbScroll) {
                    thumbScrollX = x - paddingLeft - thumbWidth / 2 < 0 ? 0 : x > totalLength + paddingLeft + thumbWidth / 2 ? totalLength : x - paddingLeft - thumbWidth;
                    DebugLog.e("test", thumbScrollX + "");

                    currentSection = (int) (thumbScrollX / (sectionLength * 1.0f) + 0.5f);

                    thumbScrollX = currentSection * sectionLength;
                }

                invalidate();
                thumbScroll = false;

                // seekBar滑动完成
                if (onSeekBarChangeListener != null) onSeekBarChangeListener.onProgressChanged(currentSection, sectionData.get(currentSection));
                break;
        }

        return true;
    }


    private boolean isInControlPos(int x, int y) {
        return y > paddingTop && y < paddingTop + thumbHeight;
    }


    public interface onSeekBarChangeListener {
        void onProgressChanged(int currentSection, String sectionText);
    }
}
