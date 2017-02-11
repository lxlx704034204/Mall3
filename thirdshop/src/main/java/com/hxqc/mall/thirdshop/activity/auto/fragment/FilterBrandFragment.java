package com.hxqc.mall.thirdshop.activity.auto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.BrandGroup;
import com.hxqc.mall.thirdshop.views.adpter.BrandExpandableAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.PinnedHeaderExpandableListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:袁秉勇
 * Date: 2015/12/1
 */
public class FilterBrandFragment extends FunctionFragment implements BaseFilterController.TBrandHandler, ExpandableListView.OnChildClickListener {
    private final static String TAG = FilterBrandFragment.class.getSimpleName();
    protected PinnedHeaderExpandableListView mExpandableListView;
    protected BrandExpandableAdapter mAutoBrandAdapter;
    protected FilterBrandFragmentCallBack filterBrandFragmentCallBack;
    protected View rootView;
    protected RequestFailView mRequestFailView;
    private ArrayList< BrandGroup > brandGroups = new ArrayList<>();
    private boolean showNoLimit = true; // 默认显示“不限”(筛选条件)  true为显示
    protected SideBar sideBarView;
    private boolean showSideBar = false;

    private BaseFilterController baseFilterController;

    /**
     * 是否显示  "不限"
     * true 显示
     * false 不显示
     */
    public void setShowNoLimit(boolean showNoLimit) {
        this.showNoLimit = showNoLimit;
    }

    /** 是否显示侧边快捷提示栏
     *  true 显示
     *  false 不显示
     */
    public void setShowSideBar(boolean showSideBar) {
        this.showSideBar = showSideBar;
    }

    public void setRecyclerViewHeaderImageView() {
        ImageView heardView = new ImageView(mContext);
        heardView.setBackgroundResource(R.drawable.pic_list_title);
        mExpandableListView.addHeaderView(heardView);
    }

    public void setFilterBrandFragmentCallBack(FilterBrandFragmentCallBack filterBrandFragmentCallBack) {
        this.filterBrandFragmentCallBack = filterBrandFragmentCallBack;
    }

    @Override
    public void onAttach(Context context) {
        DebugLog.e(TAG, " ----------- onAttach ");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ------------ onCreate ");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ---------- onCreateView ");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.t_fragment_filter_brand, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ------------ onViewCreated ");
        super.onViewCreated(view, savedInstanceState);
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        sideBarView = (SideBar) view.findViewById(R.id.SideBar);
        sideBarView.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                if (index == 0) {
                    mExpandableListView.smoothScrollToPosition(0);
                } else {
                    mExpandableListView.setSelectedGroup(index - 1);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ---------- onActivityCreated " + System.currentTimeMillis());
        super.onActivityCreated(savedInstanceState);
//        DebugLog.e(TAG, " ApiClient request time " + System.currentTimeMillis());
        if (baseFilterController == null) {
            if (getActivity() instanceof ControllerConstruct) {
                baseFilterController = ((ControllerConstruct)getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }
        getData();
    }


    @Override
    public void onStart() {
//        DebugLog.e(TAG, " --------- onStart ");
        super.onStart();
    }

    public void getData() {
        if (baseFilterController == null) {
            if (getActivity() instanceof ControllerConstruct) {
                baseFilterController = ((ControllerConstruct)getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }
        baseFilterController.requestThirdShopBrand(getActivity(), this);//获取车辆品牌列表
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (filterBrandFragmentCallBack != null) filterBrandFragmentCallBack.onFilterBrandCallback(brandGroups.get(groupPosition).group.get(childPosition));
        return false;
    }

    @Override
    public void onSucceed(ArrayList< BrandGroup > brandGroups) {
        if (brandGroups == null || brandGroups.size() <= 0) {
            onFailed(false);
            return;
        } else {
            this.brandGroups = brandGroups;
            mRequestFailView.setVisibility(View.GONE);
            if (showSideBar && sideBarView.getVisibility() == View.GONE) sideBarView.setVisibility(View.VISIBLE);
        }

        initSideTag(brandGroups);

        if (mAutoBrandAdapter == null) {
            mAutoBrandAdapter = new BrandExpandableAdapter(mContext, brandGroups);
            if (showNoLimit) {
                mExpandableListView.addHeaderView(createHeaderView(mContext));
            } else {
                setRecyclerViewHeaderImageView();
            }
            mExpandableListView.setAdapter(mAutoBrandAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAutoBrandAdapter);
            OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
        } else {
            mAutoBrandAdapter = new BrandExpandableAdapter(mContext, brandGroups);
            mExpandableListView.setAdapter(mAutoBrandAdapter);
            OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
            mExpandableListView.setSelection(0);
        }
    }

    @Override
    public void onFailed(boolean offLine) {
        if (sideBarView.getVisibility() == View.GONE) sideBarView.setVisibility(View.GONE);
        if (offLine) {
            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
        } else {
            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty));
        }
    }


    @Override
    public String fragmentDescription() {
        return "4s店品牌";
    }

    private TextView createHeaderView(Context context) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        textView.setPadding(padding, padding / 2, padding, padding / 2);
        textView.setText("不限");
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        textView.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterBrandFragmentCallBack != null) filterBrandFragmentCallBack.onFilterBrandCallback(null);
            }
        });
        return textView;
    }

//    @Override
//    public void onClick(View v) {
//        Brand brand = brandGroups.get(0).group.get(0);
//        if (filterBrandFragmentCallBack != null) filterBrandFragmentCallBack.onFilterBrandCallback(brand);
//    }

    public interface FilterBrandFragmentCallBack {
        void onFilterBrandCallback(Brand brand);
    }

    /**
     * 创建边侧导航
     */
    private void initSideTag(List< BrandGroup > brandGroups) {
        String[] tag = new String[brandGroups.size()];
        for (int i = 0; i < brandGroups.size(); i++) {
            tag[i] = brandGroups.get(i).groupTag;
        }
        sideBarView.setSideTag(tag);
    }
}
