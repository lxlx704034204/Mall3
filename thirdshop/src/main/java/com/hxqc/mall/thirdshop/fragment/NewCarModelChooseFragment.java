package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.model.newcar.ShopModelPrice;
import com.hxqc.mall.thirdshop.views.adpter.CarModelChooseAdapter;
import com.hxqc.util.JSONUtils;

/**
 * 询价 车型选择
 * Created by zhaofan
 */
public class NewCarModelChooseFragment extends Fragment {
    public static final String SERIES = "series";
    public static final String EXTID = "extID";
    public static final String AreaID = "AreaID";
    public static final String ModelName = "ModelName";
    public static final String BRAND = "BRAND";
    final ThirdPartShopClient client = new ThirdPartShopClient();
    private String areaID;
    private String series;
    private String carTypeName;
    private String brand;
    private String extID;
    private String shopSiteFrom;
    private ListView lv;
    private CarModelChooseAdapter mAdapter;


    public static NewCarModelChooseFragment newInstance(String siteId, String shopSiteFrom, String extID, String brand, String seriesName, String carTypeName) {
        Bundle args = new Bundle();
        args.putString(SERIES, seriesName);
        args.putString(EXTID, extID);
        args.putString(AreaID, siteId);
        args.putString(ModelName, carTypeName);
        args.putString(BRAND, brand);
        args.putString("shopSiteFrom", shopSiteFrom);
        NewCarModelChooseFragment fragment = new NewCarModelChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_carmodel_choose, container, false);
        lv = (ListView) v.findViewById(R.id.lv);
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        series = getArguments().getString(SERIES);
        areaID = getArguments().getString(AreaID);
        extID = getArguments().getString(EXTID);
        shopSiteFrom = getArguments().getString("shopSiteFrom");
        carTypeName = getArguments().getString(ModelName);
        brand = getArguments().getString(BRAND);
        mAdapter = new CarModelChooseAdapter(getActivity());
        mAdapter.setChoose(carTypeName);
        lv.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        //model 参数填任意值 ，返回车型列表
        client.ShopModelPrice(areaID, shopSiteFrom, extID, brand, series, "1", new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                ShopModelPrice mShopModelPrice
                        = JSONUtils.fromJson(response, ShopModelPrice.class);
                if (mShopModelPrice == null || mShopModelPrice.getModelList() == null) {
                    return;
                }
                mAdapter.setData(mShopModelPrice.getModelList());
                int i = 0;
                for (ModelInfo entity : mShopModelPrice.getModelList()) {
                    if (entity.getModelName().endsWith(carTypeName)) {
                        lv.setSelection(i);
                    }
                    i++;
                }

            }
        });
    }
}

