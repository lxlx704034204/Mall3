package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ColorInfo;

import java.util.ArrayList;

/**
 * Function:第三方店铺车辆详情中颜色gridview的Adapter
 *
 * @author 袁秉勇
 * @since 2015年12月02日
 */
public class ColorDeatailItemAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ColorInfo> colorInfos = new ArrayList<>();

    public ColorDeatailItemAdapter(Context context, ArrayList<ColorInfo> colorInfos) {
        mContext = context;
        this.colorInfos = colorInfos;
    }

    @Override
    public int getCount() {
        return colorInfos.size();
    }

    @Override
    public ColorInfo getItem(int position) {
        return colorInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.t_color_detail_gridview_item, null);
            viewHolder.mColorSquare = (ColorSquare) convertView.findViewById(R.id.color_square);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mColorSquare.setColors(new String[]{getItem(position).color});
        return convertView;
    }

    class ViewHolder {
        ColorSquare mColorSquare;
    }
}
