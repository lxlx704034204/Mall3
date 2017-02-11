package com.hxqc.mall.usedcar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.SellerCarListAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.MyCollection;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 说明:买家信息
 *
 * @author: 吕飞
 * @since: 2015-10-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class SellerInfoActivity extends BackActivity implements AdapterView.OnItemClickListener {
    CircleImageView mSellerImageView;//卖家图像
    TextView mSellerNameView;//卖家名字
    TextView mCarCountView;//车辆数
    ListView mCarListView;//车辆列表
    String mMobile;//卖家手机号
    String mSellerPhoto;//卖家头像
    String mSellerName;//卖家名字
    ArrayList<MyCollection> mMyCollections;//车辆列表数组
    UsedCarApiClient mUsedCarApiClient;
    RequestFailView mRequestFailView;//失败界面
    LinearLayout mRootView;//显示的根界面
    UsedCarSPHelper mUsedCarSPHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsedCarApiClient = new UsedCarApiClient();
        mUsedCarSPHelper = new UsedCarSPHelper(this);
        mMobile = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(UsedCarActivitySwitcher.MOBILE);
        mSellerPhoto = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(UsedCarActivitySwitcher.SELLER_PHOTO);
        mSellerName = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(UsedCarActivitySwitcher.SELLER_NAME);
        getData();
        setContentView(R.layout.activity_seller_info);
        mRootView = (LinearLayout) findViewById(R.id.root);
        mRequestFailView = (RequestFailView) findViewById(R.id.fail_view);
        mSellerImageView = (CircleImageView) findViewById(R.id.seller_image);
        mSellerNameView = (TextView) findViewById(R.id.seller_name);
        mCarCountView = (TextView) findViewById(R.id.car_count);
        mCarListView = (ListView) findViewById(R.id.car_list);
        mCarListView.setOnItemClickListener(this);
        mRequestFailView.setFailButtonClick(getResources().getString(R.string.reload), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void getData() {
        mUsedCarApiClient.getSellerCarList(mMobile, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mMyCollections = JSONUtils.fromJson(response, new TypeToken<ArrayList<MyCollection>>() {
                });
                if (mMyCollections != null && mMyCollections.size() >= 0) {
                    showData();
                } else {
                    showNoData();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showNoData();
            }
        });
    }

    //显示失败界面
    private void showNoData() {
        mRootView.setVisibility(View.GONE);
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    //显示数据
    private void showData() {
        mRootView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        ImageUtil.setImage(this, mSellerImageView, mSellerPhoto, R.mipmap.ic_individual);
        mSellerNameView.setText(mSellerName);
        mCarCountView.setText(mMyCollections.size() + "");
        mCarListView.setAdapter(new SellerCarListAdapter(this, mMyCollections));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMyCollections.get(position).isPersonal()) {
            UsedCarActivitySwitcher.toPersonalCarDetail(this, mMyCollections.get(position).car_source_no);//进入个人详情
        } else {
            UsedCarActivitySwitcher.toPlatformCarDetail(this, mMyCollections.get(position).car_source_no);//进入自营详情
        }
    }
}
