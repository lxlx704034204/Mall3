package com.hxqc.mall.views.auto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.ColorInformation;
import com.hxqc.mall.views.ColorSpinner;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/13
 * FIXME
 * Todo
 */
public class AutoCommonColorGroup extends RelativeLayout {

    public interface OnAutoCommonColorGroupSelectListener {
        void onSelectColorAction(String itemID);
    }

    //外观
    ColorSpinner mColorAppearanceView;
    //内饰
    ColorSpinner mColorInteriorView;
    OnAutoCommonColorGroupSelectListener mSelectListener;

    public AutoCommonColorGroup(Context context) {
        super(context);
    }

    public AutoCommonColorGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_color_change, this);
        mColorAppearanceView = (ColorSpinner) findViewById(R.id.color_appearance);
        mColorInteriorView = (ColorSpinner) findViewById(R.id.color_interior);

    }

    public void setOnColorSelectListener(OnAutoCommonColorGroupSelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    //颜色设置
    public void colorConfig(AutoDetail autoDetail) {
        if (autoDetail == null) return;
        final AutoBaseInformation baseInformation = autoDetail.getAutoBaseInformation();
        //外观颜色
        ArrayList< ColorInformation > colorAppearance = baseInformation.getAppearance();
        mColorAppearanceView.setColorInformationList(colorAppearance);
        GetColorCurrentItemPosition getAppearanceColorPosition = new GetColorCurrentItemPosition
                (colorAppearance).invoke(autoDetail.getItemColorDescription());
        mColorAppearanceView.setCurrentItem(getAppearanceColorPosition.getPosition());
        mColorAppearanceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                //刷新内饰颜色
                ColorInformation colorInformation = (ColorInformation) parent.getAdapter().getItem(position);
                ArrayList< ColorInformation > colorInterior = colorInformation.interior;
                //无内饰
                if (colorInterior == null || colorInterior.size() <= 0) {
                    mColorInteriorView.setVisibility(View.GONE);
                    if (mSelectListener != null) {
                        mSelectListener.onSelectColorAction(colorInformation.itemID);
                    }
                    return;
                }
                //有内饰选择
                ColorInformation information = null;
                //查找与当前车辆内饰颜色额相同的车辆
                for (ColorInformation interiorColorInformation : colorInterior) {
                    if (interiorColorInformation.getColor().equals(baseInformation.getItemInterior())) {
                        information = interiorColorInformation;
                        break;
                    }
                }
                //无与当前车辆内饰颜色相同的，取内饰第一种
                if (information == null) {
                    information = colorInterior.get(0);
                }
                if (mSelectListener != null) {
                    //刷新车辆
                    mSelectListener.onSelectColorAction(information.itemID);
                }
            }
        });

        //内饰颜色
        if (getAppearanceColorPosition.getColorInformation() == null)
            return;
        ArrayList< ColorInformation > colorInterior = getAppearanceColorPosition.getColorInformation().interior;
        GetColorCurrentItemPosition getInteriorColorPosition = new GetColorCurrentItemPosition
                (colorInterior).invoke(autoDetail.getItemInteriorDescription());
        mColorInteriorView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {

                if (mSelectListener != null) {
                    //刷新车辆
                    mSelectListener.onSelectColorAction(((ColorInformation) parent.getItemAtPosition(position)).itemID);
                }
            }
        });
        if (colorInterior.size() <= 0) {
            mColorInteriorView.setVisibility(View.GONE);
            return;
        }
        mColorInteriorView.setColorInformationList(colorInterior);
        mColorInteriorView.setCurrentItem(getInteriorColorPosition.getPosition());

    }

    /**
     * 颜色筛选
     */
    protected class GetColorCurrentItemPosition {
        ColorInformation colorInformation;
        private ArrayList< ColorInformation > colorAppearance;
        private int position = 0;

        public GetColorCurrentItemPosition(ArrayList< ColorInformation > colorAppearance) {
            this.colorAppearance = colorAppearance;
        }

        public ColorInformation getColorInformation() {
            return colorInformation;
        }

        public int getPosition() {
            return position;
        }

        public GetColorCurrentItemPosition invoke(String color) {
            for (ColorInformation colorInformation : colorAppearance) {
                if (colorInformation.colorDescription.equals(color)) {
                    this.colorInformation = colorInformation;
                    return this;
                }
                position++;
            }
            position = -1;
            return this;
        }
    }
}
