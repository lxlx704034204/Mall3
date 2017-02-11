package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.views.ServiceTypeItemView;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 22
 * Des: 服务类型的Adapter
 * FIXME
 * Todo
 */
public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ItemViewHolder> {

    private static final String TAG = AutoInfoContants.LOG_J;
    private LayoutInflater mLayoutInflater;
    public ArrayList<ServiceType> mServiceTypeGroup;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public HashMap<Integer, String> serviceTypeList;
    public ServiceType mServiceType;
    private EditText mServiceTypeRemarkContentView;


    public HashMap<Integer, String> getServiceTypeList() {
        return serviceTypeList;
    }

    public interface OnItemClickListener {
        void setOnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public ServiceTypeAdapter(Context context, ArrayList<ServiceType> serviceTypeGroup, ServiceType serviceType) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mServiceTypeGroup = serviceTypeGroup;
        serviceTypeList = new HashMap<Integer, String>();
        if (serviceType == null) {
            if (serviceTypeGroup != null && !serviceTypeGroup.isEmpty()) {
                mServiceType = serviceTypeGroup.get(0);
            }
        } else {
            for (int i = 0; i < serviceTypeGroup.size(); i++) {
                if (serviceTypeGroup.get(i).kindTitle.equals(serviceType.kindTitle)) {
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
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_service_type_group, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        ServiceTypeItemView mServiceTypeItemView = holder.mServiceTypeItemView;

        mServiceTypeItemView.setData(mContext, mServiceTypeGroup.get(position));
        mServiceTypeItemView.setAdapter(mContext, mServiceTypeGroup.get(position));
        if (position == flag) {
            mServiceTypeItemView.isChecked(true);
            if (position == mServiceTypeGroup.size() - 1) {
                mServiceTypeItemView.setRemarkVisibility(View.VISIBLE);
                DebugLog.i(TAG, "flag: " + mServiceTypeGroup.get(position).toString());
                mServiceType = mServiceTypeGroup.get(position);
                mServiceTypeItemView.setServiceTypeRemarkContent(mServiceTypeGroup.get(position).remark);
                mServiceTypeRemarkContentView = mServiceTypeItemView.getmServiceTypeRemarkContentView();
                mServiceTypeRemarkContentView.setFocusable(true);
                mServiceTypeRemarkContentView.setFocusableInTouchMode(true);
            } else {
                mServiceTypeItemView.setListVisibility(View.VISIBLE);
            }
        } else {
            mServiceTypeItemView.setListVisibility(View.GONE);
            mServiceTypeItemView.setRemarkVisibility(View.GONE);
            mServiceTypeItemView.isChecked(false);
        }
        if (mIsRed) {
            mServiceTypeItemView.setServiceTypeRemarkContentBackground(mIsRed);
        }

        setOnClickListener(mServiceTypeItemView, position);

        if (position == mServiceTypeGroup.size() - 1) {
            saveRemarkText();
        }

    }

    private int flag = 0;

    private void setOnClickListener(final ServiceTypeItemView serviceTypeItemView, final int position) {

        serviceTypeItemView.getServiceTypeItemLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = position;
                serviceTypeItemView.isChecked(true);
                mServiceType = mServiceTypeGroup.get(position);
                if (position == mServiceTypeGroup.size() - 1) {
                    mServiceTypeRemarkContentView = serviceTypeItemView.getmServiceTypeRemarkContentView();
                    mServiceTypeRemarkContentView.setFocusable(true);
                    mServiceTypeRemarkContentView.setFocusableInTouchMode(true);
                } else {
                    mServiceTypeRemarkContentView.setFocusable(false);
                    mServiceTypeRemarkContentView.setFocusableInTouchMode(false);
                }
                notifyDataSetChanged();
            }
        });

        /*mServiceTypeItemView.getmServiceChooseView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = position;
                mServiceTypeItemView.isChecked(true);
                mServiceType = mServiceTypeGroup.get(position);
                saveRemarkText();
                notifyDataSetChanged();
            }

        });*/

    }

    @Override
    public int getItemCount() {
        return mServiceTypeGroup != null ? mServiceTypeGroup.size() : 0;
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
        if (mServiceTypeRemarkContentView != null) {

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
