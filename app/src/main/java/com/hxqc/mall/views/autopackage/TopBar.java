package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2015-08-07
 * FIXME
 * Todo 一个tab切换控件
 */
public class TopBar extends View {
    private Paint mPaint;
    private float height;
    private float width;
    private int mColor = Color.parseColor("#F5f5f5");
    private int bColor = Color.parseColor("#f23435");

    private float textSize = converTextSize(14);
    private float radius = converTextSize(8);

    private int count = 3;//选项的数目
    private String[] texts = null;

    private float lineWidth = converTextSize(1);//线条的宽度
    private int checkedIndex = 0;//选中的角标

    private RectF midleRectF,//当只有一个选项的时候按钮的中间区域
            maxRectF, //当大于一个的时候按钮的最大区域
            startRoundRectF,//左边的画圆角区域
            endRoundRectF, //右边画圆角的区域
            startRectF, //当选择在第一项时填充区域
            endRectF;//当选择最后一项是填充区域

    public void setCheckedIndex(int checkedIndex) {
        this.checkedIndex = checkedIndex;
        invalidate();
    }

    public int getCheckedIndex() {
        return checkedIndex;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
        invalidate();
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }


    public void setTextSize(float textSize) {
        this.textSize = converTextSize(textSize);
        invalidate();
    }

    public void setRadius(float radius) {
        this.radius = converTextSize(radius);
        invalidate();
    }

    private OnSelectListener selectListener;


    public TopBar(Context context) {
        this(context, null);
    }


    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        textSize = typedArray.getDimension(R.styleable.TopBar_textSize, textSize);
        mColor = typedArray.getColor(R.styleable.TopBar_selectedBackgroundColor, mColor);
        bColor = typedArray.getColor(R.styleable.TopBar_backgroundColor, bColor);
        checkedIndex = typedArray.getInteger(R.styleable.TopBar_checkedIndex, checkedIndex);
        count = typedArray.getInteger(R.styleable.TopBar_count, count);
        float dimension = typedArray.getDimension(R.styleable.TopBar_radius, 8);
        radius = converTextSize(dimension);
        float dimension1 = typedArray.getDimension(R.styleable.TopBar_lineWidth, 1);
        lineWidth = converTextSize(dimension1);

        initData(count);
        typedArray.recycle();
    }

    /**
     * 初始化数据
     *
     * @param size
     */
    private void initData(int size) {
        texts = new String[size];
        for (int i = 0; i < size; i++) {
            String title = "选项" + (i + 1);
            texts[i] = title;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    /**
     * 基本初始化
     */
    private void init() {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        mPaint = new Paint();
        count = texts.length;
        midleRectF = new RectF(width / 3 - lineWidth, lineWidth, width * 2 / 3 - lineWidth, height - lineWidth);//当前为一个的时候
        maxRectF = new RectF(lineWidth, lineWidth, width - lineWidth, height - lineWidth);
        startRoundRectF = new RectF(lineWidth, lineWidth, 2 * radius - lineWidth, height - lineWidth);
        endRoundRectF = new RectF(width - 2 * radius + lineWidth, lineWidth, width - lineWidth, height - lineWidth);
        startRectF = new RectF(radius + lineWidth, lineWidth, width / count, height - lineWidth);
        endRectF = new RectF(width - width / count, lineWidth, width - radius - lineWidth, height - lineWidth);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (count == 0 || checkedIndex > count)
            return;
//        if (checkedIndex > count) {
//            throw new IndexOutOfBoundsException();
//        }

        float itemWidth = width / count;//每一个的宽度

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(lineWidth);
        //只有一个选项的时候
        if (count == 1) {
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(midleRectF, radius, radius, mPaint);
            drawTextColor(canvas, mPaint, bColor, 0);
            return;
        }
        //大于一个选项的时候
        canvas.drawRoundRect(maxRectF, radius, radius, mPaint);

        for (int i = 0; i < count; i++) {
            if (i == checkedIndex) {
                //话选中状态
                if (i == 0) {
                    //选中最左边的时候
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRoundRect(startRoundRectF, radius, radius, mPaint);
                    canvas.drawRect(startRectF, mPaint);
                } else if (i == count - 1) {
//                    选中最右边的时候
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRoundRect(endRoundRectF, radius, radius, mPaint);
                    canvas.drawRect(endRectF, mPaint);
                } else {
//                    选中其他的地方
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(i * itemWidth + lineWidth, lineWidth,
                            i * itemWidth + itemWidth + lineWidth, height - lineWidth, mPaint);
                }
//                写字
                drawTextColor(canvas, mPaint, bColor, i);
            } else {
//                写字（非选中状态的）
                drawTextColor(canvas, mPaint, mColor, i);
            }
            if (i != 0 && i != checkedIndex)
                canvas.drawLine(i * itemWidth, height - lineWidth, i * itemWidth, lineWidth, mPaint);
        }
        canvas.restore();
    }

    public void setOnSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    /**
     * 写字
     *
     * @param canvas
     * @param paint
     * @param color
     * @param position
     */
    private void drawTextColor(Canvas canvas, Paint paint, int color, int position) {
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        String text = getText(position);
        float wordWidth = paint.measureText(text);
        float v = width / count;
        float v0 = v / 2 - wordWidth / 2;//文字的margin
        float v1 = height / 2 + textSize / 2 - converTextSize(1.6f);
        paint.setColor(color);
        canvas.drawText(text, v0 + position * v, v1, paint);
    }

    /**
     * 字符长度设置
     *
     * @param position
     * @return
     */
    private String getText(int position) {
        if (texts[position].length() <= 5)
            return texts[position];
        else {
            String substring = texts[position].substring(0, 5);
            return substring + "...";
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            int i = (int) (x / width * count);
            setCheckedIndex(i);
            if (selectListener != null)
                selectListener.onSelect(this, i);
        }
        return true;
    }

    /**
     * 将sp转化为像素
     *
     * @param textSize
     * @return
     */
    private float converTextSize(float textSize) {
        Context c = getContext();
        Resources r;
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();
        float size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, textSize, r.getDisplayMetrics());
        return size;
    }

    public interface OnSelectListener {
        void onSelect(View view, int position);
    }
}
