package com.hxqc.aroundservice.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.aroundservice.adapter.AutoVarietyAdapterV2;
import com.hxqc.aroundservice.adapter.CityAdapter;
import com.hxqc.aroundservice.adapter.ProvinceAdapter;
import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.control.IllegalSharedPreferencesHelper;
import com.hxqc.aroundservice.model.AutoSpeciesType;
import com.hxqc.aroundservice.model.City;
import com.hxqc.aroundservice.model.IllegalQueryProvinceAndCity;
import com.hxqc.aroundservice.model.IllegalQueryRequestData;
import com.hxqc.aroundservice.model.IllegalQueryResult;
import com.hxqc.aroundservice.model.Province;
import com.hxqc.aroundservice.model.SpeciesNumber;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.FocusEditBackActivity;
import com.hxqc.mall.auto.dao.PACDao;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.PAC;
import com.hxqc.mall.auto.view.CommenPlateNumberView;
import com.hxqc.mall.auto.view.CommonEditInfoItemView;
import com.hxqc.mall.auto.view.PlateNumberTextView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.SpinnerPopWindow;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 07
 * FIXME
 * Todo 违章查询
 */
@Deprecated
public class IllegalQueryActivity extends FocusEditBackActivity {

    private final static int REFRESH_VIEW = 0;
//    private SpinnerPopWindow mAutoVarietyView;
    private SpinnerPopWindow mAutoProvinceView;//下拉列表弹窗的控件
    private SpinnerPopWindow mAutoCityView;
//    private PlateNumberTextView mPlateCityView;
//    private PlateNumberTextView mPlateNumView;
    private CommonEditInfoItemView mChassisView;//一条包含说明和填写的控件
    private CommonEditInfoItemView mEngineView;
    private Button mQueryView;
    private TextView mAutoVarietyTextView;
    private TextView mAutoProvinceTextView;
    private TextView mAutoCityTextView;
    private AutoVarietyAdapterV2 autoVarietyAdapterV2;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
//    private String classa;
//    private String engine;
    private City city;
    private AutoSpeciesType autoSpeciesType;
    private CommenPlateNumberView mPlateNumView;//车牌号输入控件
    private PAC query = null;

    private final String text="1、违章数据由武汉交管局提供，数据精准、权威，可查看车辆违章照片和违章历史记录，您只需在线填写基本信息并完成行驶证的上传，一键即可处理违章；\n"
            +"2、该业务暂不支持客户自助式处理违章；\n"
            +"3、业务订单支付成功后，不能退办该业务，我们将尽快为您办理完成；\n"
            +"4、违章查缴处理由合作的第三方提供线下服务，业务缴款时按每条收取一定的服务费。\n"
            +"\n"
            +"如有其它问题或建议，请致电客服\n"
            +"客服电话：400-1868-555";

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_VIEW:
                    city = (City) msg.obj;
                    //通过返回的车架号标识判断所需车架号位数
                    if (city.classa.equals("1")) {
                        mChassisView.setTitleTextColor(getResources().getColor(R.color.text_color_subheading));
                        if (city.classno.equals("0")) {
                            mChassisView.setContentHint("请输入后完整位车架号");
                        } else {
                            mChassisView.setContentHint("请输入后" + city.classno + "位车架号");
                            mChassisView.setContentMaxLength(Integer.parseInt(city.classno));
                        }
                        mChassisView.setVisibility(View.VISIBLE);
                    } else {
                        mChassisView.setVisibility(View.GONE);
                    }
                    //通过返回的发动机号标识判断所需发动机号位数
                    if (city.engine.equals("1")) {
                        mEngineView.setTitleTextColor(getResources().getColor(R.color.text_color_subheading));
                        if (city.engineno.equals("0")) {
                            mEngineView.setContentHint("请输入后完整发动机号");
                        } else {
                            mEngineView.setContentHint("请输入后" + city.engineno + "发动机号");
                            mEngineView.setContentMaxLength(Integer.parseInt(city.engineno));
                        }
                        mEngineView.setVisibility(View.VISIBLE);
                    } else {
                        mEngineView.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private PlateNumberTextView mCityView;
    private PlateNumberTextView mNumView;
    private IllegalQueryRequestData imIllegalQueryRequestData = null;
    /**
     * 查询按钮的点击监听
     */
    private View.OnClickListener queryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*if (TextUtils.isEmpty(mPlateNumView.getPlateNumber())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请输入车牌号");
                return;
            } else if (mPlateNumView.getPlateNumber().length() < 7) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "车牌号格式不对");
                return;
            }*/

            String plateNumber = "鄂A" + mNumView.getText();

            if (TextUtils.isEmpty(plateNumber)) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请输入车牌号");
                return;
            } else if (plateNumber.length() < 7) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "车牌号格式不对");
                return;
            }

            /*if (TextUtils.isEmpty(mAutoVarietyTextView.getText())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请选择号牌种类");
                return;
            }*/

            /*if (TextUtils.isEmpty(mAutoProvinceTextView.getText())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请选择省份");
                return;
            }
            if (TextUtils.isEmpty(mAutoProvinceTextView.getText())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请选择城市");
                return;
            }*/

            /*if (mEngineView.getVisibility() == View.VISIBLE && mChassisView.getVisibility() == View.GONE) {
                if (TextUtils.isEmpty(mEngineView.getContentText())) {
                    ToastHelper.showYellowToast(IllegalQueryActivity.this, "发动机号不能为空");
                    return;
                }
            } else if (mEngineView.getVisibility() == View.GONE && mChassisView.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(mChassisView.getContentText())) {
                    ToastHelper.showYellowToast(IllegalQueryActivity.this, "车架号不能为空");
                    return;
                }
            } else if (mEngineView.getVisibility() == View.VISIBLE && mChassisView.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(mChassisView.getContentText()) && TextUtils.isEmpty(mEngineView.getContentText())) {
                    ToastHelper.showYellowToast(IllegalQueryActivity.this, "一般情况下只需要填发动机号或车架号");
                    return;
                }
            }*/
            if (TextUtils.isEmpty(mChassisView.getContentText())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "车架号不能为空");
                return;
            }

            if (imIllegalQueryRequestData == null) {
                imIllegalQueryRequestData = new IllegalQueryRequestData();
            }
            /*if (city != null) {
                imIllegalQueryRequestData.city = city.city_code;
            }*/
            imIllegalQueryRequestData.cityCode = "HUB_WuHan";
