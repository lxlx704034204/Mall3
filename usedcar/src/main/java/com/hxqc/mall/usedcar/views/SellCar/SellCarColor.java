package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.CarColor;
import com.hxqc.util.DisplayTools;

import java.util.ArrayList;

/**
 * @Author : 吕飞
 * @Since : 2016-12-15
 * FIXME
 * Todo
 */
public class SellCarColor extends SellCarItem implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener {
    PopupWindow popupWindow;
    ListView listView;
    ColorAdapter adapter;
    //pop 窗体的高度
    int mPopHeight = 550;
    private Context context;
    private ArrayList<CarColor> carColors = new ArrayList<>();

    public SellCarColor(Context context) {
        super(context);
    }


    public SellCarColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        initEvent();
    }


    public void initDate(ArrayList<CarColor> carColors) {
        this.carColors = carColors;
    }

    private void initEvent() {
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        popupWindow.dismiss();
        mChooseResultView.setText(carColors.get(position).detail_dic_name);
    }

    private void initView() {
        View view = View.inflate(context, R.layout.view_color_popwin, null);
        listView = (ListView) view.findViewById(R.id.lv_get_Area);
        adapter = new ColorAdapter();
        listView.setAdapter(adapter);
        popupWindow = new PopupWindow(view);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(this);
    }


    public void showPopWindow(final View view, final int x, final int y) {
        popupWindow.setWidth(DisplayTools.getScreenWidth(getContext())-DisplayTools.dip2px(getContext(),132));
        popupWindow.setHeight(mPopHeight);
        popupWindow.showAsDropDown(view, x, y);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    @Override
    public void onDismiss() {
        mChooseResultView.validate();
    }

    class ColorAdapter extends BaseAdapter {
        LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());

        @Override
        public int getCount() {
            return carColors.size();
        }

        @Override
        public Object getItem(int position) {
            return carColors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_used_car_color_spinner, parent, false);
            }
            ColorSquare mColorSquareView = (ColorSquare) convertView.findViewById(R.id.color_square);
            TextView mColorView = (TextView) convertView.findViewById(R.id.color_square_title);
            mColorSquareView.setColors(new String[]{carColors.get(position).detail_dic_code.trim()});
            mColorView.setText(carColors.get(position).detail_dic_name);
            return convertView;
        }
    }

}
