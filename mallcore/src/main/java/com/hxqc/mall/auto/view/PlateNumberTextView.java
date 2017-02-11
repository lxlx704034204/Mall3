package com.hxqc.mall.auto.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.auto.adapter.CityInputAdapter;
import com.hxqc.mall.auto.adapter.NumberInputAbcAdapter;
import com.hxqc.mall.auto.adapter.NumberInputNumAdapter;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.util.DebugLog;

import java.lang.reflect.Field;

/**
 * Author :胡仲俊
 * Date : 2016-02-29
 * FIXME
 * Todo 车牌输入控件 默认字母输入框,setMode(1)城市输入框
 */
public class PlateNumberTextView extends EditTextValidatorView implements AdapterView.OnItemClickListener,
        View.OnFocusChangeListener {

    private static final String TAG = "PlateNumberTextView";

    /*两种键盘*/
    public static final int MODE_CITY = 1;
    public static final int MODE_WORD = 0;

    private StringBuilder result;
    private int mode = MODE_WORD;
    private PopupWindow popupWindow;

    public PlateNumberTextView(Context context) {
        this(context, null);

    }

    public PlateNumberTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setOnFocusChangeListener(this);
    }

    /**
     * 默认弹出车牌数字字母输入,设置setMote(1)弹城市弹框
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    private float mDownY;
    private boolean isAdd = true;
    /**
     *
     * @param isAdd
     */
    public void setState(boolean isAdd) {
        this.isAdd = isAdd;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DebugLog.i(TAG, "onTouchEvent1");
        if (isAdd) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    float dy = Math.abs((event.getY() - mDownY));
                    DebugLog.i(TAG, "dy:" + dy);
                    if (Math.abs(dy) > 20) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    } else {
//                    this.setFocusable(true);
//                    this.setFocusableInTouchMode(true);
//                    this.requestFocus();
//                    if (popupWindow != null) {
//                        popupWindow.dismiss();
//                    }
                        if (mode == MODE_CITY) {
                            showCityKeyboard();
                        } else {
                            showNumberKeyboard();
                        }
                        return true;
                    }
            }
        }

/*
        if (mode == MODE_CITY) {
            DebugLog.i(TAG, "showCityKeyboard onTouchEvent");
            showCityKeyboard();
        } else {
            DebugLog.i(TAG, "showNumberKeyboard onTouchEvent");
            showNumberKeyboard();
        }*/
        return false;
    }

    /**
     * 显示车牌字母数字输入
     */
    public void showNumberKeyboard() {
        if (!TextUtils.isEmpty(getText())) {
            setSelection(getText().length());
            result = new StringBuilder(getText());
        } else {
            result = new StringBuilder();
        }
        if (popupWindow == null) {
            popupWindow = new PopupWindow(getContext());
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            View view = View.inflate(getContext(), R.layout.view_keyboard_number, null);
            GridView number = (GridView) view.findViewById(R.id.number);
            GridView abc = (GridView) view.findViewById(R.id.abc);

            TextView next = (TextView) view.findViewById(R.id.next);
            ImageView back = (ImageView) view.findViewById(R.id.back);

            next.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                result = new StringBuilder(getText().toString());
                    if (result.length() > 0) {
                        result.delete(result.length() - 1, result.length());
                        setText(result);
                        if (result != null && result.length() > 0) {
                            setSelection(result.length());
                        }
                    }
                }
            });

            number.setAdapter(new NumberInputNumAdapter());
            abc.setAdapter(new NumberInputAbcAdapter());
            number.setOnItemClickListener(this);
            abc.setOnItemClickListener(this);
            popupWindow.setContentView(view);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //        popupWindow.update();
        }
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
        popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示车牌城市输入
     */
    public void showCityKeyboard() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(getContext());
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            View view = View.inflate(getContext(), R.layout.gridview_keyboard_city, null);
            GridView gridView = (GridView) view.findViewById(R.id.grid_view);
            gridView.setAdapter(new CityInputAdapter());
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                    setText((CharSequence) parent.getItemAtPosition(position));
                }
            });

     /*   //测量view的宽高
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        DebugLog.i(TAG, view.getMeasuredHeight() + "--------------");*/
            popupWindow.setContentView(view);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.update();
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.requestFocus();
        }

        popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        if (result.length() < getMaxLength()) {
            result.append(parent.getItemAtPosition(position));
            setText(result);
            DebugLog.i(TAG, "result:" + result.toString() + ",result.length():" + result.length());
            DebugLog.i(TAG, getMaxLength() + "------------");
            setSelection(result.length());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        DebugLog.i(TAG, "hasFocus:" + hasFocus);
        if (!hasFocus) {
            if (popupWindow != null) {
                DebugLog.i(TAG, "dismiss:");
                popupWindow.dismiss();
                popupWindow = null;
            }
              /*  this.setFocusable(false);
                this.setFocusableInTouchMode(false);
                this.requestFocus();*/
        } else {
            if (isAdd) {
                if (popupWindow == null) {
                    DebugLog.i(TAG, "show");
                    if (mode == MODE_CITY) {
                        DebugLog.i(TAG, "show1");
                        showCityKeyboard();
                    } else {
                        DebugLog.i(TAG, "show2");
                        showNumberKeyboard();
                    }
                }
            }
        }
    }

    public boolean popupWindowState() {
        boolean showing = false;
        if (popupWindow != null) {
            showing = popupWindow.isShowing();
        }
        return showing;
    }

    /**
     * 消除popup
     */
    public  void dismissPopup() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    /**
     * 获取文本长度
     *
     * @return
     */
    public int getMaxLength() {
        int length = 0;
        try {
            InputFilter[] inputFilters = getFilters();
            for (InputFilter filter : inputFilters) {
                Class< ? > c = filter.getClass();
                if (c.getName().equals("android.text.InputFilter$LengthFilter")) {
                    Field[] f = c.getDeclaredFields();
                    for (Field field : f) {
                        if (field.getName().equals("mMax")) {
                            field.setAccessible(true);
                            length = (Integer) field.get(filter);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

}
