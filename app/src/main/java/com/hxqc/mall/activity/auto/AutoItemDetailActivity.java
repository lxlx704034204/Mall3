package com.hxqc.mall.activity.auto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.control.AutoItemDataControl;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.fragment.auto.AutoBuyVerifyFragment;
import com.hxqc.mall.views.BottomBarToBuy;
import com.hxqc.mall.views.auto.AutoDetailIntroduce;
import com.hxqc.mall.views.auto.AutoDetailPackageGroup;
import com.hxqc.mall.views.auto.AutoDetailPickupPoint;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;
import com.umeng.socialize.UMShareAPI;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import hxqc.mall.R;


/**
 * Author: HuJunJie Date: 2015-04-07 FIXME Todo 车辆详情
 */
public abstract class AutoItemDetailActivity extends AppBackActivity implements
        AutoItemDataControl.ItemDetailListenerConfig, AutoItemDataControl.ItemDetailListener {
    public static final int PACKAGE_RESULT = 1000;//套餐返回

    OverlayDrawer mOverlayDrawer;
    //    WebView mIntroduceView;
    ImageView mBannerView;
    String itemID;

    AutoBuyVerifyFragment mVerifyFragment;
    AutoItemDataControl mAutoItemDataControl;
    BottomBarToBuy bottomBarToBuyView;
    View mContentView;//主体View
    TextView mNameView;
    RequestFailView mRequestFailView;
    ShareController shareController;//分享
    AutoDetailPackageGroup mAutoDetailPackageGroup;//套餐
    AutoDetailIntroduce mIntroduceView;//车辆详情
    AutoDetailPickupPoint mPickupPointView;//自提点
    View mSpaceView;//底部占位view
    View mBottomBar;
    View mUnavailableView;//下架提示
    int itemCategory = 10;// 车辆类型

    abstract int getContentView();

    @Override
    public int getItemCategory() {
        return itemCategory;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        EventBus.getDefault().register(this);
        itemID = getIntent().getStringExtra(ActivitySwitcher.KEY_DATA);
        if (getIntent().hasExtra(ActivitySwitcher.TITLE)) {
            String title = getIntent().getStringExtra(ActivitySwitcher.TITLE);
            setBarTitle(title);
        }
        itemCategory = getIntent().getIntExtra(AutoItem.ItemCategory, 10);

        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(true);

        mBannerView = (ImageView) findViewById(R.id.banner);
        mIntroduceView = (AutoDetailIntroduce) findViewById(R.id.introduce);
        mNameView = (TextView) findViewById(R.id.auto_descriptions);
        mVerifyFragment = (AutoBuyVerifyFragment) getSupportFragmentManager().findFragmentById(R.id.verify_fragment);
        bottomBarToBuyView = (BottomBarToBuy) findViewById(R.id.tobuy_view);
        mContentView = findViewById(R.id.detail_content_layout);

        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        mAutoDetailPackageGroup = (AutoDetailPackageGroup) findViewById(R.id.item_package_group);
        mPickupPointView = (AutoDetailPickupPoint) findViewById(R.id.pickup_points);
        mSpaceView = findViewById(R.id.space);
        mBottomBar = findViewById(R.id.bottom_bar);
        mUnavailableView = findViewById(R.id.auto_unavailable);


        ImageView mProcessView = (ImageView) findViewById(R.id.detail_process);
        ImageUtil.setImage(this, mProcessView, R.drawable.pic_process);

        ImageView mIntroductionView = (ImageView) findViewById(R.id.detail_introduction);
        ImageUtil.setImage(this, mIntroductionView, R.drawable.pic_introduction);
        ImageView mStrengthView = (ImageView) findViewById(R.id.detail_pic_strength);
        ImageUtil.setImage(this, mStrengthView, R.drawable.pic_strength);

        String phoneNumber = UserInfoHelper.getInstance().getPhoneNumber(getApplicationContext());
//        ChatManager.getInstance().userLogin(getApplicationContext(), phoneNumber, phoneNumber, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAutoItemDataControl = AutoItemDataControl.getInstance();
        mAutoItemDataControl.setConfig(this);
        mAutoItemDataControl.autoDetailAction(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void setBarTitle(String title) {
        if (title.contains(" ")) {
            title = (title.split(" ")[0]);
        }
        setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareController != null) {
            shareController.onActivityResult(this, requestCode, resultCode, data);
        }
        if (resultCode == PACKAGE_RESULT) {
            //套餐
            mAutoDetailPackageGroup.notifyDataSetChanged(
                    mAutoItemDataControl.getAutoPackages(), mAutoItemDataControl.getCheckAutoPackage());
            mVerifyFragment.setPackVerify();
        }

    }


    //参数详情
    public void clickToParameter(View view) {
//        AutoDetail autoDetail = mAutoItemDataControl.getAutoDetail();
        ActivitySwitcher.toParameter(AutoItemDetailActivity.this, itemID);
    }

    //评论列表
    public void clickToComment(View view) {
        AutoDetail autoDetail = mAutoItemDataControl.getAutoDetail();
        ActivitySwitcher.toComment(AutoItemDetailActivity.this, autoDetail.getItemID()
                , autoDetail.getSeriesID());
    }

    //分享
    public void clickShare(View view) {
        if (shareController == null) {
            shareController = new ShareController(this);
        }
        UMShareAPI.get(this);
        if (mAutoItemDataControl.getAutoDetail() == null) {
            return;
        }
        ShareContent shareContent = mAutoItemDataControl.getAutoDetail().getShare();
        shareContent.setContext(this);
        shareController.showSharePopupWindow(shareContent);
    }

    //呼叫客服
    public void clickCallService(View view) {
        AutoDetail autoDetail = mAutoItemDataControl.getAutoDetail();
        if (autoDetail == null) return;
//        ChatManager.getInstance().startChatWithGoods(itemID,
//                autoDetail.getAutoBaseInformation().getItemDescription(),
//                OtherUtil.amountFormat(autoDetail.getItemPrice(), true),
//                autoDetail.getAutoBaseInformation().getItemThumb(), "电商直营-车辆详情", "");
    }

    //购买菜单
    public void openBuyVerify(View view) {
        AutoDetail autoDetail = mAutoItemDataControl.getAutoDetail();
        if (autoDetail == null) {
            return;
        }
        switch (autoDetail.transactionStatus()) {
            case NORMAL:
                if (!mOverlayDrawer.isMenuVisible()) {
                    mOverlayDrawer.openMenu();
                    mVerifyFragment.setPackVerify();
                }
                break;
        }
    }

    //打开QA
    public void openToQA(View view) {
        ActivitySwitcher.toQAPage(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getItemID() {
        return itemID;
    }


    @Override
    public void onItemDetailSuccess(AutoDetail autoDetail) {
//        mRefreshRequestFailView.onRefreshSuccess(mContentView);
        if (autoDetail == null) return;
        if (TextUtils.isEmpty(getTitle().toString())) {
            setBarTitle(autoDetail.getAutoBaseInformation().getItemDescription());
        }
        mRequestFailView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        bottomBarToBuyView.setVisibility(View.VISIBLE);
        AutoBaseInformation autoBaseInformation = autoDetail.getAutoBaseInformation();
        mNameView.setText(autoDetail.getItemDescription());
        ImageUtil.setImage(AutoItemDetailActivity.this, mBannerView, autoBaseInformation.itemPic);
        mVerifyFragment.refresh(autoDetail);
        mIntroduceView.setAutoDetail(autoDetail);
        mAutoDetailPackageGroup.notifyDataSetChanged(autoDetail.getAutoPackages(), mAutoItemDataControl
                .getCheckAutoPackage());
        mPickupPointView.setPickupPoints(autoDetail.getPickupPoint());

    }


    @Override
    public void onItemDetailFailure(com.hxqc.mall.core.model.Error error) {
//        mRefreshRequestFailView.onRefreshFailure(mContentView);
        String message;
        if (error == null) {
            message = "网络连接失败请重试";
        } else {
            message = error.message;
        }
        mRequestFailView.setEmptyDescription(message);
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }


    /**
     * 图集
     */
    public void toPictures(View view) {
        try {
            String itemID = mAutoItemDataControl.getAutoDetail().getAutoBaseInformation().getItemID();
            ActivitySwitcher.toAtlas(this, itemID);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void finish() {
        super.finish();
        mAutoItemDataControl.destroy();
    }

    //接收 自提点的店名
    @Subscribe
    public void onEventMainThread(PickupPointT event) {
        if (event != null) {
            mPickupPointView.setPickupPoint(event);
        }
    }

    /**
     * 车辆是否下架
     *
     * @param isAvailable
     */
    protected void autoAvailable(boolean isAvailable) {
        if (isAvailable) {
            mUnavailableView.setVisibility(View.VISIBLE);
            mSpaceView.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
        } else {
            mUnavailableView.setVisibility(View.GONE);
            mSpaceView.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_to_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_to_home:
                ActivitySwitcher.toMain(this, 0);
                finish();
                break;
            case R.id.action_message:
                ActivitySwitcher.toMyMessageActivity(this);

                break;
        }
        return false;
    }
}
