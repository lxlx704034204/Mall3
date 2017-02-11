package com.hxqc.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.thirdshop.maintenance.activity.MaintenanceCommentListActivity;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;


public class TestMapNvaiActivity extends AppCompatActivity implements View.OnTouchListener {


    LinearLayout touchView;


    //la 30.629824,long 114.21115 end ll
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map_nvai);
        touchView = (LinearLayout) findViewById(R.id.touch_view_test);
        touchView.setOnTouchListener(this);
    }



    public void gas(View view) {
        //详情
        ActivitySwitchAround.toWashCarDetail(TestMapNvaiActivity.this, "");

//        ActivitySwitchAround.toAroundMap(MapMarkerType.GasStationType,TestMapNvaiActivity.this);
    }

    public void park(View view) {
        //列表

        Intent intent = new Intent(TestMapNvaiActivity.this, MaintenanceCommentListActivity.class);
        startActivity(intent);
//        ActivitySwitchAround.toAroundMap(MapMarkerType.ParkType,TestMapNvaiActivity.this);
    }

    public void traffic(View view) {
//        ActivitySwitchAround.toAroundMap(MapMarkerType.TrafficType,TestMapNvaiActivity.this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DebugLog.i("rect_draw", "TestMapNvaiActivity onTouchEvent getY: " + event.getY());
        DebugLog.i("rect_draw", "TestMapNvaiActivity onTouchEvent getX: " + event.getX());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        DebugLog.i("rect_draw", "TestMapNvaiActivity onTouch getY: " + event.getY());
        DebugLog.i("rect_draw", "TestMapNvaiActivity onTouch getX: " + event.getX());
        return false;
    }
}
