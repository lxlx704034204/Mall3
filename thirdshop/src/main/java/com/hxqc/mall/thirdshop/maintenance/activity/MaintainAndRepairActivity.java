//package com.hxqc.mall.thirdshop.maintenance.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.control.MaintenanceHelper;
//import com.hxqc.mall.thirdshop.maintenance.views.MaintainAndRepairParent;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-02-29
// * FIXME
// * Todo 保养与维修项目的Activity
// */
//public class MaintainAndRepairActivity extends BackActivity {
//
//
//    private LinearLayout mParentView;
//    private RequestFailView mFailView;
//
//    private String shopID;
//    private LinearLayout ll_parent_layer;
//    private MaintenanceHelper maintenanceHelper;
//
//    private static int RESULT_CONFRIM = 1;
//    private static int RESULT_NOT_CONFRIM = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maintain_repair);
//
//
//        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
//        mFailView = (RequestFailView) findViewById(R.id.fail_view);
//        maintenanceHelper = MaintenanceHelper.getInstance();
//        shopID = maintenanceHelper.getShopID();
//        getDate();
//
//
//    }
//
//    private void getDate() {
//        maintenanceHelper.getMaintenanceItems(this, new MaintenanceHelper.getMaintenanceItemsHandle() {
//            @Override
//            public void onSuccess(ArrayList<MaintenanceItem> repairMaintenanceItems) {
//                initDate(repairMaintenanceItems);
//            }
//
//            @Override
//            public void onFailure() {
//                showFailView();
//            }
//        });
//    }
//
//    private void initDate(ArrayList<MaintenanceItem> repairMaintenanceItems) {
//
//        for(int i = 0 ; i< repairMaintenanceItems.size() ; i++){
//            ll_parent_layer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_maintain_repair_parent,mParentView);
//            MaintainAndRepairParent childView  = (MaintainAndRepairParent) ll_parent_layer.getChildAt(i);
//            childView.initDate(repairMaintenanceItems.get(i),shopID);
//        }
//    }
//
//    //错误界面的显示
//    private void showFailView() {
//        mParentView.setVisibility(View.GONE);
//        mFailView.setVisibility(View.VISIBLE);
//        mFailView.setEmptyDescription("网络连接失败");
//        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDate();
//            }
//        });
//        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mune_maintain_repair, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(item.getItemId() == R.id.action_confirm){
//            returnItem();
//        }
//        return  false;
//    }
//
//    //点了确定按钮返回
//    private void returnItem() {
//
//        maintenanceHelper.returnItem(ll_parent_layer);
//
//        Intent intent = new Intent();
//
//        this.setResult(RESULT_CONFRIM,intent);
//        this.finish();
//    }
//
//
//    //没点确定按钮返回
//    private void returnNotConfirm(){
//        Intent intent = new Intent();
//
//        this.setResult(RESULT_NOT_CONFRIM,intent);
//        this.finish();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//
//
//}
//
