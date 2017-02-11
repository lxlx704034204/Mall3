package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.fragment.me.WishListForRecyclerFragment;

import hxqc.mall.R;

/**
 * 说明:我的关注
 *
 * author: 吕飞
 * since: 2015-03-16
 * Copyright:恒信汽车电子商务有限公司
 */
public class WishListActivity extends AppBackActivity {
//    ArrayList<CollectInfo> mCollectInfos;//关注列表
    public FragmentManager mFragmentManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_has_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fl_content, new WishListForRecyclerFragment());
        mFragmentTransaction.commit();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        initData();
//    }
//
//    private void initData() {
//        mApiClient.wishList(mSharedPreferencesHelper.getToken(), new LoadingAnimResponseHandler(this) {
//            @Override
//            public void onSuccess(String response) {
//                mCollectInfos = JSONUtils.fromJson(response, new TypeToken<ArrayList<CollectInfo>>() {
//                });
//                if (mCollectInfos != null && mCollectInfos.size() > 0) {
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    showList();
//                } else {
//                    mRecyclerView.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                showBlankPage(R.string.app_net_error);
//            }
//        });
//    }
//
//    private void showList() {
//        mAdapter = new WishListAdapter(mCollectInfos, this);
//        mRecyclerView.addItemDecoration(new DividerForRecyclerView(this));
//        mRecyclerView.setAdapter(mAdapter);
//    }
}
