package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.CarType;

import java.util.ArrayList;

/**
 * Function:车型筛选的recyclerView的Adapter
 *
 * @author 袁秉勇
 * @since 2015年12月05日
 */
public class CarTypeRecyclerViewAdapter extends RecyclerView.Adapter {
    private final static int IS_HEADER = 0;
    private final static int IS_NORMAL = 1;
    ArrayList<CarType > carTypes = new ArrayList<>();
    Context mContext;
    CarTypeRecyclerViewCallBack mCarTypeRecyclerViewCallBack;

    public void setmCarTypeRecyclerViewCallBack(CarTypeRecyclerViewCallBack mCarTypeRecyclerViewCallBack) {
        this.mCarTypeRecyclerViewCallBack = mCarTypeRecyclerViewCallBack;
    }

    public CarTypeRecyclerViewAdapter(ArrayList<CarType> carTypes, Context context) {
        this.carTypes = carTypes;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        } else {
            return IS_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return carTypes.size() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.t_item_filter_factor_head, null);
            return new ViewHolder(view, IS_HEADER);
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.t_car_type_recyclerview_item, parent, false);
            return new ViewHolder(view, IS_NORMAL);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 && ((ViewHolder) holder).viewType == IS_HEADER) {
            ((ViewHolder) holder).mNoLimitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCarTypeRecyclerViewCallBack.carTypeCallBack(null);
                }
            });
        } else if (position > 0 && ((ViewHolder) holder).viewType == IS_NORMAL) {
            if (position - 1 >= 0 && position - 1 < carTypes.size()) {
                final CarType carType = carTypes.get(position - 1);
                ((ViewHolder) holder).mCarTypeNameView.setText(carType.modelName);
                ((ViewHolder) holder).mCarTypeNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCarTypeRecyclerViewCallBack.carTypeCallBack(carType);
                    }
                });
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCarTypeNameView;
        TextView mNoLimitView;
        int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            mNoLimitView = (TextView) itemView.findViewById(R.id.no_limit_condition);
            if (viewType == IS_HEADER) {
            } else if (viewType == IS_NORMAL) {
                mCarTypeNameView = (TextView) itemView.findViewById(R.id.car_type_name);
            }
        }
    }

    public interface CarTypeRecyclerViewCallBack {
        void carTypeCallBack(CarType carType);
    }
}
