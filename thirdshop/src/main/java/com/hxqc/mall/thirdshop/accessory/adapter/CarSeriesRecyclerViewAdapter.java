package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.Series;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Function:车系筛选fragment
 *
 * @author 袁秉勇
 * @since 2015年12月05日
 */
public class CarSeriesRecyclerViewAdapter extends RecyclerView.Adapter {
    private final static String TAG = CarSeriesRecyclerViewAdapter.class.getSimpleName();
    private final static int IS_HEADER = 0;
    private final static int IS_NORMAL = 1;
    ArrayList<Series > series = new ArrayList<>();
    Context mContext;
    carSeriesRecyclerViewCallBack carSeriesRecyclerViewCallBack;

    private boolean showNoLimit = true;//默认显示“不限”(筛选条件)  ture为显示
    private String brandName;

    /** 用于新车商城不显示“不限”时，显示汽车品牌名 */
    public void setShowNoLimit(boolean showNoLimit, String brandName) {
        this.showNoLimit = showNoLimit;
        this.brandName = brandName;
    }

    /** 用于新车商城不显示“不限”时，根据用户选择的品牌动态的修改title_tip */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_header_no_limit, null);
        onBindViewHolder(new ViewHolder(view, IS_HEADER), 0);
    }

    public void setCarSeriesRecyclerViewCallBack(CarSeriesRecyclerViewAdapter.carSeriesRecyclerViewCallBack carSeriesRecyclerViewCallBack) {
        this.carSeriesRecyclerViewCallBack = carSeriesRecyclerViewCallBack;
    }

    public CarSeriesRecyclerViewAdapter(ArrayList< Series > series, Context context) {
        this.series = series;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        DebugLog.e(TAG, "---------- getItemViewType");
        if (position == 0) {
            return IS_HEADER;
        } else {
            return IS_NORMAL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DebugLog.e(TAG, " ----------- onCreateViewHolder");
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_header_no_limit, null);
            return new ViewHolder(view, IS_HEADER);
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.car_series_recyclerview_item, parent, false);
            return new ViewHolder(view, IS_NORMAL);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebugLog.e(TAG, " ----------- onBindViewHolder" + "    " + position);
        if (position == 0 && ((ViewHolder) holder).viewType == IS_HEADER) {
            if (showNoLimit) {
                ((ViewHolder) holder).mNoLimitView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        carSeriesRecyclerViewCallBack.carSeriesCallBack(null);
                    }
                });
            } else {
                ((ViewHolder) holder).mNoLimitView.setOnClickListener(null);
                ((ViewHolder) holder).mNoLimitView.setText(brandName);
                ((ViewHolder) holder).mNoLimitView.setTextAppearance(mContext, R.style.BlackText14);
            }
        } else if (position > 0 && ((ViewHolder) holder).viewType == IS_NORMAL) {
            if (position - 1 >= 0 && position - 1 < series.size()) {
                final Series series = this.series.get(position - 1);
                ((ViewHolder) holder).mCarSeriesNameView.setText(VerifyString(series.seriesName));
//                ((ViewHolder) holder).mCarSeriesPriceView.setText(OtherUtil.formatPriceRange(series.priceRange));
//                if (!TextUtils.isEmpty(series.seriesThumb)) {
//                    Picasso.with(mContext).load(series.seriesThumb).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(((ViewHolder) holder).mCarSeriesImageView);
//                } else {
//                    ((ViewHolder) holder).mCarSeriesImageView.setImageResource(R.drawable.pic_normal);
//                }

                ((ViewHolder) holder).mContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        carSeriesRecyclerViewCallBack.carSeriesCallBack(series);
                    }
                });
            }
        }
    }

    public String VerifyString(String str) {
        if (!TextUtils.isEmpty(str)) {
//            if (str.length() > 1 && "系".equals(str.substring(str.length() - 1, str.length()))) {
//                return str + "列车型";
//            } else
            if (str.length() > 2 && "系列".equals(str.substring(str.length() - 2, str.length()))) {
                return str + "车型";
            }
            return str + "系列车型";
        }
        return "未知车系车型";
    }

    @Override
    public int getItemCount() {
        DebugLog.e(TAG, " -------- getItemCount");
        return series.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCarSeriesNameView;
//        TextView mCarSeriesPriceView;
//        ImageView mCarSeriesImageView;
        LinearLayout mContentView;
        public int viewType;
        TextView mNoLimitView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            DebugLog.e(TAG, " ------------ ViewHolder" + "    " + viewType);
            this.viewType = viewType;
            if (viewType == IS_HEADER) {
                mNoLimitView = (TextView) itemView.findViewById(R.id.no_limit_condition);
            } else if (viewType == IS_NORMAL) {
                mContentView = (LinearLayout) itemView.findViewById(R.id.content);
                mCarSeriesNameView = (TextView) itemView.findViewById(R.id.car_series_name);
//                mCarSeriesPriceView = (TextView) itemView.findViewById(R.id.car_series_price);
//                mCarSeriesImageView = (ImageView) itemView.findViewById(R.id.car_series_image);
            }
        }
    }

    public interface carSeriesRecyclerViewCallBack {
        void carSeriesCallBack(Series series);
    }
}
