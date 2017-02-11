package com.hxqc.carcompare.ui.addcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.carcompare.db.DAO;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel_Table;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.util.ToastHelper;

import org.greenrobot.eventbus.EventBus;

import hxqc.mall.R;

/**
 * 添加对比车型adapter
 * Created by zhaofan on 2016/10/9.
 */

public class CarCompareAdapter extends BaseAdapter<ChooseCarModel, BaseAdapter.BaseViewHolder> {

    private ListView lv;
    private boolean isEdit = false;

    public CarCompareAdapter(Context context) {
        super(context);
    }

    public CarCompareAdapter(Context context, ListView lv) {
        super(context);
        this.lv = lv;
    }


    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_compare, null, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final ChooseCarModel data) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.modelName.setText(data.getModelName());

        //checkbox
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isEdit && !lv.isItemChecked(position)
                        && DAO.queryCarCompareList(ChooseCarModel_Table.isCheck.eq(1)).size() > 9) {
                    ToastHelper.showYellowToast(context, "最多选择10款车型");
                    return;
                }

                lv.setItemChecked(position, !lv.isItemChecked(position));
                if (!isEdit) {
                    data.setIsCheck(lv.isItemChecked(position) ? 1 : 0);
                    data.update();
                    dataSource = DAO.queryCarCompareList();//DbHelper.query(ChooseCarModel.class);
                }
                notifyDataSetChanged();

                EventBus.getDefault().post(new Event("", CarCompareListActivity.UPDATA_STATUS));
            }
        });


        if (!isEdit) {
            lv.setItemChecked(position, data.getIsCheck() == 1);
        }
        vh.cb.setChecked(lv.isItemChecked(position));

    }

    public void setEditMode(boolean is) {
        this.isEdit = is;
    }


    private class ViewHolder extends BaseViewHolder {
        //店铺
        private TextView modelName;
        private CheckBox cb;
        public RelativeLayout main;

        public ViewHolder(View itemView) {
            super(itemView);

            modelName = getView(R.id.model_name);
            cb = getView(R.id.checkbox);
            main = getView(R.id.main);

        }
    }


}
