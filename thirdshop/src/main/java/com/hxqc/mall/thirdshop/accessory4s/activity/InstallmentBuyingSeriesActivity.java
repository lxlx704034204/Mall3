package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.activity.automodel.adapter.ThirdShopBrandExpandableAdapter;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.adapter.InstallmentBuyingSeriesAdapter;
import com.hxqc.mall.thirdshop.accessory4s.views.AutoTypePopupView;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 16
 * Des: 分期购车-车系
 * FIXME
 * Todo
 */

public class InstallmentBuyingSeriesActivity extends BackActivity {

    public Brand brand;
    private AutoTypePopupView mBrandView;
    private AutoTypePopupView mSeriesView;
//    private AutoTypePopupView mModelView;
    private boolean[] mSwitchPress = null;
    private RecyclerView mSeriesListView;
    private InstallmentBuyingSeriesAdapter mInstallmentBuyingSeriesAdapter;
    private PinnedHeaderExpandableListView mExpandableListView;
    private LinearLayout mGroupBarView;
    private View mBgView;
    public AutoTypePopupView.PopupWindowListener popupWindowDismissListener = new AutoTypePopupView.PopupWindowListener() {
        @Override
        public void onDismiss() {
            mBgView.setVisibility(View.GONE);
            mSwitchPress[0] = false;
            mSwitchPress[1] = false;
            mBrandView.switchBackground(mSwitchPress[0]);
            mBrandView.switchDrawableRight(mSwitchPress[0]);
            mSeriesView.switchBackground(mSwitchPress[1]);
            mSeriesView.switchDrawableRight(mSwitchPress[1]);
        }
    };
    private View.OnClickListener sericeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSwitchPress[1]) {
                mSwitchPress[1] = false;
            } else {
                mSwitchPress[1] = true;
                mSwitchPress[0] = false;
            }
            mSeriesView.switchBackground(mSwitchPress[1]);
            mSeriesView.switchDrawableRight(mSwitchPress[1]);
            mBrandView.switchBackground(mSwitchPress[0]);
            mBrandView.switchDrawableRight(mSwitchPress[0]);
//            mModelView.switchBackground(false);
//            mModelView.switchDrawableRight(false);
        }
    };
    private ThirdShopBrandExpandableAdapter mAutoBrandAdapter;
    private ArrayList<BrandGroup> mBrandGroups;
    private CallBackControl.CallBack<ArrayList<BrandGroup>> brandGroupCallBack = new CallBackControl.CallBack<ArrayList<BrandGroup>>() {

        @Override
        public void onSuccess(ArrayList<BrandGroup> response) {
            if (response == null) return;
            if (mAutoBrandAdapter == null) {
                mBrandGroups = (ArrayList<BrandGroup>) response.clone();
                mAutoBrandAdapter = new ThirdShopBrandExpandableAdapter(InstallmentBuyingSeriesActivity.this, response);
                mExpandableListView.setAdapter(mAutoBrandAdapter);
//                mExpandableListView.setOnHeaderUpdateListener(mAutoBrandAdapter);
                OtherUtil.openAllChild(mAutoBrandAdapter, mExpandableListView);
            } else {
                mAutoBrandAdapter.notifyData(response);
            }
            mBrandView.initSideTag(response);
            if (brand != null) {
                mBrandView.aa(response);
            }
            mBrandView.showList(InstallmentBuyingSeriesActivity.this,mGroupBarView);
            mBrandView.setOnPopupWindowDismissListener(popupWindowDismissListener);
            mBgView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailed(boolean offLine) {
//            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
        }
    };
    private View.OnClickListener brandClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSwitchPress[0]) {
                mSwitchPress[0] = false;
            } else {
                mSwitchPress[0] = true;
                mSwitchPress[1] = false;
                AutoTypeControl.getInstance().requestBrand(InstallmentBuyingSeriesActivity.this, "", brandGroupCallBack);
            }
            mBrandView.switchBackground(mSwitchPress[0]);
            mBrandView.switchDrawableRight(mSwitchPress[0]);
            mSeriesView.switchBackground(mSwitchPress[1]);
            mSeriesView.switchDrawableRight(mSwitchPress[1]);
//            mModelView.switchBackground(false);
//            mModelView.switchDrawableRight(false);

        }
    };

    /*private View.OnClickListener modelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSwitchPress[2]) {
                mSwitchPress[2] = false;
            } else {
                mSwitchPress[2] = true;
            }
            mModelView.switchBackground(mSwitchPress[2]);
            mModelView.switchDrawableRight(mSwitchPress[2]);
            mBrandView.switchBackground(false);
            mBrandView.switchDrawableRight(false);
            mSeriesView.switchBackground(false);
            mSeriesView.switchDrawableRight(false);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_installment_buying_series);

        initView();

        initData();

        initEvent();
    }

    private void initData() {
        mSwitchPress = new boolean[3];
        mSwitchPress[0] = false;
        mSwitchPress[1] = false;
        mSwitchPress[2] = false;

        initAdapter();
    }

    private void initAdapter() {
        mSeriesListView.setLayoutManager(new LinearLayoutManager(this));
        mSeriesListView.setHasFixedSize(true);
	    if (mInstallmentBuyingSeriesAdapter == null) {
		    mInstallmentBuyingSeriesAdapter = new InstallmentBuyingSeriesAdapter(this);
            mSeriesListView.setAdapter(mInstallmentBuyingSeriesAdapter);
        }
    }

    private void initEvent() {

        mBrandView.setOnClickListener(brandClickListener);
        mSeriesView.setOnClickListener(sericeClickListener);
//        mModelView.setOnClickListener(modelClickListener);
    }

    private void initView() {
        mGroupBarView = (LinearLayout) findViewById(R.id.instalment_buying_series_group_bar);
        mBrandView = (AutoTypePopupView) findViewById(R.id.instalment_buying_series_brand);
        mExpandableListView = mBrandView.getExpandableListView();
        mSeriesView = (AutoTypePopupView) findViewById(R.id.instalment_buying_series);
//        mModelView = (AutoTypePopupView) findViewById(R.id.instalment_buying_model);
        findViewById(R.id.instalment_buying_series_content);
        findViewById(R.id.instalment_buying_series_content_text);
        findViewById(R.id.instalment_buying_series_content_cancel);
        mSeriesListView = (RecyclerView) findViewById(R.id.instalment_buying_series_list);

        mBgView = findViewById(R.id.instalment_buying_series_bg);

    }

}
