package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaofan
 * 车型选择
 */
public class CarModelChooseAdapter extends BaseListViewAdapter<ModelInfo, BaseListViewAdapter.BaseViewHolder> {
    Context context;
    private String chooseStr;

    public CarModelChooseAdapter(Context context) {
        super(context);
        this.context = context;
    }


    public void setChoose(String chooseStr) {
        this.chooseStr = chooseStr;

    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_carmodel_choose, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final ModelInfo data) {
        ViewHolder viewHodler = (ViewHolder) holder;
        viewHodler.modelName.setText(data.getModelName());

        viewHodler.modelName.setTextColor(chooseStr.endsWith(data.getModelName()) ?
                context.getResources().getColor(R.color.text_color_orange) :
                context.getResources().getColor(R.color.filter_text_black));

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(data);
            }
        });


    }


    private class ViewHolder extends BaseViewHolder {
        private TextView modelName;

        public ViewHolder(View itemView) {
            super(itemView);
            modelName = getView(R.id.text1);

        }
    }

}
