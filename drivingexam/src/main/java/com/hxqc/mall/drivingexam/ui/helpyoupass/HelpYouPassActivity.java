package com.hxqc.mall.drivingexam.ui.helpyoupass;

import android.view.View;
import android.widget.RelativeLayout;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.core.views.CustomToolBar;


/**
 * 学车帮你过
 * Created by zhaofan on 2016/8/23.
 */
public class HelpYouPassActivity extends initActivity implements View.OnClickListener {
    private CustomToolBar mToolBar;
    private RelativeLayout kemu3Experience;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helpyoupass;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        findViewById(R.id.kemu2_tuwen).setOnClickListener(this);
        findViewById(R.id.kemu2_jiqiao).setOnClickListener(this);
        findViewById(R.id.kemu3_dengguang).setOnClickListener(this);
        findViewById(R.id.kemu3_jiqiao).setOnClickListener(this);
        kemu3Experience = (RelativeLayout) findViewById(R.id.kemu3_experience);
        kemu3Experience.setOnClickListener(this);
    }

    @Override
    public void init() {
        mToolBar.setTitle("学车帮你过");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //科目2 图文
        if (id == R.id.kemu2_tuwen) {
            ActivitySwitcherExam.toPictureDescribe(this);
        }
        //科目2 技巧
        else if (id == R.id.kemu2_jiqiao) {
            ActivitySwitcherExam.toDrivingExperience(this, 2);
        }
        //科目3 灯光
        else if (id == R.id.kemu3_dengguang) {
            ActivitySwitcherExam.toLightAndVoice(this);
        }
        //科目3 技巧
        else if (id == R.id.kemu3_jiqiao) {
            ActivitySwitcherExam.toDrivingExperience(this, 3);
        } else if (id == R.id.kemu3_experience) {
            ActivitySwitcherExam.toKemu3PassExperience(this);
        }


    }
}
