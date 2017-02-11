package com.hxqc.aroundservice.view.mapbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.hxqc.aroundservice.model.MapButtonModel;
import com.hxqc.mall.amap.control.AMapAroundManager;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Created by wh on 2016/4/18.
 * TODO 地图 侧边 按钮
 */
public class MapAroundCheckButtonView extends RelativeLayout {

    MapButtonModel model;
    CheckBox imgCheckView;
    AMapAroundManager aroundManager;

    public MapAroundCheckButtonView(Context context, AMapAroundManager am) {
        super(context);
        this.aroundManager = am;
        initView();
    }

    public MapAroundCheckButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_function_button, this);
        imgCheckView  = (CheckBox) findViewById(R.id.map_button_icon);
        imgCheckView.setClickable(false);
    }

    /**
     * 设置 按钮数据
     *
     * @param model 按钮数据model
     */
    public void setModel(MapButtonModel model) {
        this.model = model;
        if (model != null) {
            imgCheckView.setButtonDrawable(model.buttonImgSrc);
            DebugLog.i("btn_map","setModel: "+model.toString());
        }
    }

    public void setChecked(boolean t) {
        imgCheckView.setChecked(t);
        model.isChoose  = t;
        managerChange();
    }

    private void managerChange(){
        switch(model.buttonTag){
            case MapButtonModel.BTN_ID_GAS:
                DebugLog.i("btn_map","managerChange: "+imgCheckView.isChecked()+" Tag : gas");
                aroundManager.showGasStation(imgCheckView.isChecked());
                break;
            case MapButtonModel.BTN_ID_PARK:
                DebugLog.i("btn_map","managerChange: "+imgCheckView.isChecked()+" Tag : park");
                aroundManager.searchAroundPark(imgCheckView.isChecked());
                break;
            case MapButtonModel.BTN_ID_CHARGER:
                DebugLog.i("btn_map","managerChange: "+imgCheckView.isChecked()+" Tag : charger");
                aroundManager.searchAroundCharger(imgCheckView.isChecked());
                break;
        }
    }

}
