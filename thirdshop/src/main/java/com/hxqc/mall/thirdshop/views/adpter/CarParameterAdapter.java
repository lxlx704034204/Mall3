package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.newcar.ParameterInfo;
import com.hxqc.util.DebugLog;

import java.util.List;

/**
 * 车型基本信息
 * Created by zhaofan
 */
public class CarParameterAdapter extends BaseListViewAdapter<ParameterInfo, BaseListViewAdapter.BaseViewHolder> {
    int maxWidth;
    String a = "";

    public CarParameterAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_parameter2, parent, false));
    }


    @Override
    public void setData(List<ParameterInfo> dataSource) {
        super.setData(dataSource);

        int max = dataSource.get(0).label.length();
        for (int i = 0; i < dataSource.size(); i++) {
            if (max < dataSource.get(i).label.length()) {
                max = dataSource.get(i).label.length();
                a = dataSource.get(i).label;
            }
        }
        if (a.endsWith(")")) {
            int index = a.indexOf("(");
            a = a.substring(0, index);
        }

        DebugLog.e("ParameterInfo", max + " " + a);

    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, int position, final ParameterInfo entity) {
        ViewHolder viewHodler = (ViewHolder) holder;

        maxWidth = (int) (a.length() * viewHodler.tv1.getTextSize());
        ViewGroup.LayoutParams pp = viewHodler.tv1.getLayoutParams();
        pp.width = maxWidth;
        viewHodler.tv1.setLayoutParams(pp);
        DebugLog.e("width", maxWidth + " ");

        String newLabel = entity.label;
        String unit = "";
        if (newLabel.endsWith(")")) {
            int index = newLabel.indexOf("(");
            newLabel = newLabel.substring(0, index);
            unit = entity.label.substring(index, entity.label.length());
        }

        viewHodler.tv1.setText(newLabel);
        viewHodler.tv2.setText(entity.value + unit);
    }

    private class ViewHolder extends BaseViewHolder {
        private TextView tv1, tv2;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = getView(R.id.tv1);
            tv2 = getView(R.id.tv2);
        }
    }

}
