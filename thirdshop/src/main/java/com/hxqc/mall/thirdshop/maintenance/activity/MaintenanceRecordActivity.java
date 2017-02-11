package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.api.AutoInfoClient;
import com.hxqc.mall.auto.model.MaintenanceRecord;
import com.hxqc.mall.auto.model.Record;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceRecordListAdapter;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-25
 * FIXME
 * Todo 保养记录
 */
@Deprecated
public class MaintenanceRecordActivity extends BackActivity {
    public static final String AUTO_ID = "auto_ID";
    public static final String VIN_CODE = "VIN_code";
    private ImageView autoIcon;
    private TextView autoModel, autoPlate, nextMaintenanceTime, nextRepairAdvice;
    private ListViewNoSlide maintenanceRecordList;
    private String autoID;//车辆id
    private String VIN;//车辆VIN码
    private ArrayList<Record> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_record);
        autoIcon = (ImageView) findViewById(R.id.auto_icon);
        autoModel = (TextView) findViewById(R.id.auto_model);
        autoPlate = (TextView) findViewById(R.id.auto_plate);
        nextMaintenanceTime = (TextView) findViewById(R.id.next_maintenance_time);
        nextRepairAdvice = (TextView) findViewById(R.id.next_repair_advice);
        maintenanceRecordList = (ListViewNoSlide) findViewById(R.id.maintenance_record_list);
        maintenanceRecordList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        autoID = getIntent().getStringExtra(AUTO_ID);
        VIN = getIntent().getStringExtra(VIN_CODE);
        loadData();
    }

    /**
     * 添加数据
     */
    private void loadData() {
        /*模拟数据*/
//        autoModel.setText("凯迪拉克SRX 2015款 3.0L 至尊土豪豪华型");
//        autoPlate.setText(getString(R.string.auto_plate) + "鄂A123355");
//        nextMaintenanceTime.setText(getString(R.string.next_maintenance_time) + "2016-02-16");
//        nextRepairAdvice.setText(getString(R.string.next_repair_advice) + "更换火花塞");

//        for (int i = 0; i < 9; i++) {
//            MaintenanceRecord record = new MaintenanceRecord();
//            record.content = "保养内容囧到翻爱撒旦法拉克";
//            record.count = 12453;
//            record.date = "2015-02-12";
//            record.distance = 5412154;
//            record.shop = "武汉星凯凯迪拉克";
//            records.add(record);
//        }

        new AutoInfoClient().getMaintenanceRecord(autoID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                MaintenanceRecord maintenanceRecords = JSONUtils.fromJson(response, MaintenanceRecord.class);
                if (maintenanceRecords != null) {
                    records.clear();
                    records.addAll(maintenanceRecords.record);
                }
            }
        });
        MaintenanceRecordListAdapter adapter = new MaintenanceRecordListAdapter(records, this);
        maintenanceRecordList.setAdapter(adapter);
        maintenanceRecordList.setItemChecked(0, true);
    }

    public void toSeeDetail(View view) {
        //去看看
        ActivitySwitcherMaintenance.toNormalMaintenance(this);
    }
}
