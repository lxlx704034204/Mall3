package com.hxqc.mall.auto.activity.automodel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 08 - 12
 * FIXME
 * Todo 选择页面父类
 */
public class BaseAutoTypeFragment extends FunctionFragment implements ExpandableListView.OnChildClickListener {
    protected PinnedHeaderExpandableListView mExpandableListView;
    protected View rootView;
    protected ExpandableListView.OnChildClickListener mChildClickListener;
    protected RequestFailView mRequestFailView;
    protected SideBar sideBarView;


    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_choose_brand, container, false);
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
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.brand_list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.brang_view);
        sideBarView = (SideBar) view.findViewById(R.id.SideBar);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_brand, container, false);
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
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.brand_list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.brand_view);
        sideBarView = (SideBar) view.findViewById(R.id.brand_sidebar);
    }

    @Override
    public String fragmentDescription() {
        return null;
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

    /**
     * 创建边侧导航
     */
    protected void initSideTag(List<BrandGroup> brandGroups) {//
        String[] tag = new String[brandGroups.size()];
        for (int i = 0; i < brandGroups.size(); i++) {
            tag[i] = brandGroups.get(i).groupTag;
        }
        sideBarView.setSideTag(tag);
    }

    protected Brand brand;

    /**
     * 计算位置
     *
     * @param brandGroups
     */
    protected void aa(List<BrandGroup> brandGroups) {
        int groupCount = 0;
        int chileCount;
        for (BrandGroup brandGroup : brandGroups) {
            ++groupCount;
            chileCount = 0;
            for (Brand brand1 : brandGroup.group) {
                ++chileCount;
                if (brand.equals(brand1)) {
                    int groupNum = groupCount - 2;
                    if (groupNum < 0) {
                        groupNum = 1;
                    }
                    mExpandableListView.setSelectedChild(groupNum, chileCount, true);
                    return;
                }
            }
        }
    }
}
