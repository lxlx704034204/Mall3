package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.OtherMaintenancePackageListAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.PackageListAdapter;
import com.hxqc.mall.thirdshop.maintenance.control.HomeDataHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-23
 * FIXME
 * Todo 优惠套餐列表
 */
public class PackageListActivity extends BackActivity implements OtherMaintenancePackageListAdapter.OnPackageClickListener {
    public static final String SHOP_ID = "PackageListActivity.shopID";
    public static final String AUTO = "PackageListActivity.auto";
    public static final String TYPE = "PackageListActivity.type";
    private ListView mListView;
    private PackageListAdapter adapter;
    private ArrayList<MaintenancePackage> packages;
    private String shopID;
    private MyAuto auto;
    private OtherMaintenancePackageListAdapter.Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        initView();
        initData();
        initAdapter();
        initEvent();
        loadData();
    }


    private void initView() {
        mListView = (ListView) findViewById(R.id.package_list);
    }

    private void initData() {
        packages = new ArrayList<>();
        shopID = getIntent().getStringExtra(SHOP_ID);
        auto = getIntent().getParcelableExtra(AUTO);
        type = (OtherMaintenancePackageListAdapter.Type) getIntent().getSerializableExtra(TYPE);
    }

    private void initAdapter() {
        adapter = new PackageListAdapter(packages, this);
        adapter.type = type;
        mListView.setAdapter(adapter);
    }

    private void initEvent() {
        adapter.setOnPackageClickListener(this);
    }

    private void loadData() {
        HomeDataHelper.getInstance(this).getPackageList(shopID, auto.autoModelID, type,
                new LoadDataCallBack<ArrayList<MaintenancePackage>>() {
                    @Override
                    public void onDataNull(String message) {

                    }

                    @Override
                    public void onDataGot(ArrayList<MaintenancePackage> obj) {
                        packages.addAll(obj);
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onPackageClick(int position, OtherMaintenancePackageListAdapter.Type type) {
//        if (type == OtherMaintenancePackageListAdapter.Type.OTHER)
//            ActivitySwitcherMaintenance.toSmartMaintenance(this, auto, "2",
//                    shopID, packages.get(position).packageID);
//        else ActivitySwitcherMaintenance.toSmartMaintenance(this, auto, "3",
//                shopID, packages.get(position).packageID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomeDataHelper.getInstance(this).destory();
    }
}
