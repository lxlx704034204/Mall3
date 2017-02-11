package com.hxqc.mall.activity.auto;

import android.os.Bundle;

import com.hxqc.mall.activity.BaseAtlasActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;

/**
 * 图集
 */

public class AtlasActivity extends BaseAtlasActivity {

    NewAutoClient newAutoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        newAutoClient = new NewAutoClient();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getData(String itemID, LoadingAnimResponseHandler loadingAnimResponseHandler) {
        newAutoClient.autoItemAtlas(itemID, getLoadingAnimResponseHandler());
    }
}
