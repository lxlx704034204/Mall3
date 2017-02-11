package com.hxqc.mall.thirdshop.maintenance.fragment;

import android.content.Context;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;

/**
 * Author:李烽
 * Date:2016-02-23
 * FIXME
 * Todo
 */
public class MaintenanceBaseFragment extends FunctionFragment  {
//    protected OnMaintenanceListener onMaintenanceListener;
    protected MaintenanceClient maintenanceClient;

//    @Override
//    public void updateModel(String series, String seriesID, String model, String modelID) {
//
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        onMaintenanceListener = (OnMaintenanceListener) context;
        maintenanceClient = new MaintenanceClient();
    }

    @Override
    public String fragmentDescription() {
        return null;
    }

//    public interface OnMaintenanceListener {
//        String getShopID();
//
//        String getBrand();
//
//        String getShopBrandID();
//
//        void openSlidMenu();
//    }
}
