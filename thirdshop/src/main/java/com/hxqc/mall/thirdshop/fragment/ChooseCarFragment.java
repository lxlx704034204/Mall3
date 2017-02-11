package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.SelectCarByConditionActivity;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterSeriesFragment1;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.BrandGroup;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.adpter.BrandExpandableAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: 4S店选车Fragment界面
 *
 * @author 袁秉勇
 * @since 2016年11月16日
 */
public class ChooseCarFragment extends FunctionFragment implements BaseFilterController.TBrandHandler, ExpandableListView.OnChildClickListener, FilterSeriesFragment1.FilterSeriesFragmentCallBack {
    private final static String TAG = ChooseCarFragment.class.getSimpleName();

    //    protected Toolbar toolbar;
    protected PinnedHeaderExpandableListView mExpandableListView;
    protected BrandExpandableAdapter mAutoBrandAdapter;
    protected View rootView;
    protected RequestFailView mRequestFailView;
    private ArrayList< BrandGroup > brandGroups = new ArrayList<>();
    protected SideBar sideBarView;

    private BaseFilterController baseFilterController;

    private OverlayDrawer mOverlayDrawer;
    private FilterSeriesFragment1 filterSeriesFragment;
    private Brand brand;
    private AreaSiteUtil areaSiteUtil;


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
            rootView = inflater.inflate(R.layout.fragment_choose_car, container, false);
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

//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//        toolbar.setTitle("选车");

        mOverlayDrawer = (OverlayDrawer) view.findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);

        filterSeriesFragment = (FilterSeriesFragment1) getChildFragmentManager().findFragmentById(R.id.series);
        filterSeriesFragment.setShowNoLimit(false);
        filterSeriesFragment.setFilterSeriesFragmentCallBack(this);
        filterSeriesFragment.setFromNewCar(true);

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
                baseFilterController = ((ControllerConstruct) getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }

        areaSiteUtil = AreaSiteUtil.getInstance(mContext);
        baseFilterController.mFilterMap.put("siteID", areaSiteUtil.getSiteID());
        getData();
    }


    public void getData() {
        baseFilterController.requestThirdShopBrand(getActivity(), this);//获取车辆品牌列表
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        brand = brandGroups.get(groupPosition).group.get(childPosition);
        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
        if (brand != null) filterSeriesFragment.getData(brand.brandName, true);
        return true;
    }


    @Override
    public void onSucceed(ArrayList< BrandGroup > brandGroups) {
        if (brandGroups == null || brandGroups.size() <= 0) {
            onFailed(false);
            return;
        } else {
            this.brandGroups = brandGroups;
            mRequestFailView.setVisibility(View.GONE);
        }

        initSideTag(brandGroups);

        if (mAutoBrandAdapter == null) {
            mAutoBrandAdapter = new BrandExpandableAdapter(mContext, brandGroups);
            mExpandableListView.addHeaderView(initChooseCarHeaderView());
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


    @Override
    public void onFilterSeriesCallBack(Series series) {
        ActivitySwitcherThirdPartShop.toNewCarModelList2(mContext, areaSiteUtil.getSiteID(), brand.brandName, series.seriesName);
    }


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


    private View mChooseCarView;

    private View mCarNumView;


    public View initChooseCarHeaderView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_choose_car_header, null);
        mChooseCarView = view.findViewById(R.id.choose_car);
        mCarNumView = view.findViewById(R.id.car_num);

        mChooseCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectCarByConditionActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (areaSiteUtil != null) {
            areaSiteUtil.destroy();
            areaSiteUtil = null;
        }
    }
}
