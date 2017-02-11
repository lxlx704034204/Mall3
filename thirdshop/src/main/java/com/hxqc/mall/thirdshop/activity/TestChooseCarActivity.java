package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.os.Bundle;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.control.FilterControllerForSpecialCar;


/**
 * Function: 选车主界面
 *
 * @author 袁秉勇
 * @since 2016年11月17日
 */
public class TestChooseCarActivity extends NoBackActivity implements ControllerConstruct {
    private final static String TAG = TestChooseCarActivity.class.getSimpleName();
    private Context mContext;

    private BaseFilterController baseFilterController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_choose_car);

        initController();
    }


    @Override
    public void initController() {
        baseFilterController = FilterControllerForSpecialCar.getInstance();
    }


    @Override
    public BaseFilterController getController() {
        return baseFilterController;
    }


    @Override
    public void destroyController() {
        if (baseFilterController != null) {
            baseFilterController.destroy();
            baseFilterController = null;
        }
    }
}
