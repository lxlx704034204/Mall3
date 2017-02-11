package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.CustomRecyclerView;
import com.hxqc.mall.core.views.VariousShowView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.activity.FilterAccessoryActivity;
import com.hxqc.mall.thirdshop.accessory.fragment.Filter.FilterCategoryFragment;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.AccessorySmallCategory;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.GridViewNoSlide;

import java.util.ArrayList;

/**
 * Function: 用品品系筛选Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月23日
 */
public class AccessoryGoodsCategoryAdapter extends RecyclerView.Adapter< AccessoryGoodsCategoryAdapter.ViewHolder > implements AccessorySmallCategoryAdapter.CallBack {
    private final static String TAG = AccessoryGoodsCategoryAdapter.class.getSimpleName();
    private Context mContext;

    private final static int TYPE_HEADER = 0;
    private final static int TYPE_NORMAL = 1;
    private FilterCategoryFragment filterCategoryFragment;
    private CallBack callBack;
    private ArrayList< AccessoryBigCategory > accessoryBigCategories;
    private boolean[] isFold;


    public AccessoryGoodsCategoryAdapter(Context mContext, ArrayList< AccessoryBigCategory > accessoryBigCategories) {
        DebugLog.e(TAG, "------------- Construct");
        this.mContext = mContext;
        this.accessoryBigCategories = accessoryBigCategories;
        isFold = new boolean[accessoryBigCategories.size()];
        for (int i = 0; i < isFold.length; i++) {
            isFold[i] = true;
        }

        if (mContext instanceof FilterAccessoryActivity) {
            Fragment fragment = ((FilterAccessoryActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.mdMenu);

            if (fragment != null && fragment instanceof FilterCategoryFragment) {
                filterCategoryFragment = (FilterCategoryFragment) fragment;
            }
        }
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DebugLog.e(TAG, " -------------- onCreateViewHolder");
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_header_no_limit, parent, false), viewType);
        } else if (viewType == TYPE_NORMAL) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_accessory_goods_category, parent, false), viewType);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DebugLog.e(TAG, " ------------ onBindViewHolder");
        if (getItemViewType(position) == TYPE_HEADER) {
            holder.mNoLimitConditionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack != null) callBack.onGoodsCategoryChooseCallBack(null, null);
                }
            });
        } else {
            if (position > 0 && position - 1 < accessoryBigCategories.size()) {
                final int pos = position - 1;
                ImageUtil.setImageSquare(mContext, holder.variousShowView.getmLeftIcon(),
                        accessoryBigCategories.get(pos).class1stIcon);


                holder.variousShowView.findViewById(R.id.root_view).getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, mContext.getResources().getDisplayMetrics());

                holder.variousShowView.getmLabelTextView().setText(accessoryBigCategories.get(pos).class1stName);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.variousShowView.getmLabelTextView().getLayoutParams();
                layoutParams.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, mContext.getResources().getDisplayMetrics()), 0, 0, 0);

                holder.variousShowView.setFold(isFold[pos]);

                holder.variousShowView.getmRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.variousShowView.doClick();

                        isFold[pos] = isFold[pos] == true ? false : true;

                        if (filterCategoryFragment != null) {
                            filterCategoryFragment.recyclerView.setScrollType(CustomRecyclerView.SMOOTHSCROLL);
                            filterCategoryFragment.recyclerView.move(position);
                        }
                    }
                });

                AccessorySmallCategoryAdapter accessorySmallCategoryAdapter = new AccessorySmallCategoryAdapter(mContext, accessoryBigCategories.get(pos).class2nd);
                accessorySmallCategoryAdapter.setCallBack(this);
                holder.gridViewNoSlide.setAdapter(accessorySmallCategoryAdapter);
            }
        }
    }


    @Override
    public int getItemCount() {
        DebugLog.e(TAG, " ---------- getItemCount");
        return accessoryBigCategories.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        DebugLog.e(TAG, "----------- getItemViewType");
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }


    @Override
    public void onSmallCategoryClickCallBack(int bigCategoryPosition, AccessorySmallCategory accessorySmallCategory) {
        if (callBack != null)
            callBack.onGoodsCategoryChooseCallBack(accessoryBigCategories.get(bigCategoryPosition - 1), accessorySmallCategory);
    }


    public interface CallBack {
        void onGoodsCategoryChooseCallBack(AccessoryBigCategory accessoryBigCategory, AccessorySmallCategory accessorySmallCategory);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public VariousShowView variousShowView;
        public GridViewNoSlide gridViewNoSlide;
        public View dividerLineView;
        public TextView mNoLimitConditionView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            DebugLog.e(TAG, "----------- ViewHolder");
            if (viewType == TYPE_HEADER) {
                mNoLimitConditionView = (TextView) itemView.findViewById(R.id.no_limit_condition);
            } else if (viewType == TYPE_NORMAL) {
                dividerLineView = itemView.findViewById(R.id.divider_line);
                variousShowView = (VariousShowView) itemView.findViewById(R.id.accessory_big_category);
                gridViewNoSlide = (GridViewNoSlide) itemView.findViewById(R.id.accessory_small_category);
            }
        }
    }
}
