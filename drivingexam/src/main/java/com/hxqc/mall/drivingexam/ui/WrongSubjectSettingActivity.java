package com.hxqc.mall.drivingexam.ui;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.core.views.CustomToolBar;


/**
 * 我的错题设置界面
 * Created by zhaofan on 2016/8/4.
 */
@Deprecated
public class WrongSubjectSettingActivity extends initActivity {
    private CheckBox mRemoveWrongCb;
    private CustomToolBar mToolBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wrongsubject_setting;
    }

    @Override
    public void bindView() {
        mRemoveWrongCb = (CheckBox) findViewById(R.id.remove_wrong_cb);
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
    }

    @Override
    public void init() {
        mToolBar.setTitle("我的错题");
        mRemoveWrongCb.setChecked(((Boolean) mSpUtils.get(C.REMOVE_WRONG_SUB_SETTING, true)));
        mRemoveWrongCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSpUtils.put(C.REMOVE_WRONG_SUB_SETTING, isChecked);
            }
        });

    }
}
