package com.hxqc.mall.drivingexam.ui.homepage;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.core.views.CustomToolBar;

import java.util.Arrays;

/**
 * 驾考首页
 * Created by zhaofan on 2016/8/15.
 */
public class ExamHomePageActivity extends initActivity implements View.OnClickListener {
    private CustomToolBar mToolBar;
    private GridView gv;
    ScheduleAdapter mScheduleAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exam_home_page;
    }

    @Override
    public void bindView() {
        gv = (GridView) findViewById(R.id.gv);
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        findViewById(R.id.exam).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        findViewById(R.id.practice_law).setOnClickListener(this);
        findViewById(R.id.skill).setOnClickListener(this);
    }

    @Override
    public void init() {
        mToolBar.setTitle("考驾照");
        initRightBtn();
        initGridView();

    }

    private void initGridView() {
        String[] str = {"报名", "体检及缴费", "笔试科目一", "路考科目二", "路考科目三", "笔试科目四", "领取驾驶证", ""};
        mScheduleAdapter = new ScheduleAdapter(this);
        mScheduleAdapter.setData(Arrays.asList(str));
        gv.setAdapter(mScheduleAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySwitchBase.toH5Activity(mContext, "流程说明", "/Service/V2/Cardriving/index");
            }
        });
    }


    private void initRightBtn() {
        TextView mToolBarRightTv = (TextView) mToolBar.findViewById(R.id.topbar_right_tv);
        mToolBarRightTv.setText("流程说明");
        mToolBarRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toH5Activity(mContext, "流程说明", "/Service/V2/Cardriving/index");
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.help) {
            ActivitySwitcherExam.toHelpPassActivity(this);
        } else if (id == R.id.exam) {
            ActivitySwitcherExam.toChooseKemuActivity(this);
        } else if (id == R.id.practice_law) {
            ActivitySwitcherExam.practiceLaw(this);
        } else if (id == R.id.skill) {
            ActivitySwitcherExam.rookieSkill(this);
        }
    }
}
