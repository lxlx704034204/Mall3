package com.hxqc.mall.thirdshop.maintenance.adapter.divider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hxqc.mall.auto.adapter.HFRecyclerView;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ServiceTypeAdapter;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.views.ServiceTypeItemView;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 05
 * Des: 服务类型的AdapterV2
 * FIXME
 * Todo
 */

public class ServiceTypeAdapterV2 extends HFRecyclerView<ServiceType> {

    private static final String TAG = AutoInfoContants.LOG_J;
    private LayoutInflater mLayoutInflater;
    public ArrayList<ServiceType> mServiceTypeGroup;
    private Context mContext;
    private ServiceTypeAdapter.OnItemClickListener mOnItemClickListener;
    public HashMap<Integer, String> serviceTypeList;
    public ServiceType mServiceType;
    private int flag = 0;
    private EditText mServiceTypeRemarkContentView;

    public HashMap<Integer, String> getServiceTypeList() {
        return serviceTypeList;
    }

    public interface OnItemClickListener {
        void setOnItemClick(int position);
    }

    public void setOnItemClickListener(ServiceTypeAdapter.OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public ServiceTypeAdapterV2(Context context, ArrayList<ServiceType> data, ServiceType serviceType, boolean withHeader, boolean withFooter) {
        super(data, withHeader, withFooter);
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mServiceTypeGroup = data;
        serviceTypeList = new HashMap<Integer, String>();
        if (serviceType == null) {
            if (data != null && !data.isEmpty()) {
                mServiceType = data.get(0);
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).kindTitle.equals(serviceType.kindTitle)) {
                    flag = i;
                }
            }
        }
    }

    public void notifyData(ArrayList<ServiceType> serviceTypeGroup, ServiceType serviceType) {
        this.mServiceTypeGroup = serviceTypeGroup;
        for (int i = 0; i < serviceTypeGroup.size(); i++) {
            if (serviceTypeGroup.get(i).kindTitle.equals(serviceType.kindTitle)) {
                flag = i;
            }
        }
        notifyDataSetChanged();
    }

    private boolean mIsRed = false;

    public void notifyBackground(boolean isRed) {
        mIsRed = isRed;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_service_type_group, parent, false);
        ServiceTypeAdapterV2.ItemViewHolder itemViewHolder = new ServiceTypeAdapterV2.ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setData(mContext, mServiceTypeGroup.get(position));
            ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setAdapter(mContext, mServiceTypeGroup.get(position));
            if (position == flag) {
                ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.isChecked(true);
                if (position == mServiceTypeGroup.size() - 1) {
                    ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setRemarkVisibility(View.VISIBLE);
                    DebugLog.i(TAG, "flag: " + mServiceTypeGroup.get(position).toString());
                    ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setServiceTypeRemarkContent(mServiceTypeGroup.get(position).remark);
                } else {
                    ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setListVisibility(View.VISIBLE);
                }
            } else {
                ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setListVisibility(View.GONE);
                ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setRemarkVisibility(View.GONE);
                ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.isChecked(false);
            }
            if (mIsRed) {
                ((ServiceTypeAdapterV2.ItemViewHolder) holder).mServiceTypeItemView.setServiceTypeRemarkContentBackground(mIsRed);
            }
            setOnClickListener(((ServiceTypeAdapterV2.ItemViewHolder) holder), position);
        }
    }

    private void setOnClickListener(final ServiceTypeAdapterV2.ItemViewHolder holder, final int position) {
        mServiceTypeRemarkContentView = holder.mServiceTypeItemView.getmServiceTypeRemarkContentView();
        holder.mServiceTypeItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = position;
                holder.mServiceTypeItemView.isChecked(true);
                mServiceType = mServiceTypeGroup.get(position);
                saveRemarkText();
                notifyDataSetChanged();
            }
        });


        holder.mServiceTypeItemView.getmServiceChooseView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = position;
                holder.mServiceTypeItemView.isChecked(true);
                mServiceType = mServiceTypeGroup.get(position);
                saveRemarkText();
                notifyDataSetChanged();
            }

        });

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ServiceTypeItemView mServiceTypeItemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mServiceTypeItemView = (ServiceTypeItemView) itemView.findViewById(R.id.item_service_type);
        }
    }

    public String getServiceTypeRemarkContent() {
        return mServiceTypeRemarkContentView.getText().toString();
    }

    private void saveRemarkText() {
        if (mServiceTypeRemarkContentView.getVisibility() == View.VISIBLE) {
            mServiceTypeRemarkContentView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mServiceType.serviceType.equals("-1")) {
                        mServiceType.remark = s.toString();
                        mServiceTypeGroup.set(mServiceTypeGroup.size() - 1, mServiceType);
                    }
                }
            });
        }
    }
}
