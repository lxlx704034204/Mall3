package com.hxqc.mall.auto.activity.automodel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.hxqc.mall.auto.activity.automodel.adapter.ThirdShopBrandExpandableAdapter;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * Des: 选择品牌
 * FIXME
 * Todo
 */
public class ThirdShopBrandFragment extends BaseAutoTypeFragment implements ExpandableListView.OnChildClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private ThirdShopBrandExpandableAdapter mAutoBrandAdapter;
    private ArrayList<BrandGroup> mBrandGroups;
    private String shopID;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView heardView = new ImageView(mContext);
        heardView.setBackgroundResource(R.drawable.pic_list_title);
        mExpandableListView.addHeaderView(heardView);

        sideBarView.setOnTouchingLetterChangedListener(
                new SideBar.OnTouchingLetterChangedListener() {
                    @Override
                    public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                        if (index == 0) {
                            mExpandableListView.smoothScrollToPosition(0);
                        } else {
                            mExpandableListView.setSelectedGroup(index - 1);
                        }

                    }

                });

//        mBrandDataProvider = AutoBrandDataControl.getInstance();

        if (getActivity().getIntent() != null) {
            Bundle bundle = getActivity().getIntent().getBundleExtra(ActivitySwitchAutoInfo.KEY_DATA);
            if (bundle != null) {
                shopID = bundle.getString("shopID", "");
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mBrandDataProvider.getBrandGroups(getActivity(), itemCategory, this);
        AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, brandGroupCallBack);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (mChildClickListener != null) {
            DebugLog.i(TAG, "onChildClick");
            mChildClickListener.onChildClick(parent, v, groupPosition, childPosition, id);
        }
        return false;
    }

    @Override
    public String fragmentDescription() {
        return "选择品牌";
    }

    /*@Override
    public void onBrandSucceed(ArrayList<BrandGroup> myAutoGroups) {
        if (myAutoGroups == null) return;
        if (mAutoBrandAdapter == null) {
            mBrandGroups = (ArrayList<BrandGroup>) myAutoGroups.clone();
            mAutoBrandAdapter = new ThirdShopBrandExpandableAdapter(mContext, myAutoGroups);
            mExpandableListView.setAdapter(mAutoBrandAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAutoBrandAdapter);
            OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
        } else {
            mAutoBrandAdapter.notifyData(myAutoGroups);
        }
        initSideTag(myAutoGroups);
        if (brand != null) {
            aa(myAutoGroups);
        }
    }

    @Override
    public void onBrandFailed(boolean offLine) {
        mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }*/

    private CallBackControl.CallBack<ArrayList<BrandGroup>> brandGroupCallBack = new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
        @Override
        public void onSuccess(ArrayList<BrandGroup> response) {
            if (response == null) return;
            mBrandGroups = response;
            if (mAutoBrandAdapter == null) {
                mAutoBrandAdapter = new ThirdShopBrandExpandableAdapter(mContext, response);
                mExpandableListView.setAdapter(mAutoBrandAdapter);
                mExpandableListView.setOnHeaderUpdateListener(mAutoBrandAdapter);
                OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
            } else {
                mAutoBrandAdapter.notifyData(response);
            }
            initSideTag(response);
            if (brand != null) {
                aa(response);
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
        }
    };

    public ArrayList<BrandGroup> getBrandGroups() {
        return mBrandGroups;
    }

}
