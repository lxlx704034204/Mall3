package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.me.DeliveryAddressActivity;
import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.dialog.NormalDialog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:收货地址数据
 *
 * author: 吕飞
 * since: 2015-03-30
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class DeliveryAddressAdapter extends RecyclerView.Adapter {
    ArrayList<DeliveryAddress> mDeliveryAddresses;
    Context mContext;
    DeliveryAddressActivity mDeliveryAddressActivity;
    boolean mShowDefault;

    public DeliveryAddressAdapter(ArrayList<DeliveryAddress> mDeliveryAddresses, DeliveryAddressActivity mDeliveryAddressActivity, Context mContext, boolean showDefault) {
        this.mDeliveryAddresses = mDeliveryAddresses;
        this.mDeliveryAddressActivity = mDeliveryAddressActivity;
        this.mContext = mContext;
        this.mShowDefault = showDefault;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DeliveryAddress mDeliveryAddress = mDeliveryAddresses.get(position);
        OtherUtil.setCardViewMargin(mContext, ((ViewHolder) holder).mCardView, position, mDeliveryAddresses.size());
        ((ViewHolder) holder).mRealNameView.setText(mDeliveryAddress.consignee);
        showDefault(mDeliveryAddress.isDefault, holder);
        ((ViewHolder) holder).mAddressView.setText(Html.fromHtml(mDeliveryAddress.getAddress()));
        ((ViewHolder) holder).mDetailAddressView.setText(mDeliveryAddress.detailedAddress);
        ((ViewHolder) holder).mPhoneNumberView.setText(mContext.getResources().getString(R.string.me_phone) + mDeliveryAddress.getPhone());
        ((ViewHolder) holder).mCardView.setOnClickListener(new DeliveryAddressListener(position));
        ((ViewHolder) holder).mEditTextView.setOnClickListener(new DeliveryAddressListener(position));
        ((ViewHolder) holder).mEditIconView.setOnClickListener(new DeliveryAddressListener(position));
        ((ViewHolder) holder).mDeleteTextView.setOnClickListener(new DeliveryAddressListener(position));
        ((ViewHolder) holder).mDeleteIconView.setOnClickListener(new DeliveryAddressListener(position));
    }

    private void showDefault(int isDefault, RecyclerView.ViewHolder holder) {
        if (!mShowDefault) {
            ((ViewHolder) holder).mDefaultTextView.setText("");
        }
        if (isDefault == 1) {
            ((ViewHolder) holder).mDefaultTextView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).mDefaultAddressView.setImageResource(R.drawable.radio_button_selected);
        } else {
            ((ViewHolder) holder).mDefaultTextView.setVisibility(View.GONE);
            ((ViewHolder) holder).mDefaultAddressView.setImageResource(R.drawable.radio_button_normal);
        }
    }

    @Override
    public int getItemCount() {
        return mDeliveryAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mRealNameView;
        ImageView mDefaultAddressView;
        TextView mDefaultTextView;
        TextView mAddressView;
        TextView mDetailAddressView;
        TextView mPhoneNumberView;
        TextView mEditTextView;
        ImageView mEditIconView;
        TextView mDeleteTextView;
        ImageView mDeleteIconView;

        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mRealNameView = (TextView) v.findViewById(R.id.real_name);
            mDefaultAddressView = (ImageView) v.findViewById(R.id.default_address);
            mDefaultTextView = (TextView) v.findViewById(R.id.default_text);
            mAddressView = (TextView) v.findViewById(R.id.address);
            mDetailAddressView = (TextView) v.findViewById(R.id.detail_address);
            mPhoneNumberView = (TextView) v.findViewById(R.id.phone_number);
            mEditTextView = (TextView) v.findViewById(R.id.edit_text);
            mEditIconView = (ImageView) v.findViewById(R.id.edit_icon);
            mDeleteTextView = (TextView) v.findViewById(R.id.delete_text);
            mDeleteIconView = (ImageView) v.findViewById(R.id.delete_icon);
        }
    }

    private class DeliveryAddressListener implements View.OnClickListener {
        int position;

        private DeliveryAddressListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            switch (i) {
                case R.id.card_view:
//                    切换默认地址
                    for (int j = 0; j < getItemCount(); j++) {
                        mDeliveryAddresses.get(j).isDefault = 0;
                    }
                    mDeliveryAddresses.get(position).isDefault = 1;
                    DeliveryAddressActivity.sDefaultID = mDeliveryAddresses.get(position).addressID;
                    ((DeliveryAddressActivity)mContext).changeDefaultAddress();
                    notifyDataSetChanged();
                    break;
                case R.id.edit_text:
                case R.id.edit_icon:
                    ActivitySwitcher.toEditAddress(mContext, mDeliveryAddresses.get(position));
                    break;
                case R.id.delete_text:
                case R.id.delete_icon:
                    final NormalDialog mDeleteDialogView = new NormalDialog(mContext, mContext.getResources().getString(R.string.me_delete_address)) {
                        @Override
                        protected void doNext() {
                            deleteItem();
                        }
                    };
                    mDeleteDialogView.show();
                    break;
            }
        }

        //      删除地址操作
        private void deleteItem() {
            new ApiClient().deleteAddress(UserInfoHelper.getInstance().getToken(mContext), mDeliveryAddresses.get(position), new DialogResponseHandler(mContext, mContext.getResources().getString(R.string.me_deleting)) {
                @Override
                public void onSuccess(String response) {
                    boolean mNeedUpdate = false;
                    if (mDeliveryAddresses.size() > 1 && mDeliveryAddresses.get(position).isDefault == 1) {
                        mNeedUpdate = true;
                    }
                    mDeliveryAddresses.remove(mDeliveryAddresses.get(position));
                    notifyItemRemoved(position);
                    final boolean finalMNeedUpdate = mNeedUpdate;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DeliveryAddressAdapter.this.notifyDataSetChanged();
                            if (finalMNeedUpdate) {
                                mDeliveryAddressActivity.initData();
                            }
                        }
                    }, 370);
                }
            });
        }
    }
}
