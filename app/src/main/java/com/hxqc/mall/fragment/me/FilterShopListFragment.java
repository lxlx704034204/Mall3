package com.hxqc.mall.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxqc.mall.activity.me.ComplaintsActivity;
import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.control.TFilterController_2;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-24
 * FIXME
 * Todo 选择投诉的店铺
 */

public class FilterShopListFragment extends FunctionFragment {
    private ListView list;
    private RequestFailView requestFailView;
    private List<ShopSearchShop> mDatas = new ArrayList<>();

    public static final String BRAND = "brand";
    public static final String SITEID = "siteID";
    private String brand;
    private String siteID;

    public static FilterShopListFragment newInstance(String brand, String siteID) {
        Bundle args = new Bundle();
        args.putString(BRAND, brand);
        args.putString(SITEID, siteID);
        FilterShopListFragment fragment = new FilterShopListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setBrand(String brand) {
        this.brand = brand;
//        loadData();
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
//        loadData();
    }

    public void reloadData() {
        mDatas.clear();
        ((CommonAdapter) list.getAdapter()).notifyDataSetChanged();
        loadData();
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadData();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && (TextUtils.isEmpty(brand) && TextUtils.isEmpty(siteID))) {
            brand = bundle.getString(BRAND);
            siteID = bundle.getString(SITEID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feedback_options, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadData();
    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put(BRAND, brand);
        params.put(SITEID, siteID);
        TFilterController_2.getInstance().getShopData(getContext(), 1, 9999, params,
                new TFilterController_2.TShopHandler() {
                    @Override
                    public void onGetShopSucceed(ArrayList<ShopSearchShop> shopSearchShops) {
                        mDatas.clear();
                        ShopSearchShop areaCategory = new ShopSearchShop();
                        areaCategory.shopTitle = "不限";
                        areaCategory.promotionList = new ArrayList<>();
                        areaCategory.shopID = "";
                        mDatas.add(areaCategory);//添加不限
                        mDatas.addAll(shopSearchShops);
                        ((CommonAdapter) list.getAdapter()).notifyDataSetChanged();
                        requestFailView.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onGetShopFailed() {
                        requestFailView.setVisibility(View.VISIBLE);
                        list.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public String fragmentDescription() {
        return "feedbackOption";
    }

    private void initView(View view) {
        list = (ListView) view.findViewById(R.id.list);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选
        list.setAdapter(new CommonAdapter<ShopSearchShop>(getContext(), mDatas, R.layout.item_feedback_options_2) {
            @Override
            public void convert(ViewHolder helper, ShopSearchShop item, int position) {
                helper.setText(R.id.feedback_option_name, item.shopTitle);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity() instanceof ComplaintsActivity)
                    ((ComplaintsActivity) getActivity()).onShopSelected(mDatas.get(position));
                list.setSelection(position);
            }
        });
    }
}
