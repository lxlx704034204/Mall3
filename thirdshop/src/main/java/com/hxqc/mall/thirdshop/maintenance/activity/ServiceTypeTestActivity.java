package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ServiceTypeMaintainListAdapter;
import com.hxqc.mall.thirdshop.maintenance.config.MaintainContants;

import java.util.HashMap;

/**
 * Author :胡仲俊
 * Date : 2016-03-08
 * FIXME
 * Todo 服务类型
 */
@Deprecated
public class ServiceTypeTestActivity extends BackActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "ServiceTypeTestActivity";
    private ImageView mRepairImgView;
    private ImageView mMaintenanceImgView;
    private ImageView mSpryImgView;
    private ImageView mInspectImgView;
    private CheckBox mRepairCBView;
    private CheckBox mMaintenanceCBView;
    private CheckBox mSpryCBView;
    private CheckBox mInspectCBView;
    private TextView mRepairContentView;
    private TextView mMaintenanceContentView;
    private TextView mSpryContentView;
    private TextView mInspectContentView;
    private RelativeLayout mSprayItemView;
    private RelativeLayout mInspectItemView;
    private RecyclerView mRepairListView;

    private HashMap<Integer, String> serviceTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type_test);

        initView();

        initEvent();

        initData();

    }

    private void initData() {
        serviceTypeList = new HashMap<Integer, String>();
    }

    private void initEvent() {

        mSprayItemView.setOnClickListener(this);
        mInspectItemView.setOnClickListener(this);

        mRepairCBView.setOnCheckedChangeListener(this);
        mMaintenanceCBView.setOnCheckedChangeListener(this);
        mSpryCBView.setOnCheckedChangeListener(this);
        mInspectCBView.setOnCheckedChangeListener(this);

        ServiceTypeMaintainListAdapter serviceTypeMaintainListAdapter = new ServiceTypeMaintainListAdapter(ServiceTypeTestActivity.this);
        mRepairListView.setHasFixedSize(true);
        mRepairListView.setLayoutManager(new GridLayoutManager(ServiceTypeTestActivity.this, 2));
        mRepairListView.setAdapter(serviceTypeMaintainListAdapter);

    }

    private void initView() {

        mSprayItemView = (RelativeLayout) findViewById(R.id.service_type_spray_item);
        mInspectItemView = (RelativeLayout) findViewById(R.id.service_type_inspect_item);

        mRepairListView = (RecyclerView) findViewById(R.id.service_type_repair_list);

        mRepairImgView = (ImageView) findViewById(R.id.service_type_repair_img);
        mMaintenanceImgView = (ImageView) findViewById(R.id.service_type_maintain_img);
        mSpryImgView = (ImageView) findViewById(R.id.service_type_spry_img);
        mInspectImgView = (ImageView) findViewById(R.id.service_type_inspect_img);

        mRepairContentView = (TextView) findViewById(R.id.service_type_repair_tv);
        mMaintenanceContentView = (TextView) findViewById(R.id.service_type_maintain_tv);
        mSpryContentView = (TextView) findViewById(R.id.service_type_spry_tv);
        mInspectContentView = (TextView) findViewById(R.id.service_type_inspect_tv);

        mRepairCBView = (CheckBox) findViewById(R.id.serviec_type_repair_cb);
        mMaintenanceCBView = (CheckBox) findViewById(R.id.service_type_maintain_cb);
        mSpryCBView = (CheckBox) findViewById(R.id.service_type_spray_cb);
        mInspectCBView = (CheckBox) findViewById(R.id.service_type_inspect_cb);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_service_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_confirmation) {
            Intent intent = new Intent(ServiceTypeTestActivity.this, ReserveMaintainActivity.class);
            intent.putExtra("serviceTypeBackData",serviceTypeList);
            setResult(MaintainContants.SERVICE_TYPE_DATA_BACK, intent);
            finish();
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.serviec_type_repair_cb) {
            changeViewColor(mRepairImgView, mRepairContentView, mRepairCBView, isChecked);
        } else if (buttonView.getId() == R.id.service_type_maintain_cb) {
            changeViewColor(mMaintenanceImgView, mMaintenanceContentView, mMaintenanceCBView, isChecked);
        } else if (buttonView.getId() == R.id.service_type_spray_cb) {
            changeViewColor(mSpryImgView, mSpryContentView, mSpryCBView, isChecked);
        } else if (buttonView.getId() == R.id.service_type_inspect_cb) {
            changeViewColor(mInspectImgView, mInspectContentView, mInspectCBView, isChecked);
        }
    }

    /**
     * 根据选择情况改变相关颜色
     *
     * @param imageView
     * @param textView
     * @param isChecked
     */

    private void changeViewColor(ImageView imageView, TextView textView, CheckBox checkBox, boolean isChecked) {
        if (isChecked) {
            if (imageView.getId() == R.id.service_type_repair_img) {
//                imageView.setImageResource(R.drawable.ic_service_repair_sel);
                imageView.setSelected(true);
                serviceTypeList.put(0, "维修");
            } else if (imageView.getId() == R.id.service_type_maintain_img) {
//                imageView.setImageResource(R.drawable.ic_service_maintain_sel);
                imageView.setSelected(true);
                serviceTypeList.put(1, "保养");
            } else if (imageView.getId() == R.id.service_type_spry_img) {
//                imageView.setImageResource(R.drawable.ic_service_spray_sel);
                imageView.setSelected(true);
                serviceTypeList.put(2, "钣喷");
            } else if (imageView.getId() == R.id.service_type_inspect_img) {
//                imageView.setImageResource(R.drawable.ic_service_inspect_sel);
                imageView.setSelected(true);
                serviceTypeList.put(3, "检查故障");
            }
//            textView.setTextColor(getResources().getColor(R.color.service_type_common_bg));
            textView.setSelected(true);
//            checkBox.setBackgroundResource(R.drawable.ic_check_sel);
            checkBox.setSelected(true);
        } else {
            if (imageView.getId() == R.id.service_type_repair_img) {
//                imageView.setImageResource(R.drawable.ic_service_repair_def);
                imageView.setSelected(false);
                serviceTypeList.remove(0);
            } else if (imageView.getId() == R.id.service_type_maintain_img) {
//                imageView.setImageResource(R.drawable.ic_service_maintain_def);
                imageView.setSelected(false);
                serviceTypeList.remove(1);
            } else if (imageView.getId() == R.id.service_type_spry_img) {
//                imageView.setImageResource(R.drawable.ic_service_spray_def);
                imageView.setSelected(false);
                serviceTypeList.remove(2);
            } else if (imageView.getId() == R.id.service_type_inspect_img) {
//                imageView.setImageResource(R.drawable.ic_service_inspect_def);
                imageView.setSelected(false);
                serviceTypeList.remove(3);
            }
//            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setSelected(false);
//            checkBox.setBackgroundResource(R.drawable.ic_check_def);
            checkBox.setSelected(false);
        }

    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.service_type_spray_item) {
            Intent intent = new Intent(ServiceTypeTestActivity.this, SprayRepairActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.service_type_inspect_item) {
            Intent intent = new Intent(ServiceTypeTestActivity.this, InspectActivity.class);
            startActivity(intent);
        }

    }

}
