package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BaseAtlasActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年12月12日
 */
public class ThirdShopAltasActivity extends BaseAtlasActivity {
    ThirdPartShopClient ThirdPartShopClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThirdPartShopClient = new ThirdPartShopClient();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getData(String itemID, LoadingAnimResponseHandler loadingAnimResponseHandler) {
        if (isExtID) {
            ThirdPartShopClient.thirdCarItemAtlas("extID", itemID, loadingAnimResponseHandler);
        } else
            ThirdPartShopClient.thirdCarItemAtlas(itemID, loadingAnimResponseHandler);
    }
}
