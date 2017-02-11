package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.AutoBaseInfoThirdShop;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 主推车型
 */
public class MainAutoView extends LinearLayout implements CommonTitleView.OnClickListener {
    private CommonTitleView titleView;
    private ListViewNoSlide listViewNoSlide;

    public void addData(ArrayList<AutoBaseInfoThirdShop > mData) {
        if (mData == null || mData.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            this.mData.clear();
            this.mData.addAll(mData);
            adapter.notifyDataSetChanged();
        }
    }

    private ArrayList<AutoBaseInfoThirdShop> mData;
    private ListAdapter adapter;

    public MainAutoView(Context context) {
        this(context, null);
    }

    public MainAutoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainAutoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_main_auto, this);
        titleView = (CommonTitleView) findViewById(R.id.top_title);
        listViewNoSlide = (ListViewNoSlide) findViewById(R.id.main_auto_list);
        listViewNoSlide.setFocusable(false);
        titleView.setOnClickListener(this);
        mData = new ArrayList<>();
        adapter = new ListAdapter();
        listViewNoSlide.setAdapter(adapter);
    }

    public interface OnClickListener {
        //点击试驾
        void onClickTestDrive(View view, int position);

        //点击询价
        void onClickInquiry(View view, int position);

        //点击查看全部
        void onClickCheckAll(View view);

        //点击查看item
        void onClickItem(View view, int position);
    }

    private MainAutoView.OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.top_title) {//title的点击
            if (null != onClickListener)
                onClickListener.onClickCheckAll(MainAutoView.this);

        }
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mData != null)
                if (mData.size() > 3)
                    return 3;
                else
                    return mData.size();
            else
                return 0;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.t_item_main_auto_list, null);
            MainAutoItemView itemView = (MainAutoItemView) convertView.findViewById(R.id.main_auto_item);
            if (null != mData) {
                itemView.setAutoBaseInfoThirdShop(mData.get(position));
            }
            itemView.setOnClickListener(new MainAutoItemView.OnClickListener() {

                @Override
                public void onClickTestDrive() {
                    if (null != onClickListener)
                        onClickListener.onClickTestDrive(MainAutoView.this, position);
                }

                @Override
                public void onClickInquiry() {
                    if (null != onClickListener)
                        onClickListener.onClickInquiry(MainAutoView.this, position);
                }

                @Override
                public void onNormalClick() {
                    if (null != onClickListener)
                        onClickListener.onClickItem(MainAutoView.this, position);
                }
            });
            return convertView;
        }

    }
}
