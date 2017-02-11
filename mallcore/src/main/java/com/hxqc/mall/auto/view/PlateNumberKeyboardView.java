package com.hxqc.mall.auto.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.hxqc.mall.core.R;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 26
 * FIXME
 * Todo 新车牌号键盘样式
 */

public class PlateNumberKeyboardView extends KeyboardView {

    private Context mContext;
    private Keyboard mKeyBoard;
    private int mXmlLayoutResId = -1;
    private int mCityPrimaryCode;
    private String mCityCharacter;

    public PlateNumberKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setXmlLayoutResId(int xmlLayoutResId, String cityCharacter, int cityPrimaryCode) {
        this.mXmlLayoutResId = xmlLayoutResId;
        this.mCityPrimaryCode = cityPrimaryCode;
        this.mCityCharacter = cityCharacter;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mKeyBoard = this.getKeyboard();
        List<Key> keys = null;
        if (mKeyBoard != null) {
            keys = mKeyBoard.getKeys();
        }

        if (keys != null) {
            if (mXmlLayoutResId == R.xml.licence_new_letter) {
                drawKeyBackground(R.drawable.bg_platenumber_orange, canvas, keys.get(1));
                drawText(canvas, keys.get(1));
                drawKeyBackground(R.drawable.bg_platenumber_gray, canvas, keys.get(9));
                drawText(canvas, keys.get(9));
                drawKeyBackground(R.drawable.bg_platenumber_gray, canvas, keys.get(29));
                drawText(canvas, keys.get(29));
                /*for (Key key : keys) {
                    // TODO: 16/8/23  different key set the different background
                    if (key.codes[0] == 73 || key.codes[0]==Keyboard.KEYCODE_DELETE) {
                        drawKeyBackground(R.drawable.bg_platenumber_gray, canvas, key);
                        drawText(canvas, key);
                    }
                }*/
            } else if (mXmlLayoutResId == R.xml.licence_new_city) {
                for (Key key : keys) {
                    if (key.codes[0] == mCityPrimaryCode) {
                        drawKeyBackground(R.drawable.bg_platenumber_orange, canvas, key);
                        drawText(canvas, key);
                    }
                }
            }
        }
    }

    private void drawKeyBackground(int drawableId, Canvas canvas, Key key) {
        Drawable npd = mContext.getResources().getDrawable(
                drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            npd.setState(drawableState);
        }

        npd.setBounds(key.x, key.y + getPaddingTop(), key.x + key.width, key.y
                + key.height + getPaddingTop());
        npd.draw(canvas);
    }

    private void drawText(Canvas canvas, Key key) {
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setAntiAlias(true);

        paint.setColor(Color.WHITE);
        if (key.label != null) {
            String label = key.label.toString();

            Field field;

            if (label.length() > 1 && key.codes.length < 2) {
                int labelTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    labelTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(labelTextSize);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                int keyTextSize = 0;
                try {
                    field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                    field.setAccessible(true);
                    keyTextSize = (int) field.get(this);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                paint.setTextSize(keyTextSize);
                paint.setTypeface(Typeface.DEFAULT);
            }
            paint.getTextBounds(key.label.toString(), 0, key.label.toString()
                    .length(), bounds);
            canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                    (key.y + key.height / 2) + bounds.height() / 2 + getPaddingTop(), paint);
        } else if (key.icon != null) {
            key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + getPaddingTop(),
                    key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight() + getPaddingTop());
            key.icon.draw(canvas);
        }
    }
}
