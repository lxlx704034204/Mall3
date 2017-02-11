package com.hxqc.mall.auto.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.dao.PACDao;
import com.hxqc.mall.auto.model.PAC;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.PlateNumberRegexpValidator;
import com.hxqc.util.DebugLog;


/**
 * Author:胡仲俊
 * Date: 2016 - 10 - 26
 * Des: 车牌号组合控件
 * FIXME
 * Todo
 */

public class PlateNumberEditText extends LinearLayout {

    private static final String TAG = AutoInfoContants.LOG_J;
    private Context mContext;
    private CityEditText mCityEditText;
    protected NumberEditText mNumberEditText;

    public PlateNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        initView(context, attrs);

//        setPlateNumber("");
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     */
    protected void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_common_plate_num, this);

        mCityEditText = (CityEditText) findViewById(R.id.common_license_city);

        mNumberEditText = (NumberEditText) findViewById(R.id.common_license_num);
        mNumberEditText.addValidator(new PlateNumberRegexpValidator("请输入正确的车牌号", "^[A-Z]{1}[A-Z_0-9]{5}$"));
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

    }

    /**
     * 添加车牌号校验规则
     */
    public void addPlateNumberValidator() {
        mNumberEditText.addValidator(new PlateNumberRegexpValidator("请输入正确的车牌号", "^[A-Z]{1}[A-Z_0-9]{5}$"));
    }

    /**
     * 校验车牌号码
     *
     * @param activity
     * @return
     */
    public boolean checkPlateNumber(Activity activity) {
        VWholeEditManager vWholeEditManager = null;
        if (vWholeEditManager == null) {
            vWholeEditManager = new VWholeEditManager(activity);
        }
        vWholeEditManager.addEditView(mNumberEditText);
        return vWholeEditManager.validate();
    }

    /**
     * 初始化事件
     */
    public void initEvent() {
        Log.i(TAG, "initEvent");
        mCityEditText.addTextChangedListener(cityChangeListener);
        mNumberEditText.addTextChangedListener(numberChangeListener);
        mNumberEditText.setOnClickListener(numClickListener);
    }

    private TextWatcher cityChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            jumpNumKerboard();
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
            jumpNumKerboard();
        }
    };

    /**
     * 跳转到字母键盘
     */
    public void jumpNumKerboard() {
        mNumberEditText.showKeyboard();
        mNumberEditText.setFocusable(true);
        mNumberEditText.setFocusableInTouchMode(true);
        mNumberEditText.requestFocus();
        mNumberEditText.requestFocusFromTouch();
    }

    /**
     * 跳转到字母键盘
     */
    public void jumpCityKerboard() {
        mCityEditText.showKeyboard();
        mCityEditText.setFocusable(true);
        mCityEditText.setFocusableInTouchMode(true);
        mCityEditText.requestFocus();
        mCityEditText.requestFocusFromTouch();
    }

    /**
     * 设置车牌号
     *
     * @param city
     * @param num
     */
    public void setPlateNumber(String city, String num) {
        try {
            mCityEditText.setText(city);
            mNumberEditText.setText(num);
            mNumberEditText.setSelection(num.length());
            initEvent();
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
                        mCityEditText.setText(query.plateNumber.substring(0, 1));
                    }
                }
            } else {
                mCityEditText.setText(plateNumber.substring(0, 1));
                mNumberEditText.setText(plateNumber.substring(1, 7));
                mNumberEditText.setSelection(plateNumber.substring(1, 7).length());
            }
            initEvent();
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
        return mCityEditText.getText().toString() + mNumberEditText.getText().toString();
    }

    public CityEditText getmCityView() {
        return mCityEditText;
    }

    public NumberEditText getmNumView() {
        return mNumberEditText;
    }

    private View.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mCityEditText.getText())) {
                jumpCityKerboard();
            }
        }
    };

}
