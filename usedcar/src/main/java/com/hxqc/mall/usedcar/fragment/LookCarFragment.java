package com.hxqc.mall.usedcar.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.CityAdapter;
import com.hxqc.mall.usedcar.adapter.ProvinceAdapter;
import com.hxqc.mall.usedcar.adapter.RegionAdapter;
import com.hxqc.mall.usedcar.model.AreaModel;
import com.hxqc.mall.usedcar.model.CityModel;
import com.hxqc.mall.usedcar.model.RegionModel;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-09-24
 * FIXME
 * Todo 车牌所在地，看车地点
 */
public class LookCarFragment extends FunctionFragment implements View.OnClickListener {
    TextView mTitleView;
    MaterialEditText mProvinceView;
    MaterialEditText mCityView;
    MaterialEditText mRegionView;
    Button mSubmitView;
    PopupWindow popupWindow;
    //pop 窗体的高度
    int mPopHeight = 550;

    ListView listView;

    ArrayList<AreaModel> areaModels_p;//省
    ArrayList<CityModel> areaModels_c;//市
    ArrayList<RegionModel> areaModels_s;//区

    AreaModel current_P;  //当前选中的省
    CityModel current_C;  //当前选中的市
    RegionModel current_S;  //当前选中的区

    ProvinceAdapter provinceAdapter;
    CityAdapter cityAdapter;
    RegionAdapter regionAdapter;

    String flag;

    String province;
    String city;
    String region;
    String provinceId;
    String cityId;
    String provinceCode;
    String cityCode;
    String regionId;
    View popView;
    private ChooseAreaListener chooseAreaListener;

    public LookCarFragment() {
    }

    public void setChooseAreaListener(ChooseAreaListener chooseAreaListener) {
        this.chooseAreaListener = chooseAreaListener;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_look_car, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProvinceView = (MaterialEditText) view.findViewById(R.id.province);
        mCityView = (MaterialEditText) view.findViewById(R.id.city);
        mCityView.setClickable(false);
        mCityView.setFocusable(false);
        mRegionView = (MaterialEditText) view.findViewById(R.id.region);
        mRegionView.setClickable(false);
        mRegionView.setFocusable(false);
        mSubmitView = (Button) view.findViewById(R.id.bt_submit);
        mTitleView = (TextView) view.findViewById(R.id.title);
        popView = View.inflate(getActivity(), R.layout.view_color_popwin, null);
        listView = (ListView) popView.findViewById(R.id.lv_get_Area);
        popupWindow = new PopupWindow(popView);
        chooseAreaListener.initData();
        initEvent();
    }

    private void initEvent() {
        mProvinceView.setOnClickListener(this);
        if (!TextUtils.isEmpty(province)) {
            mCityView.setOnClickListener(this);
        }
        if (!TextUtils.isEmpty(city)) {
            mRegionView.setOnClickListener(this);
        }
        mSubmitView.setOnClickListener(this);
    }

    public void initDate(String province, String city, String region, ArrayList<AreaModel> areaModels_p, String flag) {
        this.province = province;
        this.city = city;
        this.region = region;
        initDate(areaModels_p, flag);
        if (!TextUtils.isEmpty(province)) {
            mProvinceView.setText(province);
        }
        if (!TextUtils.isEmpty(city)) {
            mCityView.setText(city);
        }
        if (!TextUtils.isEmpty(region)) {
            mRegionView.setText(region);
        }
    }

    public void initDate(ArrayList<AreaModel> areaModels_p, String flag) {
        this.areaModels_p = areaModels_p;
        this.flag = flag;
        if (flag.equals("2")) {
            mRegionView.setVisibility(View.GONE);
            mTitleView.setText("车牌所在地");
        } else if (flag.equals("1")) {
            mTitleView.setText("看车地点");
        }
    }

    @Override
    public String fragmentDescription() {
        return "车牌所在地，看车地点";
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.province) {
            showPop(mProvinceView, "1");
        } else if (i == R.id.city) {
            showPop(mCityView, "2");
        } else if (i == R.id.region) {
            showPop(mRegionView, "3");
        } else if (i == R.id.bt_submit) {
            if (mProvinceView.getText().toString().trim().equals("")) {
                ToastHelper.showYellowToast(getActivity(), "请选择省");
                return;
            }
            if (mCityView.getText().toString().trim().equals("")) {
                ToastHelper.showYellowToast(getActivity(), "请选择市");
                return;
            }
            if (flag.equals("1")) {
                if (mRegionView.getText().toString().trim().equals("")) {
                    ToastHelper.showYellowToast(getActivity(), "请选择区");
                    return;
                }
            }
            chooseAreaListener.onSubmitComplete(mProvinceView.getText().toString().trim(),
                    mCityView.getText().toString().trim(), mRegionView.getText().toString().trim(),
                    provinceId, provinceCode, cityId, cityCode, regionId, areaModels_p, areaModels_c, areaModels_s);

        }
    }

    private void showPop(View view, String flag) {
        popupWindow.setWidth(view.getWidth() + 24);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        if (flag.equals("1")) {
            provinceAdapter = new ProvinceAdapter(getActivity(), areaModels_p, mProvinceView);
            listView.setAdapter(provinceAdapter);

            if (provinceAdapter.getCount() >= 4) {
                popupWindow.setHeight(mPopHeight);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    current_P = provinceAdapter.getItem(position);
                    areaModels_c = current_P.city;
                    mProvinceView.setText(current_P.p_name);
                    provinceId = current_P.p_id;
                    provinceCode = current_P.p_code;
                    mCityView.setClickable(true);
                    mCityView.setFocusable(true);
                    mCityView.setText("");
                    mRegionView.setText("");
                    mCityView.setOnClickListener(LookCarFragment.this);
                    popupWindow.dismiss();
                }
            });
        }
        if (flag.equals("2")) {
            cityAdapter = new CityAdapter(getActivity(), areaModels_c, mCityView);
            listView.setAdapter(cityAdapter);

            if (cityAdapter.getCount() >= 4) {
                popupWindow.setHeight(mPopHeight);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    current_C = cityAdapter.getItem(position);
                    areaModels_s = current_C.region;
                    mCityView.setText(current_C.c_name);
                    cityCode = current_C.c_code;
                    cityId = current_C.c_id;
                    mRegionView.setClickable(true);
                    mRegionView.setFocusable(true);
                    mRegionView.setText("");
                    mRegionView.setOnClickListener(LookCarFragment.this);
                    popupWindow.dismiss();
                }
            });
        }
        if (flag.equals("3")) {
            regionAdapter = new RegionAdapter(getActivity(), areaModels_s, mRegionView);
            listView.setAdapter(regionAdapter);
            if (regionAdapter.getCount() >= 4) {
                popupWindow.setHeight(mPopHeight);
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    current_S = regionAdapter.getItem(position);
                    mRegionView.setText(current_S.r_name);
                    regionId = current_S.r_code;
                    popupWindow.dismiss();
                }
            });
        }
        if (popupWindow.isShowing()) return;
        popupWindow.showAsDropDown(view, -12, -view.getHeight());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    public interface ChooseAreaListener {
        void onSubmitComplete(String province1, String city1, String region1, String provinceId, String provinceCode, String cityId, String cityCode, String regionId, ArrayList<AreaModel> areaModels_p1, ArrayList<CityModel> areaModels_c1,
                              ArrayList<RegionModel> areaModels_s1);

        void initData();
    }
}
