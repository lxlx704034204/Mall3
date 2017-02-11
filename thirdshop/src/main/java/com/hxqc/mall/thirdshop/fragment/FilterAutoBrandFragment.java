package com.hxqc.mall.thirdshop.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.BrandGroup;
import com.hxqc.mall.thirdshop.views.adpter.BrandExpandableAdapter;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-05
 * FIXME
 * Todo 筛选车辆品牌菜单
 */
public class FilterAutoBrandFragment extends FunctionFragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {
    private RequestFailView autoBrandView;
    private PinnedHeaderExpandableListView autoBrandList;

    private BrandExpandableAdapter adapter;

    FilterAction mFilterAction;

    ThirdPartShopClient client;
    private String areaID;
    private View mHeaderView;

    public void setmFilterGroup(FilterGroup mFilterGroup) {
        this.mFilterGroup = mFilterGroup;
    }

    FilterGroup mFilterGroup;

    private ArrayList<BrandGroup> mBrandGroups = new ArrayList<>();

    public static FilterAutoBrandFragment newInstance(String areaID) {
        Bundle args = new Bundle();
        FilterAutoBrandFragment fragment = new FilterAutoBrandFragment();
        fragment.setArguments(args);
        fragment.areaID = areaID;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFilterAction = (FilterAction) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_brand_2, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        autoBrandView = (RequestFailView) view.findViewById(R.id.auto_brand_view_2);
        autoBrandList = (PinnedHeaderExpandableListView) view.findViewById(R.id.auto_brand_list_2);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_factor_head, null);
        mHeaderView.setOnClickListener(this);
        autoBrandList.addHeaderView(mHeaderView);

        client = new ThirdPartShopClient();
        //加载数据
        client.filterAutoBrand(areaID, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {

                ArrayList<BrandGroup> tBrandsGroups = JSONUtils.fromJson(response,
                        new TypeToken<ArrayList<BrandGroup>>() {
                        });
                mBrandGroups.clear();
                mBrandGroups.addAll(tBrandsGroups);

                adapter = new BrandExpandableAdapter(getActivity(), tBrandsGroups);
                autoBrandList.setAdapter(adapter);
                autoBrandList.setOnHeaderUpdateListener(adapter);
                OtherUtil.openAllChild(adapter, autoBrandList);
            }
        });
        autoBrandList.setOnChildClickListener(this);
        //****************测试数据**********************/
        ArrayList<BrandGroup> brandGroups = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BrandGroup brandGroup = new BrandGroup();
            brandGroup.groupTag = i + "";
            ArrayList<Brand> brands = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Brand brand = new Brand("品牌" + i + j);
                brand.brandThumb = "";
//                brand.filterValue = "filterValue" + i + j;
//                brand.filterKey = "filterKey" + i + j;
                brand.brandInitial = i + "";
                brands.add(brand);
            }
            brandGroup.group = brands;
            brandGroups.add(brandGroup);
        }
        mBrandGroups.clear();
        mBrandGroups.addAll(brandGroups);

        adapter = new BrandExpandableAdapter(getActivity(), brandGroups);
        autoBrandList.setAdapter(adapter);
        autoBrandList.setOnHeaderUpdateListener(adapter);
        OtherUtil.openAllChild(adapter, autoBrandList);

    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Brand brand = mBrandGroups.get(groupPosition).group.get(childPosition);
        brand.filterKey = FilterResultKey.BRAND_KEY;
        brand.filterValue = brand.brandName;
        mFilterAction.filterListener(1, brand, mFilterGroup);
        return false;
    }

    @Override
    public void onClick(View v) {
//        Brand brand = mBrandGroups.get(0).group.get(0);
        Brand brand = new Brand("");
        brand.filterKey = FilterResultKey.BRAND_KEY;
        mFilterAction.filterListener(0, brand, mFilterGroup);
    }

    @Override
    public String fragmentDescription() {
        return "品牌筛选";
    }
}
