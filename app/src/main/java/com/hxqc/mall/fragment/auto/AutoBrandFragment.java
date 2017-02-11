package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxqc.mall.control.AutoBrandDataControl;
import com.hxqc.mall.core.adapter.BrandExpandableAdapter;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.BrandGroup;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

public class AutoBrandFragment extends FunctionFragment implements AutoBrandDataControl.BrandHandler,
        ExpandableListView.OnChildClickListener {
    protected PinnedHeaderExpandableListView mExpandableListView;
    protected BrandExpandableAdapter mAutoBrandAdapter;
    protected View rootView;
    AutoBrandDataControl mBrandDataProvider;
    ExpandableListView.OnChildClickListener mChildClickListener;
    RequestFailView mRequestFailView;
    int itemCategory=10;//品牌类型

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemCategory = getArguments().getInt(AutoItem.ItemCategory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_auto_brand, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        mBrandDataProvider = AutoBrandDataControl.getInstance();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBrandDataProvider.getBrandGroups(getActivity(), itemCategory, this);
    }

    public void setChildClickListener(ExpandableListView.OnChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (mChildClickListener != null) {
            mChildClickListener.onChildClick(parent, v, groupPosition, childPosition, id);
        }
        return false;
    }

    @Override
    public void onSucceed(List< BrandGroup > brandGroups) {
        if (brandGroups == null) return;
        if (mAutoBrandAdapter == null) {
            mAutoBrandAdapter = new BrandExpandableAdapter(mContext, (ArrayList< BrandGroup >) brandGroups);
            mExpandableListView.setAdapter(mAutoBrandAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAutoBrandAdapter);
            OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
        }
    }

    @Override
    public void onFailed() {
        mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }

    @Override
    public String fragmentDescription() {
        return "品牌";
    }

}