//            illegalQueryRequestData.hphm = mPlateCityView.getText().toString() + mPlateNumView.getText().toString();
//            imIllegalQueryRequestData.hphm = mPlateNumView.getPlateNumber().toString();
            imIllegalQueryRequestData.hphm = plateNumber;
            imIllegalQueryRequestData.hpzl = "02";
//            imIllegalQueryRequestData.engineno = mEngineView.getContentText();
            imIllegalQueryRequestData.engineno = "";
            imIllegalQueryRequestData.classno = mChassisView.getContentText();
            imIllegalQueryRequestData.registno = "";
            imIllegalQueryRequestData.handled = "0";
//            mPlateNumView.dismissPopup();
            IllegalQueryControl.getInstance().requestIllegalQuery(IllegalQueryActivity.this, imIllegalQueryRequestData, illegalQueryCallBack);

        }
    };

    /**
     * 品牌种类子类的点击
     */
    private AdapterView.OnItemClickListener autoVarietyItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = (String) parent.getAdapter().getItem(position);
            autoSpeciesType = autoVarietyAdapterV2.getmAutoSpeciesTypes().get(position);
            mAutoVarietyTextView.setText(item);
        }
    };

    /**
     * 城市子类的点击监听
     */
    private AdapterView.OnItemClickListener cityItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = (String) parent.getAdapter().getItem(position);
            mAutoCityTextView.setText(item);
            Message msg = Message.obtain();
            msg.what = REFRESH_VIEW;
            msg.obj = cityAdapter.getmCities().get(position);
            mHandler.sendMessage(msg);
        }
    };

    /**
     * 省份子类的点击监听
     */
    private AdapterView.OnItemClickListener provinceItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item = (String) parent.getAdapter().getItem(position);
            mAutoProvinceTextView.setText(item);
            if (!TextUtils.isEmpty(mAutoCityTextView.getText())) {
                mAutoCityTextView.setText("");
            }
            if (provinceAdapter != null) {
                if (cityAdapter == null) {
//                    DebugLog.i(TAG,"123:" + provinceAdapter.getmProvinces().get(0).citys.get(0).city_name);
                    cityAdapter = new CityAdapter(IllegalQueryActivity.this, provinceAdapter.getmProvinces().get(position).citys);
                    mAutoCityView.setAdapter(cityAdapter);
                } else {
                    cityAdapter.notifyData(provinceAdapter.getmProvinces().get(position).citys);
                }
                mAutoCityView.setOnItemClickList(cityItemListener);
            }
        }
    };

    /**
     * 城市点击监听
     */
    private View.OnClickListener autoCityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mAutoProvinceTextView.getText())) {
                ToastHelper.showYellowToast(IllegalQueryActivity.this, "请选择省份");
                return;
            } else {
                if (provinceAdapter.getmProvinces() != null) {
                    for (int i = 0; i < provinceAdapter.getmProvinces().size(); i++) {
                        if (mAutoProvinceTextView.getText().equals(provinceAdapter.getmProvinces().get(i).province)) {
                            if (cityAdapter == null) {
                                cityAdapter = new CityAdapter(IllegalQueryActivity.this, provinceAdapter.getmProvinces().get(i).citys);
                                mAutoCityView.setAdapter(cityAdapter);
                            } else {
                                cityAdapter.notifyData(provinceAdapter.getmProvinces().get(i).citys);
                            }
                        }
                    }
                    mAutoCityView.showPopupWindow(v);
                    mAutoCityView.setOnItemClickList(cityItemListener);
                }
            }
        }
    };

    /**
     * 车架号控件的点击监听
     */
    private CommonEditInfoItemView.onContentClickListener chassisClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (city != null) {
                if (!TextUtils.isEmpty(mAutoProvinceTextView.getText()) && !TextUtils.isEmpty(mAutoCityTextView.getText()) && city.classa.equals("1")) {
                    if (!mChassisView.getContentFocusable()) {
                        mChassisView.setContentFocusable(true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
    };

    /**
     * 发动机号的点击监听
     */
    private CommonEditInfoItemView.onContentClickListener engineClickListener = new CommonEditInfoItemView.onContentClickListener() {
        @Override
        public void onContentClick(View v) {
            if (city != null) {
                if (!TextUtils.isEmpty(mAutoProvinceTextView.getText()) && !TextUtils.isEmpty(mAutoCityTextView.getText()) && city.engine.equals("1")) {
                    if (!mEngineView.getContentFocusable()) {
                        mEngineView.setContentFocusable(true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
    };

    private TextWatcher cityTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mPlateNumView.getPlateNumber().length() >= 2) {
                query = PACDao.getInstance().query(mPlateNumView.getPlateNumber().substring(0, 2));
                if (query != null) {
                    mAutoProvinceTextView.setText(query.province);
                    mAutoCityTextView.setText(query.city);
                    matcheCity(query.city);
                } else {
                    mAutoProvinceTextView.setText("");
                    mAutoCityTextView.setText("");
                }
                query = null;
            }
            mPlateNumView.jumpNumKerboard();
        }
    };

    private TextWatcher numTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1) {
                if (mPlateNumView.getPlateNumber().length() == 2) {
                    query = PACDao.getInstance().query(mPlateNumView.getPlateNumber());
                    if (query != null) {
                        mAutoProvinceTextView.setText(query.province);
                        mAutoCityTextView.setText(query.city);
                        matcheCity(query.city);
                    } else {
                        mAutoProvinceTextView.setText("");
                        mAutoCityTextView.setText("");
                    }
                }
                query = null;
            }
        }
    };
    private IllegalSharedPreferencesHelper imIllegalSharedPreferencesHelper = null;
    private TextView mExplainView;

    /**
     * @param city
     */
    private void matcheCity(String city) {
        City queryCity = null;
        if (provinceAdapter != null) {
            ArrayList<Province> provinces = provinceAdapter.getmProvinces();
            if (provinces != null && provinces.size() > 0) {
                for (int i = 0; i < provinces.size(); i++) {
                    for (int j = 0; j < provinces.get(i).citys.size(); j++) {
                        if (city.equals(provinces.get(i).citys.get(j).city_name)) {
                            queryCity = provinces.get(i).citys.get(j);
                        }
                    }
                }
            }
        }
        if (queryCity != null) {
            Message msg = Message.obtain();
            msg.what = REFRESH_VIEW;
            msg.obj = queryCity;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_illegal_query);

        initView();

        initData();

        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mQueryView.setOnClickListener(queryClickListener);
//        mAutoVarietyView.setOnItemClickList(autoVarietyItemListener);
//        mAutoProvinceView.setOnItemClickList(provinceItemListener);
//        mAutoCityView.setOnClickListener(autoCityClickListener);

//        mChassisView.setOnContentClickListener(chassisClickListener);
//        mEngineView.setOnContentClickListener(engineClickListener);

//        mPlateNumView.initTextChangedListener();
//        mCityView.addTextChangedListener(cityTextWatcher);
//        mNumView.addTextChangedListener(numTextWatcher);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        imIllegalSharedPreferencesHelper = new IllegalSharedPreferencesHelper(this);
//        IllegalQueryControl.getInstance().requestSpeciesNumber(this, speciesNumberCallBack);
//        IllegalQueryControl.getInstance().requestIllegalQueryCity(this, illegalQueryCityCallBack);

    }

    /**
     * 初始化控件
     */
    private void initView() {
//        mAutoVarietyView = (SpinnerPopWindow) findViewById(R.id.illegal_query_auto_variety);
//        mAutoVarietyView.setPopHeight(480);
        mAutoVarietyTextView = (TextView) findViewById(R.id.illegal_query_auto_variety_tv);

        mAutoProvinceView = (SpinnerPopWindow) findViewById(R.id.illegal_query_auto_province);
        mAutoProvinceView.setPopHeight(480);
        mAutoProvinceTextView = (TextView) findViewById(R.id.illegal_query_auto_province_tv);
        mAutoProvinceView.setClickable(false);

//        mAutoCityView = (SpinnerPopWindow) findViewById(R.id.illegal_query_auto_city);
//        mAutoCityView.setPopHeight(480);
//        mAutoCityTextView = (TextView) findViewById(R.id.illegal_query_auto_city_tv);
//        mAutoCityView.setClickable(false);
//        mPlateCityView = (PlateNumberTextView) findViewById(R.id.illegal_query_plate_city);
//        mPlateCityView.setMode(PlateNumberTextView.MODE_CITY);
//        mPlateNumView.setMode(PlateNumberTextView.MODE_WORD);
//        mPlateNumView = (CommenPlateNumberView) findViewById(R.id.illegal_query_number);
//        mCityView = mPlateNumView.getmCityView();
//        mNumView = mPlateNumView.getmNumView();
        mNumView = (PlateNumberTextView) findViewById(R.id.illegal_query_plate_num);
        mChassisView = (CommonEditInfoItemView) findViewById(R.id.illegal_query_chassis);
        mEngineView = (CommonEditInfoItemView) findViewById(R.id.illegal_query_engine);
        mQueryView = (Button) findViewById(R.id.illegal_query_query);

        mExplainView = (TextView) findViewById(R.id.illegal_query_explain);
        mExplainView.setText(text);
    }

   /* @Override
    public void onSpeciesNumberSucceed(SpeciesNumber speciesNumber) {
        DebugLog.i(TAG, "speciesNumber:" + speciesNumber.toString());
    *//*    if (autoVarietyAdapterV2 == null) {
            autoVarietyAdapterV2 = new AutoVarietyAdapterV2(IllegalQueryActivity.this, speciesNumber.result);
            mAutoVarietyView.setAdapter(autoVarietyAdapterV2);
            mAutoVarietyTextView.setText(speciesNumber.result.get(1).car);
            autoSpeciesType = speciesNumber.result.get(1);
        } else {
            autoVarietyAdapterV2.notifyData(speciesNumber.result);
        }*//*
    }

    @Override
    public void onSpeciesNumberFailed(boolean offLine) {
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_illegal_processing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.illegal_processing_btn) {
            ActivitySwitchBase.toH5Activity(this, "服务说明", OrderDetailContants.SERVICE_EXPLAIN);
        }
        return false;
    }

    /**
     * 号牌种类的回调
     */
    private CallBackControl.CallBack<SpeciesNumber> speciesNumberCallBack = new CallBackControl.CallBack<SpeciesNumber>() {
        @Override
        public void onSuccess(SpeciesNumber response) {
    /*    if (autoVarietyAdapterV2 == null) {
            autoVarietyAdapterV2 = new AutoVarietyAdapterV2(IllegalQueryActivity.this, speciesNumber.result);
            mAutoVarietyView.setAdapter(autoVarietyAdapterV2);
            mAutoVarietyTextView.setText(speciesNumber.result.get(1).car);
            autoSpeciesType = speciesNumber.result.get(1);
        } else {
            autoVarietyAdapterV2.notifyData(speciesNumber.result);
        }*/
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

 /*   @Override
    public void onIllegalQueryProvinceAndCitySucceed(IllegalQueryProvinceAndCity illegalQueryCity) {
        DebugLog.i(TAG, "speciesNumber:" + illegalQueryCity.toString());
        if (provinceAdapter == null) {
            provinceAdapter = new ProvinceAdapter(IllegalQueryActivity.this, illegalQueryCity.result);
            mAutoProvinceView.setAdapter(provinceAdapter);
        } else {
            provinceAdapter.notifyData(illegalQueryCity.result);
        }

//        PACUtil.getFromAssets(this);
    }

    @Override
    public void onIllegalQueryProvinceAndCityFailed(boolean offLine) {

    }*/

    /**
     * 查询地区的回调
     */
    private CallBackControl.CallBack<IllegalQueryProvinceAndCity> illegalQueryCityCallBack = new CallBackControl.CallBack<IllegalQueryProvinceAndCity>() {
        @Override
        public void onSuccess(IllegalQueryProvinceAndCity response) {
            if(response!=null) {
                if (provinceAdapter == null) {
                    provinceAdapter = new ProvinceAdapter(IllegalQueryActivity.this, response.result);
                    mAutoProvinceView.setAdapter(provinceAdapter);
                } else {
                    provinceAdapter.notifyData(response.result);
                }
                //        PACUtil.getFromAssets(this);
                for (int i = 0; i < response.result.size(); i++) {
                    for (int j = 0; j < response.result.get(i).citys.size(); j++) {
                        if (response.result.get(i).citys.get(j).city_name.equals("武汉")) {
                            city = response.result.get(i).citys.get(j);
                        }
                    }
                }
                if (city.classno.equals("0")) {
                    mChassisView.setContentHint("请输入后完整位车架号");
                } else {
                    mChassisView.setContentHint("请输入后" + city.classno + "位车架号");
                    mChassisView.setContentMaxLength(Integer.parseInt(city.classno));
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

  /*  private IllegalQueryControl.IllegalQueryHandler illegalQueryCallBack = new IllegalQueryControl.IllegalQueryHandler() {
        @Override
        public void onIllegalQuerySucceed(IllegalQueryResult illegalQueryResult) {
            if(illegalQueryResult.result.lists.isEmpty()) {
                ToastHelper.showGreenToast(IllegalQueryActivity.this,"您目前没有未处理的交通违章记录");
            } else {
                ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryActivity.this, illegalQueryResult.result);
            }
        }

        @Override
        public void onIllegalQueryFailed(boolean offLine) {

        }
    };*/

    /**
     * 违章结果
     */
    private CallBackControl.CallBack<IllegalQueryResult> illegalQueryCallBack = new CallBackControl.CallBack<IllegalQueryResult>() {
        @Override
        public void onSuccess(IllegalQueryResult response) {
            if (response != null) {
                ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryActivity.this, OrderDetailContants.ILLEGAL_NEW_LIST,response,imIllegalQueryRequestData);
                if (response.result.lists != null) {
                    if (!response.result.lists.isEmpty()) {
                        imIllegalSharedPreferencesHelper.setCityCode(imIllegalQueryRequestData.cityCode);
                        imIllegalSharedPreferencesHelper.setHPZL(imIllegalQueryRequestData.hpzl);
                        imIllegalSharedPreferencesHelper.setEngineno(imIllegalQueryRequestData.engineno);
                        imIllegalSharedPreferencesHelper.setClassno(imIllegalQueryRequestData.classno);
                    }
                }
                    /*if (response.result.lists != null) {
                        if (response.result.lists.isEmpty()) {
                            ToastHelper.showGreenToast(IllegalQueryActivity.this, "您目前没有未处理的交通违章记录");
                        } else {
                            imIllegalSharedPreferencesHelper.setCity(imIllegalQueryRequestData.cityCode);
                            imIllegalSharedPreferencesHelper.setHPZL(imIllegalQueryRequestData.hpzl);
                            imIllegalSharedPreferencesHelper.setEngineno(imIllegalQueryRequestData.engineno);
                            imIllegalSharedPreferencesHelper.setClassno(imIllegalQueryRequestData.classno);
                            ActivitySwitchAround.toIllegalQueryResultActivity(IllegalQueryActivity.this, response.result);
                        }
                    } else {
                        ToastHelper.showGreenToast(IllegalQueryActivity.this, "您目前没有未处理的交通违章记录");
                    }*/
            } else {
//                ToastHelper.showGreenToast(IllegalQueryActivity.this, "您目前没有未处理的交通违章记录");
            }
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (query != null) {
            query = null;
        }
        if (imIllegalQueryRequestData != null) {
            imIllegalQueryRequestData = null;
        }
        if (imIllegalSharedPreferencesHelper != null) {
            imIllegalSharedPreferencesHelper = null;
        }

        IllegalQueryControl.getInstance().killInstance();
        super.onDestroy();
    }

}
