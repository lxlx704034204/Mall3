package com.hxqc.mall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.mall.activity.LaunchActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.reactnative.util.RNConfigUtil;
import com.hxqc.mall.thirdshop.activity.GroupBuyMergeActivity;
import com.hxqc.tinker.BuildInfo;
import com.hxqc.tinker.util.TinkerAutoFixStartUtil;
import com.hxqc.util.DebugLog;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import hxqc.mall.R;


public class TestDemoActivity extends DevDemoActivity {
    protected int getContentViewLayout() {
        return R.layout.activity_test_demo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button loadPatchButton = (Button) findViewById(R.id.loadPatch);

        loadPatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TinkerAutoFixStartUtil().requestForPatch(TestDemoActivity.this);
            }
        });


        Button killSelfButton = (Button) findViewById(R.id.killSelf);

        killSelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        Button buildInfoButton = (Button) findViewById(R.id.showInfo);

        buildInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(TestDemoActivity.this);
            }
        });
    }


    public void toAutoCalendar(View view) {
        ActivitySwitchAutoInformation.toNewAutoCalendar(this);
    }


    public void groupBuy(View view) {
        Intent intent = new Intent(this, GroupBuyMergeActivity.class);
        startActivity(intent);
    }


    public void toMaintianceAppointment(View view) {
//        Intent intent = new Intent(this, FilterMaintenanceShopListActivity.class);
//        startActivity(intent);
        AutoInfoControl.getInstance().toActivityInter(this, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, null);
    }


    public boolean showInfo(Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }

        final TextView v = new TextView(context);
        v.setText(sb);
        v.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setTextColor(0xFF000000);
        v.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        v.setPadding(padding, padding, padding, padding);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(v);
        final AlertDialog alert = builder.create();
        alert.show();
        return true;
    }


    public void toReleaseTestRN(View view) {
        RNConfigUtil.isRNDebug = true;
        DebugLog.setDebug(true);
        ApiUtil.isDebug = false;
        ApiUtil.setTestHostUrl(http + "://app-interface.t.hxqc.com/");
        ApiUtil.AccessoryHostURL = http + "://accessory-goods.tadmin.hxqc.com/";
        ApiUtil.MaintainHostURL = http + "://maintain-interface-goods.tadmin.hxqc.com/";
        ApiUtil.UsedCarHostURL = http + "://usedcar-interface.t.hxqc.com/";
        ApiUtil.Host_Insurance = http + "://value-added.tm.hxqc.com";
        ApiUtil.APPConfigHostURL = http + "://appconf.t.hxqc.com";
        ApiUtil.RNBsdiffURL = http + "://appconf.t.hxqc.com";
        ApiUtil.ConfigAppKey = "f9faee90-3476-46f2-872d-86e3b2b45921";
        ApiUtil.UsedCarAuctionHostURL = "http://usedcar.tm.hxqc.com";
        DebugLog.w("bsdiff_rn", "toReleaseTest: " + ApiUtil.getHomeRNURLHost());
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }


    public void to203RN(View view) {
        RNConfigUtil.isRNDebug = true;
        DebugLog.setDebug(true);
        ApiUtil.isDebug = true;
        ApiUtil.setTestHostUrl("http://app-interface.t.hxqctest.com/");
        ApiUtil.AccessoryHostURL = "http://accessory-goods.tadmin.hxqctest.com/accessory/";
        ApiUtil.MaintainHostURL = "http://maintain-interface-goods.tadmin.hxqctest.com/";
        ApiUtil.UsedCarHostURL = "http://app-interface.t.hxqctest.com";
        ApiUtil.Host_Insurance = "http://value-added.tm.hxqctest.com";
        ApiUtil.APPConfigHostURL = "http://10.0.15.205:8361";
        ApiUtil.RNBsdiffURL = "http://10.0.15.205:8361";
        ApiUtil.ConfigAppKey = "4729e28f-ded5-4b71-8124-21d88af41817";
        ApiUtil.UsedCarAuctionHostURL = "http://usedcar.tm.hxqctest.com";
        DebugLog.w("bsdiff_rn", "to203: " + ApiUtil.getHomeRNURLHost());
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
}
