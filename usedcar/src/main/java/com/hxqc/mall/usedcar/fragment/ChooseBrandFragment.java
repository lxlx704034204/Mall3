package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.ChooseBrandAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.BrandGroup;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.mall.usedcar.views.SideBar;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;


/**
 * 说明:品牌选择
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseBrandFragment extends FunctionFragment implements ExpandableListView.OnChildClickListener, SideBar.OnTouchingLetterChangedListener {
    protected PinnedHeaderExpandableListView mBrandListView;
    ChooseBrandListener mListener;
    ArrayList<BrandGroup> brandGroups;
    ChooseBrandAdapter mChooseBrandAdapter;
    SideBar mSideBar;//侧边栏
    RequestFailView mRequestFailView;
    int mType;
    UsedCarSPHelper mUsedCarSPHelper;
    UsedCarApiClient mUsedCarApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_brand, container, false);
        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.fail_view);
        mBrandListView = (PinnedHeaderExpandableListView) rootView.findViewById(R.id.expandable_list);
        mBrandListView.setOnChildClickListener(this);
        mSideBar = (SideBar) rootView.findViewById(R.id.side_bar);
        mSideBar.mb1 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        mSideBar.setOnTouchingLetterChangedListener(this);
        mBrandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        }, false);
        return rootView;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUsedCarSPHelper = new UsedCarSPHelper(getActivity());
        mUsedCarApiClient = new UsedCarApiClient();
        getData(true);
    }

    private void getData(boolean hasLoadingAnim) {
        if (mType == ChooseFragment.VALUATION) {
            brandGroups = mUsedCarSPHelper.getValuationBrandList();
            if (brandGroups != null && brandGroups.size() > 0) {
                showList();
            } else {
                mUsedCarApiClient.getValuationBrands(new LoadingAnimResponseHandler(getActivity(), hasLoadingAnim) {
                    @Override
                    public void onSuccess(String response) {
                        brandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                        });
                        if (brandGroups != null && brandGroups.size() > 0) {
                            showList();
                            mUsedCarSPHelper.saveValuationBrandList(brandGroups);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        showFail();
                    }
                });
            }
        } else if (mType == ChooseFragment.INTENT) {
            brandGroups = mUsedCarSPHelper.getIntentBrandList();
            if (brandGroups != null && brandGroups.size() > 0) {
                showList();
            } else {
                mUsedCarApiClient.getIntentBrands(new LoadingAnimResponseHandler(getActivity(), hasLoadingAnim) {
                    @Override
                    public void onSuccess(String response) {
                        brandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                        });
                        if (brandGroups != null && brandGroups.size() > 0) {
                            showList();
                            mUsedCarSPHelper.saveIntentBrandList(brandGroups);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        showFail();
                    }
                });
            }
        } else {
            brandGroups = mUsedCarSPHelper.getBrandList();
            if (brandGroups != null && brandGroups.size() > 0) {
                showList();
            } else {
                mUsedCarApiClient.getBrands(new LoadingAnimResponseHandler(getActivity(), hasLoadingAnim) {
                    @Override
                    public void onSuccess(String response) {
                        brandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                        });
                        if (brandGroups != null && brandGroups.size() > 0) {
                            showList();
                            mUsedCarSPHelper.saveBrandList(brandGroups);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        showFail();
                    }
                });
            }
        }
    }

    private void showFail() {
        mBrandListView.setVisibility(View.GONE);
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        mRequestFailView.setFailButtonClick(getResources().getString(R.string.reload), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(true);
            }
        });
    }

    private void showList() {
        mBrandListView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        mChooseBrandAdapter = new ChooseBrandAdapter(getActivity(), brandGroups, mType);
        mBrandListView.setAdapter(mChooseBrandAdapter);
        mBrandListView.setOnHeaderUpdateListener(mChooseBrandAdapter);
        OtherUtil.openAllChild(mChooseBrandAdapter, mBrandListView);
    }

    @Override
    public String fragmentDescription() {
        return "二手车品牌选择";
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mListener.completeBrandChoose(brandGroups.get(groupPosition).group.get(childPosition).brand_name, brandGroups.get(groupPosition).group.get(childPosition).id, brandGroups.get(groupPosition).group.get(childPosition).brand_logurl);
        return false;
    }

    @Override
    public void onTouchingLetterChanged(String s, StringBuffer s1) {
        int position = 0;
        for (int i = 0; i < brandGroups.size(); i++) {
            if (brandGroups.get(i).groupTag.charAt(0) == (s.charAt(0))) {
                mBrandListView.setSelection(position);
                break;
            } else {
                position = position + brandGroups.get(i).group.size() + 1;
            }
        }
    }

    public void setListener(ChooseBrandListener mListener) {
        this.mListener = mListener;
    }

    //回调
    public interface ChooseBrandListener {
        void completeBrandChoose(String brand, String brandId, String brandIcon);
    }
}
