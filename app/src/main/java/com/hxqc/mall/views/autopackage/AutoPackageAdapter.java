package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxqc.mall.activity.auto.AutoPackageChooseActivity;
import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.views.dialog.PackageInfoDialog;

import java.util.Set;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2015-11-16
 * FIXME
 * Todo 商品列表的适配器
 */
public class AutoPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
//    private boolean isCustom;//是否是自定义
//    private Set<Accessory> selectGoods;//选中的商品
    private AutoPackage mAutoPackage;
//    private ArrayList<Accessory>mData;
//    private ArrayList<AutoPackage>mAutoPackage;
    private static final int VIEW_TYPE_HEAD = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    public AutoPackageAdapter(Context mContext, AutoPackage mAutoPackage) {
//        this.mData=mData;
        this.mContext = mContext;
        this.mAutoPackage = mAutoPackage;
//        this.isCustom = mAutoPackage.get(0).isCustomPackage() == AutoPackage.PackageTypeEnum.custom;
//        selectGoods = AutoPackage.getTempChooseAccessory();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEAD : VIEW_TYPE_NORMAL;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_TYPE_HEAD) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_auto_package_head, null);
             holder = new HeadViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_list, null);
             holder = new AutoViewHolder(view);
        }
        return holder;
    }

    private OnAdapterClickListener onAdapterClickListener;

    public void setOnAdapterClickListener(OnAdapterClickListener onItemSelectListener) {
        this.onAdapterClickListener = onItemSelectListener;
    }


    public interface OnAdapterClickListener {
        void onItemSelect(Accessory mAccessory, boolean isSelected);

        void onPackageSelect(View view, boolean isChecked);

        void onClearAllClick();
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        selectGoods = AutoPackage.getTempChooseAccessory();
        if (position == 0) {
            //head布局
            HeadViewHolder viewHolder = (HeadViewHolder) holder;
            viewHolder.packageTitle.setText(mAutoPackage.title);
            final CheckBox chooseButton = viewHolder.chooseButton;
            chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onAdapterClickListener)
                        onAdapterClickListener.onPackageSelect(v, chooseButton.isChecked());
                }
            });
            viewHolder.headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseButton.toggle();
                    if (null != onAdapterClickListener)
                        onAdapterClickListener.onPackageSelect(v, chooseButton.isChecked());
                }
            });
            viewHolder.clearChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onAdapterClickListener)
                        onAdapterClickListener.onClearAllClick();
                }
            });
            if (!(mAutoPackage.isCustomPackage() == AutoPackage.PackageTypeEnum.custom)) {
                viewHolder.chooseButton.setVisibility(View.VISIBLE);
                viewHolder.chooseButton.setChecked(isSelected(0));
                viewHolder.clearChoose.setVisibility(View.GONE);
                viewHolder.originalPrice.setVisibility(View.VISIBLE);
                //金额

                viewHolder.currentPrice.setText(String.format(mContext.getString(R.string.current_price),
                        OtherUtil.stringToMoney(mAutoPackage.getAmount())));
                viewHolder.originalPrice.setText(String.format(mContext.getString(R.string.original_price), OtherUtil.stringToMoney(mAutoPackage.getTotalAmount())));

            } else {
                viewHolder.chooseButton.setVisibility(View.GONE);
                Set<Accessory> chooseAccessorys = AutoPackage.getTempChooseAccessory();
                if (null != chooseAccessorys)
                    if (chooseAccessorys.size() == 0)
                        viewHolder.clearChoose.setVisibility(View.GONE);
                    else
                        viewHolder.clearChoose.setVisibility(View.VISIBLE);
                else
                    viewHolder.clearChoose.setVisibility(View.GONE);
                //金额
                viewHolder.currentPrice.setText(String.format(mContext.getString(R.string.current_price),
                        OtherUtil.stringToMoney(mAutoPackage.getCustomTempToalAmount())));
                viewHolder.originalPrice.setVisibility(View.GONE);
            }
            viewHolder.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中划线
        } else {
            boolean selected = isSelected(position - 1);
            AutoViewHolder autoViewHolder = (AutoViewHolder) holder;
            autoViewHolder.goodsItem.setAccessory(mAutoPackage.accessory.get(position - 1), selected,
                    mAutoPackage.isCustomPackage() == AutoPackage.PackageTypeEnum.custom);
//            autoViewHolder.goodsItem.setTag(position-1);
            autoViewHolder.goodsItem.setOnItemSelectListener(new GoodsItem.OnItemSelectListener() {
                @Override
                public void onItemSelect(Accessory mAccessory, boolean isSelected) {
                    notifyDataSetChanged();
                    if (null != onAdapterClickListener)
                        onAdapterClickListener.onItemSelect(mAccessory, isSelected);
                }
            });
            autoViewHolder.goodsItem.setOnClickListener(new GoodsItem.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PackageInfoDialog(mContext, mAutoPackage.accessory.get(position - 1)).show();
                }
            });
        }
    }

    public boolean isSelected(int position) {
        if (mAutoPackage.isCustomPackage() == AutoPackage.PackageTypeEnum.custom) {
            if (null == AutoPackage.getTempChooseAccessory())
                return false;
            for (int i = 0; i < AutoPackage.getTempChooseAccessory().size(); i++) {
                if (AutoPackage.getTempChooseAccessory().contains(mAutoPackage.accessory.get(position)))
                    return true;
            }
        } else {
            AutoPackage tempChoosePackage = AutoPackageChooseActivity.getTempChoosePackage();
            return null != tempChoosePackage && tempChoosePackage.equals(mAutoPackage);
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (null == mAutoPackage)
            return 0;
        else
            return mAutoPackage.accessory == null ? 0 : mAutoPackage.accessory.size() + 1;
    }


    public class AutoViewHolder extends RecyclerView.ViewHolder {
        private GoodsItem goodsItem;

        public AutoViewHolder(View itemView) {
            super(itemView);
            goodsItem = (GoodsItem) itemView.findViewById(R.id.goods_item);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        private TextView packageTitle, originalPrice, currentPrice, clearChoose;
        private CheckBox chooseButton;
        private View headView;

        public HeadViewHolder(View itemView) {
            super(itemView);
            headView = itemView;
            packageTitle = (TextView) itemView.findViewById(R.id.package_title);
            originalPrice = (TextView) itemView.findViewById(R.id.original_price);
            currentPrice = (TextView) itemView.findViewById(R.id.current_price);
            clearChoose = (TextView) itemView.findViewById(R.id.clear_choose);
            chooseButton = (CheckBox) itemView.findViewById(R.id.package_cb);
        }
    }
}
