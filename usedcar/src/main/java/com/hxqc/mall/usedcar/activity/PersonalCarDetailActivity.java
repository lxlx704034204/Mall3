package com.hxqc.mall.usedcar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.MonitorScrollView;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.BuyCarListAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.CarDetail;
import com.hxqc.mall.usedcar.model.QADetail;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.CarDetailTopView;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人车辆详情页
 * Created by huangyi on 15/10/23.
 */
public class PersonalCarDetailActivity extends NoBackActivity implements MonitorScrollView.ScrollViewListener, View.OnClickListener {

    public static final String CAR_SOURCE_NO = "car_source_no";
    CarDetail mCarDetail; //数据源

    Toolbar mToolbar;
    MonitorScrollView mMonitorView;
    CarDetailTopView mTopView;
    LinearLayout mTempParent1View, mTempParent2View, mTempParent3View, mBottomView;
    TextView mFirstView, mAddressView, mAgeView, mMileageView, mTemp1View, mTemp2View, mTemp3View, mTemp4View, mTemp5View, mTemp6View,
            mSellerNameView, mSellerAddressView, mSellerDescriptionView, mNameView;
    CircleImageView mSellerPhotoView;
    ListViewNoSlide mListView;
    ImageView mPhoneView;
    RequestFailView mFailView;

