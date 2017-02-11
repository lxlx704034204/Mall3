package com.hxqc.mall.auto.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.dao.PACDao;
import com.hxqc.mall.auto.model.PAC;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.tech.PlateNumberRegexpValidator;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 22
 * Des: 新车牌号键盘控件
 * FIXME
 * Todo
 */

public class NewPlateNumberLayout extends LinearLayout {

    private static final String TAG = AutoInfoContants.LOG_J;
    private EditTextValidatorView mCityEditText;
    private EditTextValidatorView mNumberEditText;

    private Keyboard mKeyboard;
    private PlateNumberKeyboardView mKeyboardView;

    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private Context mContext;

    public PopupWindow mKeyboardWindow;

    private boolean isNeedCustomKeyboard = true; // 是否启用自定义键盘
    /**
     * 输入框在被键盘弹出时，要被推上的距离
     */
    private int mScrollDistance = 0;

    public static int screenWidth = -1; // 未知宽高
    public static int screenHeight = -1;

    /**
     * 不包含导航栏的高度
     */
    public static int screenHeightNoNavBar = -1;
    /**
     * 实际内容高度，  计算公式:屏幕高度-导航栏高度-电量栏高度
     */
    public static int screenContentHeight = -1;
    public static float density = 1.0f;
    public static int densityDpi = 160;
    private int mXmlLayoutResId = -1;
    private View mNextView;
    private final int textSize;

    private String mCityCharacter;
    private int mCityPrimaryCode;
    private boolean isIntercept = false;
    private View rootView;

    public NewPlateNumberLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        DebugLog.i(TAG, "NewPlateNumberLayout");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewPlateNumberLayout);
        textSize = typedArray.getDimensionPixelSize(R.styleable.NewPlateNumberLayout_layout_new_plate_number_textsize, getResources().getDimensionPixelSize(R.dimen.text_size_16));

        mContext = context;
        initView(context);
        initAttributes(context);
        initEvent();

        typedArray.recycle();
    }

    public void initEvent() {
        mCityEditText.setOnTouchListener(touchListener);
        mNumberEditText.setOnTouchListener(touchListener);
    }

    public void initTextChangedListener() {
        mCityEditText.addTextChangedListener(cityChangeListener);
        mNumberEditText.addTextChangedListener(numberChangeListener);
    }

    public void removeTextChangedListener() {
        if (cityChangeListener != null) {
            mCityEditText.removeTextChangedListener(cityChangeListener);
        }
        if (numberChangeListener != null) {
            mNumberEditText.removeTextChangedListener(numberChangeListener);
        }
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.layout_plate_number_v2, this);

        mCityEditText = (EditTextValidatorView) findViewById(R.id.layout_license_city);
        mNumberEditText = (EditTextValidatorView) findViewById(R.id.layout_license_num);
