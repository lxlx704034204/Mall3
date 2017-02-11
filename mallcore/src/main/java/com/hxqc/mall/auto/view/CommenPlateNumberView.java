package com.hxqc.mall.auto.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.dao.PACDao;
import com.hxqc.mall.auto.model.PAC;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.PlateNumberRegexpValidator;
import com.hxqc.util.DebugLog;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 07
 * FIXME
 * Todo 通用车牌号键盘控件
 */
public class CommenPlateNumberView extends LinearLayout {

    private static final String TAG = AutoInfoContants.LOG_J;
    private PlateNumberTextView mCityView;
    private PlateNumberTextView mNumView;
    private OnCheckListener mCheckListener;
    private OnCheckAutoListener mCheckAutoListener;
    private OnMatchPlateNumberListener mMatchPlateNumberListener;
    private Context mContext;
    private boolean isIntercept = false;

    /**
     * 城市监听
     */
    private TextWatcher cityChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            DebugLog.i(TAG, "s:" + s.toString());
            jumpNumKerboard();
        }
    };

    /**
     * 车牌位数监听
     */
    private TextWatcher licenseNumberChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            DebugLog.i(TAG, "beforeTextChanged:" + s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            DebugLog.i(TAG, "onTextChanged:" + s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            DebugLog.i(TAG, "afterTextChanged:" + s.toString());
            if (s.toString().length() == 6) {
                String plateNumber = mCityView.getText() + s.toString();
                DebugLog.i(TAG, "getInstance" + plateNumber);
//                AutoInfoControl.getInstance().checkAutoInfo(ReserveMaintainActivity.this, "", plateNumber, ReserveMaintainActivity.this);+
                if (mCheckListener != null) {
                    if (!TextUtils.isEmpty(mCityView.getText())) {
                        mCheckListener.checkListener(plateNumber);
                    } else {
                        ToastHelper.showRedToast(mContext, "请选择省份");
                    }
                }

                if (mCheckAutoListener != null) {
                    mCheckAutoListener.checkAutoListener(plateNumber);
                }

                if (mMatchPlateNumberListener != null) {
                    mMatchPlateNumberListener.matchPlateNumberListener();
                }


            }
        }
    };

    public CommenPlateNumberView(Context context) {
        this(context, null);

    }

    public CommenPlateNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context, attrs);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        DebugLog.i(TAG, "Touch");
        mCityView.addTextChangedListener(cityChangeListener);
        mNumView.addTextChangedListener(licenseNumberChangeListener);
        mNumView.setOnTouchListener(numTouchListener);
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_commen_plate_num, this);

        mCityView = (PlateNumberTextView) findViewById(R.id.commen_license_city);
        mCityView.setMode(PlateNumberTextView.MODE_CITY);

        mNumView = (PlateNumberTextView) findViewById(R.id.commen_license_number);

        mNumView.setMode(PlateNumberTextView.MODE_WORD);
        mNumView.addValidator(new PlateNumberRegexpValidator("请输入正确的车牌号", "^[A-Z]{1}[A-Z_0-9]{5}$"));
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

    }

    /**
     * 校验车牌号码
     *
     * @param activity
     * @return
     */
    public boolean checkPlateNumber(Activity activity) {
        VWholeEditManager vWholeEditManager = new VWholeEditManager(activity);
        vWholeEditManager.addEditView(mNumView);
        return vWholeEditManager.validate();
    }

    /**
     * 设置车牌号
     *
     * @param city
     * @param num
     */
    public void setPlateNumber(String city, String num) {
        try {
            mCityView.setText(city);
            mNumView.setText(num);
            mNumView.setSelection(num.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置车牌号
     *
     * @param plateNumber
     */
    public void setPlateNumber(String plateNumber) {
        /*if(plateNumber.length()<7) {
            mNumView.setText(plateNumber);
            mNumView.setSelection(plateNumber.length());
        } else {
            mCityView.setText(plateNumber.substring(0, 1));
            mNumView.setText(plateNumber.substring(1, 7));
            mNumView.setSelection(plateNumber.substring(1, 7).length());
        }*/
        try {
            if (TextUtils.isEmpty(plateNumber)) {
                BaseSharedPreferencesHelper baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(SampleApplicationContext.context);
                String province = baseSharedPreferencesHelper.getProvince();
                if (!TextUtils.isEmpty(province)) {
                    DebugLog.i(TAG, "province:" + province);
                    if (province.substring(province.length() - 1, province.length()).equals("省")) {
                        DebugLog.i(TAG, "province:" + province.substring(0, province.length() - 1));
                        PAC query = PACDao.getInstance().checkProvince(province.substring(0, province.length() - 1));
                        mCityView.setText(query.plateNumber.substring(0, 1));
                    }
                }
            } else {
                mCityView.setText(plateNumber.substring(0, 1));
                mNumView.setText(plateNumber.substring(1, 7));
                mNumView.setSelection(plateNumber.substring(1, 7).length());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取车牌号
     *
     * @return
     */
    public String getPlateNumber() {
        return mCityView.getText().toString() + mNumView.getText().toString();
    }

    /**
     * 设置匹配监听
     *
     * @param l
     */
    public void setCheckListener(OnCheckListener l) {
        this.mCheckListener = l;
        initEvent();
    }

    /**
     * 输入监听
     */
    public void setCheckListener() {
        initEvent();
    }


    public void setCheckAutoListener(OnCheckAutoListener l) {
        this.mCheckAutoListener = l;
    }

    public void setMatchPlateNumberListener(OnMatchPlateNumberListener l) {
        this.mMatchPlateNumberListener = l;
    }

    public void initTextChangedListener() {
        mCityView.addTextChangedListener(cityChangeListener);
    }

    public PlateNumberTextView getmCityView() {
        return mCityView;
    }

    public PlateNumberTextView getmNumView() {
        return mNumView;
    }

    /**
     * 跳转到字母键盘
     */
    public void jumpNumKerboard() {
        mCityView.clearFocus();
        mCityView.dismissPopup();
        mNumView.setFocusable(true);
        mNumView.setFocusableInTouchMode(true);
        mNumView.requestFocus();
        mNumView.showNumberKeyboard();
    }

    /**
     * 跳转到字母键盘
     */
    public void jumpCityKerboard() {
        mNumView.clearFocus();
        mNumView.dismissPopup();
        mCityView.setFocusable(true);
        mCityView.setFocusableInTouchMode(true);
        mCityView.requestFocus();
        mCityView.showCityKeyboard();
    }

    /**
     * 消除popup
     */
    public void dismissPopup() {
        mCityView.dismissPopup();
        mNumView.dismissPopup();
    }

    public interface OnCheckListener {
        void checkListener(String plateNumber);
    }

    public interface OnCheckAutoListener {
        void checkAutoListener(String plateNumber);
    }

    public interface OnMatchPlateNumberListener {
        void matchPlateNumberListener();
    }

    /**
     * @param isAdd
     */
    public void setState(boolean isAdd) {
        mCityView.setState(isAdd);
        mNumView.setState(isAdd);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isIntercept) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void isIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
    }

    private View.OnTouchListener numTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (TextUtils.isEmpty(mCityView.getText())) {
                jumpCityKerboard();
                return true;
            }
            return false;
        }
    };

    public void focusable() {
        mCityView.setFocusable(false);
        mCityView.setFocusableInTouchMode(false);
        mCityView.requestFocus();
        mCityView.requestFocusFromTouch();

        mNumView.setFocusable(false);
        mNumView.setFocusableInTouchMode(false);
        mNumView.requestFocus();
        mNumView.requestFocusFromTouch();

    }

}
