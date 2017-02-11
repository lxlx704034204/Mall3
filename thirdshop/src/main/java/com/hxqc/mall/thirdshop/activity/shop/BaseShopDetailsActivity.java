package com.hxqc.mall.thirdshop.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;

/**
 * liaoguilong
 * Created by CPR113 on 2016/4/5.
 * 店铺详情base页
 */
public class BaseShopDetailsActivity extends BackActivity {
    public ShopDetailsController mShopDetailsController;
    protected ShopDetailsHeadView mShopDetailsHeadView;
    private ShareController mShareController; //分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShopDetailsController = ShopDetailsController.getInstance();
        if(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) !=null)
        {
            if(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopDetailsController.SHOPID_KEY) !=null) {
                mShopDetailsController.setShopID(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopDetailsController.SHOPID_KEY));
            }
            if(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).containsKey(ShopDetailsController.FROM_KEY))
                mShopDetailsController.setFrom(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(ShopDetailsController.FROM_KEY));
        }

        mShopDetailsController.page++;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.t_menu_to_share_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_to_home) {
            mShopDetailsController.page=0;
            ActivitySwitcherThirdPartShop.toMain(this, 0);
            mShopDetailsController.onDestroy();
        } else if (item.getItemId() == R.id.action_to_share) {
            //分享
            toShare();
        }
        return false;
    }


    //分享
    public void toShare() {
        if (mShareController == null) {
            mShareController = new ShareController(this);
        }
        if (mShopDetailsController.getThirdPartShop() == null || mShopDetailsController.getThirdPartShop().getShare() == null) {
            return;
        }
        ShareContent shareContent = mShopDetailsController.getThirdPartShop().getShare();
        shareContent.setContext(this);
        mShareController.showSharePopupWindow(shareContent);

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareController != null) {
            mShareController.onActivityResult(this, requestCode, resultCode, data);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShopDetailsController.page--;
        mShopDetailsController.onDestroy();
    }
}
