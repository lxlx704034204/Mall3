//package com.hxqc.mall.thirdshop.maintenance.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-04-23
// * FIXME
// * Todo
// */
//public class FourSAndQuickMaintainAndRepairActivity extends BackActivity {
//
//    private LinearLayout mParentView;
//
//    private String shopID;
//    private LinearLayout ll_parent_layer;
//    private FourSAndQuickHelper fourSAndQuickHelper;
//    private Button mConfirmView;
//    private static int RESULT_CONFRIM = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maintain_repair);
//
//        mParentView = (LinearLayout) findViewById(R.id.ll_parent);
//        mConfirmView = (Button) findViewById(R.id.finish_btn);
//        fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
//        shopID = fourSAndQuickHelper.getShopID();
//        initDate(fourSAndQuickHelper.getMaintenanceItems());
//
//        mConfirmView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                returnItem();
//            }
//        });
//    }
//
//    private void initDate(final ArrayList<MaintenanceItemN> repairMaintenanceItems) {
//
//        for(int i = 0 ; i< repairMaintenanceItems.size() ; i++){
//            ll_parent_layer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_maintain_repair_parent,mParentView);
//            final MaintainAndRepairParent childView  = (MaintainAndRepairParent) ll_parent_layer.getChildAt(i);
//            childView.initDate(repairMaintenanceItems,i,shopID, ll_parent_layer);
////            childView.setOnCheckClick(new MaintainAndRepairParent.onCheckClick() {
////                @Override
////                public void onCheckClick(ArrayList<MaintenanceItemN> maintenanceItemNs,int position) {
////                    ll_parent_layer.removeAllViews();
////                    initDate(repairMaintenanceItems);
////                }
////            });
//        }
//    }
//
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.mune_maintain_repair, menu);
////        return true;
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////
////        if(item.getItemId() == R.id.action_confirm){
////            returnItem();
////        }
////        return  false;
////    }
//
//    private void returnItem() {
//
//        fourSAndQuickHelper.returnItem(ll_parent_layer);
//
//        Intent intent = new Intent();
//
//        this.setResult(RESULT_CONFRIM,intent);
//        this.finish();
//    }
//
//
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
//}
