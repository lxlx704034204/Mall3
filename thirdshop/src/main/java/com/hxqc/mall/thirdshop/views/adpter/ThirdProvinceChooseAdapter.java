package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ThirdAreaModel;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月07日
 */
public class ThirdProvinceChooseAdapter extends BaseAdapter {
    ArrayList<ThirdAreaModel > thirdAreaModels = new ArrayList<>();
    Context context;
    String chooseStr = "";

    public ThirdProvinceChooseAdapter(Context context, ArrayList<ThirdAreaModel> thirdAreaModels) {
        this.context = context;
        this.thirdAreaModels = thirdAreaModels;
    }

    @Override
    public int getCount() {
        return thirdAreaModels != null ? thirdAreaModels.size() : 0;
    }

    public void setChoosedColorChange(ArrayList<ThirdAreaModel> areaModels, String str) {
        this.thirdAreaModels = areaModels;
        this.chooseStr = str;
        notifyDataSetChanged();
    }

    @Override
    public ThirdAreaModel getItem(int position) {
        return thirdAreaModels.get(position);
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
        ThirdAreaModel thirdAreaModel = getItem(position);

        textView.setText(thirdAreaModel.provinceName);
        return view;
    }

    class ViewHolder {
        public TextView area;
    }
}
