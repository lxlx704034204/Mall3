package com.hxqc.mall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.ColorInformation;
import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.core.views.SpinnerPopWindow;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-08
 * FIXME
 * Todo
 */
public class ColorSpinner extends LinearLayout implements AdapterView.OnItemClickListener {
    SpinnerPopWindow mSpinnerPopWindow;
    ArrayList< ColorInformation > colorInformationList;//实际
    ColorAdapter mColorAdapter;
    TextView mLabelView;
    ArrayList< ColorInformation > tColorInformationList;//显示
    String mLabelString;
    AdapterView.OnItemClickListener onItemClickListener;

    public ColorSpinner(Context context) {
        super(context);
    }

    public ColorSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ColorSpinner);
        mLabelString = typedArray.getString(R.styleable.ColorSpinner_spinnerLabel);
        typedArray.recycle();
        LayoutInflater.from(context).inflate(R.layout.view_color_spinner, this);
        mSpinnerPopWindow = (SpinnerPopWindow) findViewById(R.id.color_spinner);
        mLabelView = (TextView) findViewById(R.id.color_spinner_label);
        mLabelView.setText(mLabelString);
    }

    public void setColorInformationList(ArrayList< ColorInformation > colorInformationList) {
        if (colorInformationList == null) {
            return;
        }
        this.colorInformationList = colorInformationList;
//        tColorInformationList = (ArrayList< ColorInformation >) colorInformationList.clone();
        tColorInformationList = new ArrayList<>();
        for (ColorInformation cl : colorInformationList) {
            ColorInformation tColorInformation = (ColorInformation) cl.clones();
            tColorInformationList.add(tColorInformation);
        }
        mColorAdapter = new ColorAdapter(tColorInformationList);
        mSpinnerPopWindow.setAdapter(mColorAdapter);
        mColorAdapter.notifyDataSetChanged();
        mSpinnerPopWindow.setOnItemClickList(this);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

//    /**
//     * 刷新
//     */
//    public void notifyColors(ArrayList< ColorInformation > colorInformationList) {
//        mSpinnerPopWindow.clearValue();
//        this.colorInformationList = colorInformationList;
//        tColorInformationList = (ArrayList< ColorInformation >) colorInformationList.clone();
//        mColorAdapter = new ColorAdapter(tColorInformationList);
//        mSpinnerPopWindow.setAdapter(mColorAdapter);
//    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        if (this.onItemClickListener != null)
            onItemClickListener.onItemClick(parent, view, position, id);
        setCurrentItem(position);
    }

    public void setCurrentItem(int position) {
        if (position == -1) return;
        mSpinnerPopWindow.setCurrentItem(position);
        tColorInformationList = new ArrayList<>();
        for (ColorInformation cl : colorInformationList) {
            ColorInformation tColorInformation = (ColorInformation) cl.clones();
            tColorInformationList.add(tColorInformation);
        }
        ColorInformation colorInformation = mColorAdapter.getItem(position);
        tColorInformationList.remove(colorInformation);
        mColorAdapter.notifyData(tColorInformationList);
    }

    public class ColorAdapter extends BaseAdapter {
        LayoutInflater mLayoutInflater;
        ArrayList< ColorInformation > tColorInformationList;

        public ColorAdapter(ArrayList< ColorInformation > tColorInformationList) {
            mLayoutInflater = LayoutInflater.from(getContext());
            this.tColorInformationList = tColorInformationList;
        }

        public void notifyData(ArrayList< ColorInformation > tColorInformationList) {
            this.tColorInformationList = tColorInformationList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return this.tColorInformationList == null ? 0 : this.tColorInformationList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public ColorInformation getItem(int position) {
            return this.tColorInformationList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_auto_color_spinner, parent, false);
            }
            ColorSquare mColorSquareView = (ColorSquare) convertView.findViewById(R.id.color_squares);
            TextView mLabelView = (TextView) convertView.findViewById(R.id.color_square_title);
            ColorInformation colorInformation = getItem(position);
            mColorSquareView.setColors(colorInformation.getColors());

            mLabelView.setText(colorInformation.colorDescription);
            return convertView;
        }
    }
}