//        addPlateNumberValidator(mCityEditText.getText().toString());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        mCityEditText.setTextSize(DisplayTools.px2sp(context, textSize));
        mNumberEditText.setTextSize(DisplayTools.px2sp(context, textSize));

    }

    /**
     * @param context
     */
    private void initAttributes(Context context) {
        initScreenParams(context);
        mCityEditText.setLongClickable(false); // 设置EditText不可长按
        mCityEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mNumberEditText.setLongClickable(false); // 设置EditText不可长按
        mNumberEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        removeCopyAbility();

        Point point = getNavigationBarSize(context);
        DebugLog.i(TAG, point.x + "--------" + point.y);

        /*if (this.getText() != null) {
            this.setSelection(this.getText().length());
        }*/

    }

    /**
     * @param xmlLayoutResId
     */
    public void initKeyboard(int xmlLayoutResId) {
        if (mXmlLayoutResId != xmlLayoutResId) {

            hideKeyboard();
            mKeyboard = new Keyboard(mContext, xmlLayoutResId);
            mKeyboardView = (PlateNumberKeyboardView) LayoutInflater.from(mContext).inflate(R.layout.view_new_keyboard, null);
            if (xmlLayoutResId == R.xml.licence_new_letter) {
                List<Keyboard.Key> keys = mKeyboard.getKeys();
                keys.get(1).label = mCityCharacter;
                keys.get(1).codes[0] = mCityPrimaryCode;
            }

            mKeyboardView.setXmlLayoutResId(xmlLayoutResId, mCityCharacter, mCityPrimaryCode);
            mKeyboardView.setKeyboard(mKeyboard);
            mKeyboardView.setEnabled(true);
            mKeyboardView.setPreviewEnabled(false);
            mKeyboardView.setOnKeyboardActionListener(keyboardActionListener);

            if (mKeyboardWindow == null) {
                mKeyboardWindow = new PopupWindow(mKeyboardView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                mKeyboardWindow.setContentView(mKeyboardView);
            }
            mKeyboardWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mScrollDistance > 0) {
                        int temp = mScrollDistance;
                        mScrollDistance = 0;
                        if (null != mContentView) {
                            mContentView.scrollBy(0, -temp);
                        }
                    }
                }
            });

            hideSysInput();
            showKeyboard();
            mXmlLayoutResId = xmlLayoutResId;
        }
    }

    /**
     * @param xmlLayoutResId
     */
    private void initNumKeyboard(int xmlLayoutResId) {
        if (mXmlLayoutResId != xmlLayoutResId) {
            hideKeyboard();
            mKeyboard = new Keyboard(mContext, xmlLayoutResId);
            mKeyboardView = (PlateNumberKeyboardView) LayoutInflater.from(mContext).inflate(R.layout.view_keyboard_v2, null);
            mKeyboardView.setKeyboard(mKeyboard);
            mKeyboardView.setEnabled(true);
            mKeyboardView.setPreviewEnabled(false);
            mKeyboardView.setOnKeyboardActionListener(keyboardActionListener);

            if (mKeyboardWindow == null) {
                mKeyboardWindow = new PopupWindow(mKeyboardView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                mKeyboardWindow.setContentView(mKeyboardView);
            }
            mKeyboardWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mScrollDistance > 0) {
                        int temp = mScrollDistance;
                        mScrollDistance = 0;
                        if (null != mContentView) {
                            mContentView.scrollBy(0, -temp);
                        }
                    }
                }
            });

            hideSysInput();
            showKeyboard();
            mXmlLayoutResId = xmlLayoutResId;
        }
    }

    public void isIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isIntercept) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void onResume() {
        hideSysInput();
    }

    /**
     * 添加车牌号校验规则
     */
    public void addPlateNumberValidator(String city) {
        mNumberEditText.clearValidators();
        mNumberEditText.addValidator(new PlateNumberRegexpValidator("请输入正确的车牌号", "^[A-Z_0-9]{5}$", city));
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            NewPlateNumberLayout.super.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == R.id.layout_license_city) {
                    if (mCityEditText.getText().length() == 1) {
                        initKeyboard(R.xml.licence_new_letter);
                    } else {
                        initKeyboard(R.xml.licence_new_city);
                    }
                    mCityEditText.requestFocus();
                    mCityEditText.requestFocusFromTouch();
                } else if (v.getId() == R.id.layout_license_num) {
                    if (TextUtils.isEmpty(mCityEditText.getText())) {
                        jumpCityKerboard();
                    } else if (mCityEditText.getText().length() > 0 && mCityEditText.getText().length() < 2) {
                        initKeyboard(R.xml.licence_new_letter);
                        mCityEditText.requestFocus();
                        mCityEditText.requestFocusFromTouch();
                    } else {
                        initNumKeyboard(R.xml.licence_num);
                        mNumberEditText.requestFocus();
                        mNumberEditText.requestFocusFromTouch();
                    }
                }

                if (isNeedCustomKeyboard) {
                    hideSysInput();
                    showKeyboard();
                }
            }
            return true;
        }
    };

    @TargetApi(11)
    private void removeCopyAbility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mCityEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });

            mNumberEditText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
    }

    /**
     * @param context
     */
    private void initScreenParams(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);

        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        density = metrics.density;
        densityDpi = metrics.densityDpi;

        screenHeightNoNavBar = screenHeight;

        int version = Build.VERSION.SDK_INT;
        // 新版本的android 系统有导航栏，造成无法正确获取高度
        if (version == 13) {
            try {
                Method method = display.getClass().getMethod("getRealHeight");
                screenHeightNoNavBar = (Integer) method.invoke(display);
            } catch (Exception e) {
                // do nothing
            }
        } else if (version > 13) {
            try {
                Method method = display.getClass().getMethod("getRawHeight");
                screenHeightNoNavBar = (Integer) method.invoke(display);
            } catch (Exception e) {
                // do nothing
            }
        }

        screenContentHeight = screenHeightNoNavBar - getStatusBarHeight(context);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    private int getStatusBarHeight(Context context) {
        Class<?> clazz = null;
        Object object = null;
        Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            object = clazz.newInstance();
            field = clazz.getField("status_bar_height");
            x = Integer.parseInt(field.get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    private Keyboard.Key getKeyByKeyCode(int primaryCode) {
        if (null != mKeyboard) {
            List<Keyboard.Key> keyList = mKeyboard.getKeys();
            for (int i = 0, size = keyList.size(); i < size; i++) {
                Keyboard.Key key = keyList.get(i);

                int codes[] = key.codes;

                if (codes[0] == primaryCode) {
                    return key;
                }
            }
        }

        return null;
    }

    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);
        DebugLog.i(TAG, "appUsableSize: " + appUsableSize + "-----realScreenSize: " + realScreenSize);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }

        return size;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DebugLog.i(TAG, "KeyEvent.KEYCODE_BACK");
            if (null != mKeyboardWindow) {
                if (mKeyboardWindow.isShowing()) {
                    mKeyboardWindow.dismiss();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.mWindow = ((Activity) mContext).getWindow();
        this.mDecorView = this.mWindow.getDecorView();
        rootView = ((ViewGroup) (this.mDecorView.findViewById(android.R.id.content))).getChildAt(0);
//        this.mContentView = this.mWindow.findViewById(Window.ID_ANDROID_CONTENT);
        DebugLog.i(TAG, rootView.toString());
        hideSysInput();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        hideKeyboard();

        mKeyboardWindow = null;
        mKeyboardView = null;
        mKeyboard = null;

        mDecorView = null;
        mContentView = null;
        mWindow = null;
    }

    /**
     * @param scrollView
     */
    public void setScrollView(View scrollView) {
        this.mContentView = scrollView;
    }

    private int mScreenHeight;
    private int mKeyboardHeight;

    /**
     * 显示自定义键盘
     */
    public void showKeyboard() {
        if (mKeyboardWindow != null) {
            if (!mKeyboardWindow.isShowing()) {

                if (mDecorView != null) {
                    mKeyboardView.setKeyboard(mKeyboard);
//                    mKeyboardWindow.setFocusable(true);
                    mKeyboardWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    mKeyboardWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    mKeyboardWindow.setBackgroundDrawable(new BitmapDrawable());
                    mKeyboardWindow.setOutsideTouchable(true);
                    mKeyboardWindow.setAnimationStyle(R.style.PopupAnimation);
                    mKeyboardWindow.showAtLocation(this.rootView, Gravity.BOTTOM, 0, 0);
                    mKeyboardWindow.update();
                }

                if (null != mDecorView && null != mContentView) {
                    /*int[] pos = new int[2];
                    getLocationOnScreen(pos);
                    float height = dpToPx(getContext(), 240);

                    int[] hsmlpos = new int[2];
                    mDecorView.getLocationOnScreen(hsmlpos);

                    Rect outRect = new Rect();
                    mDecorView.getWindowVisibleDisplayFrame(outRect);

                    int screen = screenContentHeight;
                    mScrollDistance = (int) ((pos[1] + getMeasuredHeight() - outRect.top) - (screen - height));

                    if (mScrollDistance > 0) {
                        mContentView.scrollBy(0, mScrollDistance);
                    }*/
                    setScrollDistance(mContext);

                }
            }
        }
    }

    /**
     * 控件向上移动的距离
     *
     * @param context
     */
    public void setScrollDistance(Context context) {
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        DebugLog.i(TAG, mKeyboardView.getPaddingTop() + "-------" + mKeyboardView.getPaddingBottom() + "-------" + mKeyboardWindow.getHeight());
        mKeyboardHeight = (int) (0.07 * 4 * mScreenHeight) + 55 + mKeyboardView.getPaddingTop() + mKeyboardView.getPaddingBottom();
        int[] pos = new int[2];
        getLocationOnScreen(pos);
        int toBottom = mScreenHeight - pos[1] - (int) dpToPx(context, 44);
        if (toBottom < mKeyboardHeight && mContentView != null) {
//                        mScrollDistance = mKeyboardHeight ;
            mContentView.scrollBy(0, mKeyboardHeight - toBottom);
        }
    }

    /**
     * 隐藏自定义键盘
     */
    public void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    /**
     * 密度转换为像素值
     *
     * @param context
     * @param dp
     * @return
     */
    private int dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSysInput() {
        if (this != null) {
            if (this.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private KeyboardView.OnKeyboardActionListener keyboardActionListener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (primaryCode != 32) {
                Editable editable = null;
                int start = 0;
                if (mXmlLayoutResId == R.xml.licence_new_city) {
                    mCityCharacter = Character.toString((char) primaryCode);
                    mCityPrimaryCode = primaryCode;
                    editable = mCityEditText.getText();
                    if (editable.length() <= 2) {
                        editable.clear();
                    }
                    start = mCityEditText.getSelectionStart();
                } else if (mXmlLayoutResId == R.xml.licence_new_letter) {
                    editable = mCityEditText.getText();
                    if (editable.length() == 2) {
                        editable.clear();
                    }
                    start = mCityEditText.getSelectionStart();
                } else {
                    editable = mNumberEditText.getText();
                    start = mNumberEditText.getSelectionStart();
                }
                if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 隐藏键盘
                    if (mNextView instanceof EditText) {
                        mNextView.setFocusable(true);
                        mNextView.setFocusableInTouchMode(true);
                        mNextView.requestFocus();

                    } else if (mNextView instanceof Button) {
                        mNextView.setFocusable(true);
                        mNextView.setFocusableInTouchMode(true);
                        mNextView.requestFocus();
                    }
                    hideKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                    if (mXmlLayoutResId != R.xml.licence_new_letter) {
                        if (editable != null && editable.length() > 0) {
                            if (start > 0) {
                                editable.delete(start - 1, start);
                            }
                        }
                    }
                } else if (0x0 <= primaryCode && primaryCode <= 0x7f) {
                    //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
                    if (mXmlLayoutResId == R.xml.licence_new_letter && primaryCode != 73) {
                        editable.insert(start, Character.toString((char) primaryCode));
                    } else if (mXmlLayoutResId == R.xml.licence_num) {
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                } else if (primaryCode > 0x7f) {
                    /*Keyboard.Key key = getKeyByKeyCode(primaryCode);
                    //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
                    editable.insert(start, key.label);*/
                    if (mCityPrimaryCode != primaryCode && mXmlLayoutResId == R.xml.licence_new_letter) {
                        editable.insert(start, Character.toString((char) primaryCode));
                    } else if (mXmlLayoutResId == R.xml.licence_new_city || mXmlLayoutResId == R.xml.licence_num) {
                        editable.insert(start, Character.toString((char) primaryCode));
                    }
                } else if (primaryCode == EditorInfo.IME_ACTION_DONE) {
                    //其他一些暂未开放的键指令，如next到下一个输入框等指令

                } else if (primaryCode == -9) {
                    initKeyboard(R.xml.licence_new_city);
                }
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private TextWatcher cityChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mNumberEditText.clearValidators();
            if (s.length() == 1) {
                addPlateNumberValidator(mCityEditText.getText().toString());
                initKeyboard(R.xml.licence_new_letter);
            } else if (s.length() == 2) {
                addPlateNumberValidator(mCityEditText.getText().toString());
                jumpNumKerboard();
            }
        }
    };

    private TextWatcher numberChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            jumpNumKerboard();
            if (s.length() == 5) {
                if (mMatchPlateNumberListener != null) {
                    mMatchPlateNumberListener.matchPlateNumberListener();
                }
            }
        }
    };

    private OnMatchPlateNumberListener mMatchPlateNumberListener;

    public interface OnMatchPlateNumberListener {
        void matchPlateNumberListener();
    }

    public void setMatchPlateNumberListener(OnMatchPlateNumberListener l) {
        this.mMatchPlateNumberListener = l;
    }

    /**
     * 跳转到字母键盘
     */
    public void jumpNumKerboard() {
        initNumKeyboard(R.xml.licence_num);
        mNumberEditText.setFocusable(true);
        mNumberEditText.setFocusableInTouchMode(true);
        mNumberEditText.requestFocus();
        mNumberEditText.requestFocusFromTouch();
    }

    /**
     * 跳转到字母键盘
     */
    public void jumpCityKerboard() {
        initKeyboard(R.xml.licence_new_city);
        mCityEditText.setFocusable(true);
        mCityEditText.setFocusableInTouchMode(true);
        mCityEditText.requestFocus();
        mCityEditText.requestFocusFromTouch();
    }

    /**
     * 获取车牌号
     *
     * @return
     */
    public String getPlateNumber() {
        return mCityEditText.getText().toString() + mNumberEditText.getText().toString();
    }

    public void setNextView(View nextView) {
        mNextView = nextView;
    }

    public EditTextValidatorView getCityEditText() {
        return mCityEditText;
    }

    public EditTextValidatorView getNumberEditText() {
        return mNumberEditText;
    }

    /**
     * 设置车牌号
     *
     * @param plateNumber
     * @param isNeedProvince
     */
    public void setPlateNumber(String plateNumber, boolean isNeedProvince) {
        if (isNeedProvince) {
            removeTextChangedListener();
            try {
                if (TextUtils.isEmpty(plateNumber)) {
                    BaseSharedPreferencesHelper baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(SampleApplicationContext.context);
                    String province = baseSharedPreferencesHelper.getProvince();
                    if (!TextUtils.isEmpty(province)) {
                        DebugLog.i(TAG, "province:" + province);
                        if (province.substring(province.length() - 1, province.length()).equals("省")) {
                            DebugLog.i(TAG, "province:" + province.substring(0, province.length() - 1));
                            PAC query = PACDao.getInstance().checkProvince(province.substring(0, province.length() - 1));
                            mCityEditText.setText(query.plateNumber);
                        }
                    }
                } else {
                    mCityEditText.setText(plateNumber.substring(0, 2));
                    mNumberEditText.setText(plateNumber.substring(2, 7));
                    mNumberEditText.setSelection(plateNumber.substring(2, 7).length());
                }
                initTextChangedListener();
                DebugLog.i(TAG, "mCityView" + mCityEditText.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            addPlateNumberValidator(mCityEditText.getText().toString());
        } else {
            if (!TextUtils.isEmpty(plateNumber)) {
                mCityEditText.setText(plateNumber.substring(0, 2));
                mNumberEditText.setText(plateNumber.substring(2, 7));
                mNumberEditText.setSelection(plateNumber.substring(2, 7).length());
            }
        }
    }

    public void setParentFocus() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.requestFocus();
    }


}
