package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ThirdArea;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月07日
 */
public class ThirdCityChooseAdapter extends BaseAdapter {
    ArrayList<ThirdArea > thirdAreas = new ArrayList<>();
    Context context;
    String chooseStr = "";

    public ThirdCityChooseAdapter(Context context, ArrayList<ThirdArea> thirdAreas) {
        this.context = context;
        this.thirdAreas = thirdAreas;
    }

    @Override
    public int getCount() {
        return thirdAreas != null ? thirdAreas.size() : 0;
    }

    public void setChoosedColorChange(ArrayList<ThirdArea> thirdAreas, String str) {
        this.thirdAreas = thirdAreas;
        this.chooseStr = str;
        notifyDataSetChanged();
    }

    @Override
    public ThirdArea getItem(int position) {
        return thirdAreas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.item_list_choose, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_area);

//        if (chooseStr.equals(getItem(position).provinceName)) {
//            textView.setTextColor(context.getResources().getColor(R.color.cursor_orange));
//        } else {
//            textView.setTextColor(context.getResources().getColor(R.color.straight_matter_and_secondary_text));
//        }
        ThirdArea thirdArea = getItem(position);

        textView.setText(thirdArea.province);
        return view;
    }

    class ViewHolder {
        public TextView area;
    }
}