    ArrayList<QADetail> temp; //借用QADetail

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_car_detail);
        initView();
        initData(true);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.personal_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitleTextColor(Color.parseColor("#00ffffff"));
        setSupportActionBar(mToolbar);

        mMonitorView = (MonitorScrollView) findViewById(R.id.personal_monitor);
        mMonitorView.setHeight((int) (DisplayTools.getScreenWidth(this) / 1.75 - DisplayTools.dip2px(this, 56)));
        mMonitorView.setScrollViewListener(mToolbar,this);
        mTopView = (CarDetailTopView) findViewById(R.id.personal_top);
        mTempParent1View = (LinearLayout) findViewById(R.id.personal_temp_parent1);
        mTempParent2View = (LinearLayout) findViewById(R.id.personal_temp_parent2);
        mTempParent3View = (LinearLayout) findViewById(R.id.personal_temp_parent3);
        mFirstView = (TextView) findViewById(R.id.personal_first);
        mAddressView = (TextView) findViewById(R.id.personal_address);
        mAgeView = (TextView) findViewById(R.id.personal_age);
        mMileageView = (TextView) findViewById(R.id.personal_mileage);
        mTemp1View = (TextView) findViewById(R.id.personal_temp1);
        mTemp2View = (TextView) findViewById(R.id.personal_temp2);
        mTemp3View = (TextView) findViewById(R.id.personal_temp3);
        mTemp4View = (TextView) findViewById(R.id.personal_temp4);
        mTemp5View = (TextView) findViewById(R.id.personal_temp5);
        mTemp6View = (TextView) findViewById(R.id.personal_temp6);
        mSellerNameView = (TextView) findViewById(R.id.personal_seller_name);
        mSellerAddressView = (TextView) findViewById(R.id.personal_seller_address);
        mSellerDescriptionView = (TextView) findViewById(R.id.personal_seller_description);
        mSellerPhotoView = (CircleImageView) findViewById(R.id.personal_seller_photo);
        findViewById(R.id.personal_config).setOnClickListener(this);
        mListView = (ListViewNoSlide) findViewById(R.id.personal_list);
        mBottomView = (LinearLayout) findViewById(R.id.personal_bottom);
        mBottomView.setOnClickListener(this);
        mPhoneView = (ImageView) findViewById(R.id.personal_phone);
        mNameView = (TextView) findViewById(R.id.personal_name);

        mFailView = (RequestFailView) findViewById(R.id.personal_fail);
        mFailView.setEmptyDescription("没有数据");
        mFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
            }
        });
        mFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false);
            }
        });

        mBottomView.setVisibility(View.GONE);
        mMonitorView.setVisibility(View.GONE);
    }

    private void initData(boolean showAnim) {
        new UsedCarApiClient().getProductDetail(UserInfoHelper.getInstance().getPhoneNumber(this), getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(CAR_SOURCE_NO), new LoadingAnimResponseHandler(this, showAnim) {
            @Override
            public void onSuccess(String response) {
                mCarDetail = JSONUtils.fromJson(response, CarDetail.class);
                if (mCarDetail == null) {
                    mBottomView.setVisibility(View.GONE);
                    mMonitorView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    return;
                } else {
                    mBottomView.setVisibility(View.VISIBLE);
                    mMonitorView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);
                }

                //非上架状态 不能联系卖家
                if (Integer.valueOf(mCarDetail.state) != 0) {
                    mBottomView.setEnabled(false);
                    mBottomView.setBackgroundColor(Color.parseColor("#999999"));
                    mPhoneView.setVisibility(View.GONE);
                    mNameView.setTextColor(ContextCompat.getColor(PersonalCarDetailActivity.this, R.color.divider));
                    if (Integer.valueOf(mCarDetail.state) == 1) {
                        mNameView.setText("已   售");
                    } else if (Integer.valueOf(mCarDetail.state) == 2) {
                        mNameView.setText("已下架");
                    } else if (Integer.valueOf(mCarDetail.state) == 3) {
                        mNameView.setText("待上架");
                    }
                } else {
                    mNameView.setText(mCarDetail.contacts); //设置联系人
                }

                mToolbar.setTitle(mCarDetail.car_name);
                mTopView.initData(mCarDetail);
                mFirstView.setText(String.format("上牌时间: %s", mCarDetail.first_on_card.substring(0, 7)));
                mAddressView.setText(String.format("上牌地点: %s %s", mCarDetail.province, mCarDetail.city));
                mAgeView.setText(String.format("车龄: %s", mCarDetail.getCarAgeStr()));
                mMileageView.setText(String.format("行驶里程: %s万公里", mCarDetail.car_mileage));

                temp = new ArrayList<>();
                if (!TextUtils.isEmpty(mCarDetail.transmission))
                    temp.add(new QADetail(mCarDetail.transmission, "变速箱: "));
                if (!TextUtils.isEmpty(mCarDetail.engine))
                    temp.add(new QADetail(mCarDetail.engine, "发动机: "));
                if (!TextUtils.isEmpty(mCarDetail.inspection_date))
                    temp.add(new QADetail(mCarDetail.inspection_date, "年检到期: "));
                if (!TextUtils.isEmpty(mCarDetail.sali_date))
                    temp.add(new QADetail(mCarDetail.sali_date, "交强险到期: "));
                if (!TextUtils.isEmpty(mCarDetail.warranty_date))
                    temp.add(new QADetail(mCarDetail.warranty_date, "质保到期: "));
                if (!TextUtils.isEmpty(mCarDetail.insurance_date))
                    temp.add(new QADetail(mCarDetail.insurance_date, "商业险到期: "));

                for (int i = 0; i < temp.size(); i++) {
                    QADetail detail = temp.get(i);
                    switch (i) {
                        case 0:
                            mTemp1View.setText(detail.key + detail.value);
                            break;
                        case 1:
                            mTemp2View.setText(detail.key + detail.value);
                            break;
                        case 2:
                            mTemp3View.setText(detail.key + detail.value);
                            break;
                        case 3:
                            mTemp4View.setText(detail.key + detail.value);
                            break;
                        case 4:
                            mTemp5View.setText(detail.key + detail.value);
                            break;
                        case 5:
                            mTemp6View.setText(detail.key + detail.value);
                            break;
                    }
                }
                if (temp.size() == 0) {
                    mTempParent1View.setVisibility(View.GONE);
                    mTempParent2View.setVisibility(View.GONE);
                    mTempParent3View.setVisibility(View.GONE);
                } else if (temp.size() <= 2) {
                    mTempParent2View.setVisibility(View.GONE);
                    mTempParent3View.setVisibility(View.GONE);
                } else if (temp.size() <= 4) {
                    mTempParent3View.setVisibility(View.GONE);
                }

                ImageUtil.setImage(mContext, mSellerPhotoView, mCarDetail.customer_head_img, R.mipmap.ic_individual);
                mSellerNameView.setText(String.format("商家: %s (个人)", mCarDetail.contacts));
                mSellerAddressView.setText(String.format("地址: %s", mCarDetail.look_address));
                mSellerDescriptionView.setText(mCarDetail.owners);

                mListView.setAdapter(new BuyCarListAdapter(mContext, mCarDetail.recommend_car_source));
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mCarDetail.recommend_car_source.get(position).isPersonal()) {
                            UsedCarActivitySwitcher.toPersonalCarDetail(mContext, mCarDetail.recommend_car_source.get(position).car_source_no);
                        } else {
                            UsedCarActivitySwitcher.toPlatformCarDetail(mContext, mCarDetail.recommend_car_source.get(position).car_source_no);
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mBottomView.setVisibility(View.GONE);
                mMonitorView.setVisibility(View.GONE);
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.personal_config && null != mCarDetail) { //参数配置
            ActivitySwitchBase.toH5Activity(this, "参数配置", mCarDetail.outer_inner_img);

        } else if (id == R.id.personal_bottom && null != mCarDetail) { //卖家电话
            new android.support.v7.app.AlertDialog.Builder(this, R.style.MaterialDialog)
                    .setTitle("拨打电话")
                    .setMessage("" + mCarDetail.phone_num)
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCarDetail.phone_num)));
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
    }

    @Override
    public void onScrollChange(float f1) {
        mToolbar.getBackground().setAlpha((int) (f1 * 255));
        String alpha = Integer.toHexString((int) (f1 * 255));
        if (alpha.length() == 1) {
            alpha = "0" + alpha;
        }
        mToolbar.setTitleTextColor(Color.parseColor("#" + alpha + "ffffff"));
    }

    @Override
    public void moveDown() {
    }

    @Override
    public void moveUp() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome_hy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
