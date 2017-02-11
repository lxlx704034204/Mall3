package com.hxqc.autonews.activities;

import android.os.Bundle;

import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.fragments.PublicCommentFragment;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import hxqc.mall.R;

/**
 * 口碑评价
 * Created by huangyi on 16/10/17.
 */
public class PublicCommentActivity extends BackActivity {

    public static final String EXT_ID = "ext_id";
    public static final String BRAND = "brand";
    public static final String SERIES = "series";
    public String extID, brand, series;
    public AutoInformationApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_comment);

        extID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(EXT_ID);
        brand = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(BRAND);
        series = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(SERIES);
        client = new AutoInformationApiClient();
        getSupportFragmentManager().beginTransaction().replace(R.id.comment_list, new PublicCommentFragment()).commit();
    }

}
