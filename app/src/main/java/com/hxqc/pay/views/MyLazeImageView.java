package com.hxqc.pay.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hxqc.util.DebugLog;

/**
 * Author: liukechong
 * Date: 5/10/18
 */
@Deprecated
public class MyLazeImageView extends ImageView {
    private static final String TAG = "MyLazeImageView";

    public MyLazeImageView(Context context) {
        super(context);
    }

    public MyLazeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Paint paint;
    private float progressHeight = 0;

    private Bitmap errorBitmap;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        paint = new Paint();
        paint.setColor(0xaaaaaaaa);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (progressFlag) {
            drawProgressCover(canvas);
        }


    }

    /**
     * 画进度
     *
     * @param canvas
     */
    private void drawProgressCover(Canvas canvas) {
        DebugLog.d(TAG, "onDraw() returned: " + progressHeight);


        paint.setStrokeWidth(getWidth());
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() * (1 - progressHeight), paint);
        if (!errorFlag) {
            canvas.drawBitmap(errorBitmap, (getWidth() - errorBitmap.getWidth()) / 2, (getHeight() - errorBitmap.getHeight()) / 2, paint);
        }
    }

    private boolean errorFlag = true;
    private boolean progressFlag = false;

    /**
     * 上传进度
     *
     * @param progress 0~1
     */
    public synchronized void setProgress(float progress) {
        errorFlag = true;
        progressFlag = true;
        if (progress > 0.9f) {

        } else {
            progressHeight = progress;
//            DebugLog.d(TAG, "setProgress() called with: " + "progressHeight = [" + progressHeight + "]");
            invalidate();
        }


    }

    /**
     * 上传失败
     */
    public synchronized void setUploadError(Bitmap errorBitmap) {

        this.errorBitmap = errorBitmap;
        errorFlag = false;
        progressFlag = true;
        progressHeight = 0;
        invalidate();
    }

    /**
     * 上传成功,蒙板高度设置为1
     */
    public void setUploadSuccess() {
        progressHeight = 1;
        invalidate();
    }

    @Override
    public synchronized void setImageDrawable(Drawable drawable) {
        progressFlag = false;
        progressHeight= 0.9f;
        super.setImageDrawable(drawable);
    }
}
