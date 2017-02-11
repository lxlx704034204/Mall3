package com.hxqc.mall.photolibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class PSquareImage extends ImageView {

	private static final int COLORDRAWABLE_DIMENSION = 2;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	int viewHeight;
	int viewWidth;
	boolean isResource = false;

	public PSquareImage(Context context) {
		super(context);
		init();
	}

	public PSquareImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getMeasuredHeight() != getMeasuredWidth()) {
			setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
		}
		viewWidth = getMeasuredWidth();
		viewHeight = getMeasuredHeight();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		isResource = false;
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		isResource = false;
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		isResource = true;
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		isResource = false;
	}

	private void init() {
		super.setScaleType(ScaleType.CENTER);
	}


	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		try {
			Bitmap bitmap;
			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isResource)
			updateShaderMatrix();
		super.onDraw(canvas);
	}


	private void updateShaderMatrix() {
		Bitmap mBitmap = getBitmapFromDrawable(getDrawable());
		if (mBitmap == null) return;
		int mBitmapWidth = mBitmap.getHeight();
		int mBitmapHeight = mBitmap.getWidth();
		float scale;
		Matrix mShaderMatrix = getImageMatrix();
		float heightScale = viewHeight / (float) mBitmapHeight;
		float widthScale = viewWidth / (float) mBitmapWidth;
		if (heightScale < widthScale) {
			scale = widthScale;
		} else {
			scale = heightScale;
		}
		mShaderMatrix.setScale(scale, scale);
		setImageMatrix(mShaderMatrix);

	}
}
