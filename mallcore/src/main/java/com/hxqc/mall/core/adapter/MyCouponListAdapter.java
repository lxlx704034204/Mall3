package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.coupon.MyCouponActivity;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-02
 * FIXME
 * Todo 我的优惠券列表适配器
 */
public class MyCouponListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_USABLE = 10;
    private static final int ITEM_TYPE_EXPIRED = 20;
    private static final int ITEM_TYPE_USED = 30;
    private static final int ITEM_TYPE_NOT_START = 40;
    private static final int ITEM_TYPE_FOOT = 50;
    //    public ArrayList<Coupon> selectCoupons = new ArrayList<>();
    private ArrayList<Coupon> mData;
    private Context mContext;
    private boolean canChoose = false;//选择模式
    private OnCouponSelectListener onCouponSelectListener;

//    public void setCouponCombinations(ArrayList<CouponCombination> couponCombinations) {
//        this.couponCombinations = couponCombinations;
//    }

//    private ArrayList<CouponCombination> couponCombinations = new ArrayList<>();

    public MyCouponListAdapter(ArrayList<Coupon> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_list_footer, null);
            return new FootViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_list_new, null);
            return new ViewHolder(view);
        }
    }

    public void setCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOT;
        } else {
            String statusCode = mData.get(position).statusCode;
            if (!TextUtils.isEmpty(statusCode))
                try {
                    return Integer.parseInt(statusCode);
                } catch (NumberFormatException e) {
                    return -1;
                }
            else return -1;
        }
    }

    public void setOnCouponSelectListener(OnCouponSelectListener onCouponSelectListener) {
        this.onCouponSelectListener = onCouponSelectListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE_FOOT) {
            FootViewHolder viewHolder = (FootViewHolder) holder;
            viewHolder.button.setVisibility(canChoose ? View.VISIBLE : View.GONE);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof MyCouponActivity) {
                        ((MyCouponActivity) mContext).doNotUseCoupon();
                    }
                }
            });
        } else {
            setNormalItems((ViewHolder) holder, position, itemViewType);
        }
    }

    private void setNormalItems(ViewHolder holder, final int position, int itemViewType) {
        int bgID = R.drawable.ic_coupon_normal;

        switch (itemViewType) {
            case ITEM_TYPE_USED:
                bgID = R.drawable.ic_coupon_used;
                break;
            case ITEM_TYPE_USABLE:
                bgID = R.drawable.ic_coupon_normal;

                break;
            case ITEM_TYPE_EXPIRED:
                bgID = R.drawable.ic_coupon_overtime;
                break;
            case ITEM_TYPE_NOT_START:
                bgID = R.drawable.ic_coupon_not_start;
                break;
            default:
                break;
        }
        final CheckBox cb = holder.checkBox;

        holder.checkBox.setVisibility(canChoose ? View.VISIBLE : View.GONE);
        if (canChoose) {
//            holder.checkBox.setEnabled(checkBoxState(mData.get(position).couponID));
//            holder.checkBox.setEnabled(mData.get(position).isChoose == 1);
            holder.checkBox.setChecked(mData.get(position).isChoose == 1);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOne(position, cb);
            }
        });

        holder.rootLayout.setBackgroundResource(bgID);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canChoose)
                    ActivitySwitchMallCore.toCouponDetail(mContext, mData.get(position));
                else {
                    cb.toggle();
                    chooseOne(position, cb);
                }
            }
        });

        Coupon coupon = mData.get(position);

        holder.usableStore.setText(coupon.shopName);
        holder.usableDate.setText(String.format("%s-%s", coupon.startDate, coupon.endDate));
        String price = coupon.price;
        if (!TextUtils.isEmpty(price))
//            holder.money.setText(coupon.price);
            holder.money.setText(OtherUtil.reformatPriceNoEndZero(coupon.price));
//        holder.coupon_type.setText(String.format("仅限%s使用", coupon.kind));
        holder.coupon_type.setText(coupon.kind);
//        holder.coupon_type.setText(coupon.name);
    }

    private void chooseOne(int position, CheckBox cb) {
        Coupon coupon = null;

        for (Coupon c : mData) {
            if (c.isChoose == 1)
                coupon = c;
        }
        if (cb.isChecked()) {
            if (coupon != null) {
                coupon.isChoose = 0;
            }
            mData.get(position).isChoose = 1;
            log();
//            selectCoupons.remove(coupon);
//            DebugLog.i("selectCoupons", selectCoupons.size() + "remove");
        } else {
            cb.setChecked(true);
//            mData.get(position).isChoose = 0;
//            selectCoupons.add(coupon);
//            DebugLog.i("selectCoupons", selectCoupons.size() + "add");
        }
        notifyDataSetChanged();
        if (onCouponSelectListener != null)
            onCouponSelectListener.onSelect(position);
    }

    private void log() {
        for (Coupon c : mData) {
            if (c.isChoose == 1) {
                DebugLog.d("selectCoupons", c.couponID);
            }
        }
    }

    /**
     * 判断优惠券是否可以选
     *
     * @return
     */
//    private boolean checkBoxState(String couponID) {

//        if (selectCoupons.size() == 0)
//            return true;
    //遍历所有可用的组合
//        for (int i = 0; i < couponCombinations.size(); i++) {
//            ArrayList<String> couponIDs = couponCombinations.get(i).couponIDs;
//            if (isContain(couponIDs) && couponIDs.contains(couponID)) {
//                //当前的组合包括已选的和点击的这个
//                return true;
//            }
//        }
//        return false;
//    }

//    /**
//     * 是否包含当前所有已选的优惠券
//     *
//     * @param arrayList
//     * @return
//     */
//    private boolean isContain(ArrayList<String> arrayList) {
//        ArrayList<String> selectCouponID = getSelectCouponID();
//        for (String string : selectCouponID) {
//            if (!arrayList.contains(string))
//                return false;
//        }
//        return true;
//    }

    /**
     * 选择的优惠券的id
     *
     * @return
     */
//    private ArrayList<String> getSelectCouponID() {
//        ArrayList<String> selectedCouponIDs = new ArrayList<>();
////        for (Coupon coupon : selectCoupons) {
////            selectedCouponIDs.add(coupon.couponID);
////        }
//        for (Coupon coupon : mData) {
//            if (coupon.isChoose == 1)
//                selectedCouponIDs.add(coupon.couponID);
//        }
//        return selectedCouponIDs;
//    }
    @Override
    public int getItemCount() {
        return mData == null ? 1 : mData.size() + 1;
    }

    public interface OnCouponSelectListener {
        void onSelect(int position);
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public FootViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.do_not_use_coupon_btn);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout rootLayout,
                leftLayout;
        private TextView money,
        //                /*couponStatus,*/
        coupon_type,
        /*usableProgram,*/ usableStore, usableDate;
        private CheckBox checkBox;
//        private ImageView stateIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.root_layout);
            leftLayout = (LinearLayout) itemView.findViewById(R.id.coupon_left_info);
            money = (TextView) itemView.findViewById(R.id.money);
//            couponStatus = (TextView) itemView.findViewById(R.id.coupon_status);
//            usableProgram = (TextView) itemView.findViewById(R.id.usable_program);
            usableStore = (TextView) itemView.findViewById(R.id.usable_store);
            usableDate = (TextView) itemView.findViewById(R.id.usable_date);
            coupon_type = (TextView) itemView.findViewById(R.id.coupon_type);
            checkBox = (CheckBox) itemView.findViewById(R.id.coupon_checkbox);
//            stateIcon = (ImageView) itemView.findViewById(R.id.state_icon);
        }
    }
}
