package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.amap.api.maps.MapView;
import com.hxqc.aroundservice.model.MapButtonModel;
import com.hxqc.aroundservice.view.mapbutton.MapAroundCheckButtonView;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.amap.control.AMapAroundManager;
import com.hxqc.mall.amap.control.MapMarkerType;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

public class AroundServiceAMapActivity extends NoBackActivity implements View.OnClickListener, AMapAroundManager.OnLocationInitSuccessListener, AMapAroundManager.OnDataRequestListener {

    final public static String AROUND_MAP_TAG = "mapType";//传递tag
    AMapAroundManager aroundManager;
    CheckBox mRoadView;
    ImageButton mRelocView;
    ImageButton mIncreaseView;
    ImageButton mReduceView;

    LinearLayout checkButtons;
    MapAroundCheckButtonView checkedButtonView;
    ArrayList<MapButtonModel> mapButtonModels;
    private int intExtra = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_service_amap);

//        intExtra = getIntent().getIntExtra(AROUND_MAP_TAG, -1);
        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            intExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(AROUND_MAP_TAG);
        } else {
            intExtra =getIntent().getIntExtra(AROUND_MAP_TAG, -1);
        }


        MapView mapView = (MapView) findViewById(R.id.around_map);
        aroundManager = AMapAroundManager.getInstance();
        aroundManager.setOnLocationInitSuccessListener(this);
        aroundManager.setOnDataRequestListener(this);
        aroundManager.onCreate(savedInstanceState, this, mapView);
        initView();
        //back
        ImageButton back = (ImageButton) findViewById(R.id.around_map_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        checkButtons = (LinearLayout) findViewById(R.id.map_sidle_buttons);
        mRoadView = (CheckBox) findViewById(R.id.around_map_road);
        mRelocView = (ImageButton) findViewById(R.id.around_map_reloc);
        mIncreaseView = (ImageButton) findViewById(R.id.around_map_increase);
        mReduceView = (ImageButton) findViewById(R.id.around_map_reduce);

        mRoadView.setOnClickListener(this);
        mRelocView.setOnClickListener(this);
        mIncreaseView.setOnClickListener(this);
        mReduceView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.around_map_road) {
            //路况
            if (mRoadView.isChecked()) {
                aroundManager.openTraffic();
            } else {
                aroundManager.closeTraffic();
            }

        } else if (v.getId() == R.id.around_map_reloc) {
            //重新定位
            aroundManager.reLoc();
        } else if (v.getId() == R.id.around_map_increase) {
            //放大地图
            aroundManager.increaseMapView();
        } else if (v.getId() == R.id.around_map_reduce) {
            //缩小地图
            aroundManager.reduceMapView();
        }
    }

    //------------------生命周期重写函数---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        aroundManager.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        aroundManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        aroundManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        aroundManager.onDestroy();
    }

    @Override
    public void finish() {
        aroundManager.onDestroy();
        super.finish();
    }

    @Override
    public void onLocationChangeSuccess() {
        operateInitData();
    }

    //操作初始化数据
    private void operateInitData() {
        initButtonDatas();
        switch (intExtra) {
            case MapMarkerType.GasStationType:
                mapButtonModels.get(0).isChoose = true;
                break;
            case MapMarkerType.ParkType:
                mapButtonModels.get(1).isChoose = true;
                break;
            case MapMarkerType.ChargerStationType:
                mapButtonModels.get(2).isChoose = true;
                break;
            case MapMarkerType.TrafficType:
                mRoadView.setChecked(true);
                aroundManager.openTraffic();
                break;
            default:
                break;
        }

        initRadioFunctionButtons();
    }

    /**
     * 初始化 按钮信息
     */
    private void initButtonDatas() {
        mapButtonModels = new ArrayList<>();

        MapButtonModel gasButton = new MapButtonModel(R.drawable.bg_map_around_gas_selecter, MapButtonModel.BTN_ID_GAS);
        mapButtonModels.add(gasButton);

        MapButtonModel parkButton = new MapButtonModel(R.drawable.bg_map_around_park_selecter, MapButtonModel.BTN_ID_PARK);
        mapButtonModels.add(parkButton);

        MapButtonModel chargerButton = new MapButtonModel(R.drawable.bg_map_around_charger_selecter, MapButtonModel.BTN_ID_CHARGER);
        mapButtonModels.add(chargerButton);

    }

    ArrayList<MapAroundCheckButtonView> buttonViews = new ArrayList<>();

    /**
     * 初始化 功能按钮
     */
    private void initRadioFunctionButtons() {
        for (int i = 0; i < mapButtonModels.size(); i++) {
            final MapButtonModel model = mapButtonModels.get(i);
            MapAroundCheckButtonView itemView = new MapAroundCheckButtonView(AroundServiceAMapActivity.this, aroundManager);
            DebugLog.i("btn_map", "initRadioFunctionButtons: " + model.toString());
            itemView.setModel(model);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPaymentChange((MapAroundCheckButtonView) v, model.buttonTag);
                }
            });

            if (model.isChoose) {
                checkedButtonView = itemView;
                checkedButtonView.setChecked(true);
                checkedButtonView.setTag(model.buttonTag);
            }

            buttonViews.add(itemView);
            checkButtons.addView(itemView);
        }
    }

    /**
     * 设置是否可以点击
     */
    private void setViewClickable(boolean b) {
        for (int i = 0; i<buttonViews.size();i++){
            buttonViews.get(i).setClickable(b);
        }
    }

    /**
     * 按钮 点击操作
     *
     * @param v   点击的view
     * @param tag 点击的view 的标识
     */
    private void checkPaymentChange(MapAroundCheckButtonView v, String tag) {
        DebugLog.i("btn_map", "checkPaymentChange: " + tag);

        setViewClickable(false);

        if (checkedButtonView == null) {
            checkedButtonView = v;
            checkedButtonView.setChecked(true);
            checkedButtonView.setTag(tag);
        } else {
            checkedButtonView.setChecked(false);
            if (checkedButtonView.getTag().equals(tag)) {
                checkedButtonView = null;
            } else {
                checkedButtonView = v;
                checkedButtonView.setChecked(true);
                checkedButtonView.setTag(tag);
            }
        }
    }

    @Override
    public void requestSuccess() {
                DebugLog.w("btn_map", "managerChange: requestSuccess");
                setViewClickable(true);
    }

    @Override
    public void requestFail() {
                DebugLog.w("btn_map", "managerChange: requestFail");
                setViewClickable(true);
    }
}
