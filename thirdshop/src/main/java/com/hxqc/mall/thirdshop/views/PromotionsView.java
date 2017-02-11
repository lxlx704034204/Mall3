package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 最新促销信息
 */
public class PromotionsView extends LinearLayout {
    private ListViewNoSlide listViewNoSlide;
    private ArrayList<SalesPModel > mData;
    private PromotionsView.ListAdapter adapter;

    /**
     * 填充数据
     *
     * @param data
     */
    public void addData(ArrayList<SalesPModel> data) {
        if (data == null || data.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            mData.clear();
            mData.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public PromotionsView(Context context) {
        this(context, null);
    }

    public PromotionsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PromotionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_promotions, this);
        listViewNoSlide = (ListViewNoSlide) findViewById(R.id.promotions_list);
        ((CommonTitleView) findViewById(R.id.top_title)).setOnClickListener
                (new CommonTitleView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != onClickListener)
                            onClickListener.onClickCheckAll(PromotionsView.this);
                    }
                });
        mData = new ArrayList<>();
        adapter = new ListAdapter();
        listViewNoSlide.setAdapter(adapter);
    }

    public interface OnClickListener {
        void onItemClick(View view, int position);

        void onClickCheckAll(View view);
    }

    private PromotionsView.OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListAdapter extends BaseAdapter {
        private static final int ITEM_TYPE_HEAD = 0;
        private static final int ITEM_TYPE_NORMAL = 1;

        @Override
        public int getCount() {
            if (mData == null)
                return 0;
            else if (mData.size() > 3)
                return 3;
            else
                return mData.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return ITEM_TYPE_HEAD;
            else
                return ITEM_TYPE_NORMAL;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            if (position == 0) {
            PromotionsNormalItem item = new PromotionsNormalItem(getContext());
            item.setPromotion(mData.get(position));
            convertView = item;

//            } else {
//                PromotionsItem item = new PromotionsItem(getContext());
//                item.setPromotion(mData.get(position));
//                convertView = item;
//            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener)
                        onClickListener.onItemClick(PromotionsView.this, position);
                }
            });
            return convertView;
        }
    }
}
