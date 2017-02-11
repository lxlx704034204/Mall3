package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;

/**
 * Author:李烽
 * Date:2016-02-25
 * FIXME
 * Todo 保养列表的item用到的
 */
public class MaintenanceDetailView extends LinearLayout implements Checkable {
    private boolean checked = false;
    private ImageView checkMark;
    private TextView maintenanceTime, maintenanceCount, drivenDistance, maintenanceContent, maintenanceShop, to_order_detail;
    private LinearLayout rootLayout;

    public MaintenanceDetailView(Context context) {
        this(context, null);
    }

    public MaintenanceDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaintenanceDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_maintenance_record_detail, this);
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        checkMark = (ImageView) findViewById(R.id.select_mark);
        maintenanceTime = (TextView) findViewById(R.id.maintenance_time);
        maintenanceCount = (TextView) findViewById(R.id.maintenance_count);
        drivenDistance = (TextView) findViewById(R.id.driven_distance);
        maintenanceContent = (TextView) findViewById(R.id.maintenance_content);
        maintenanceShop = (TextView) findViewById(R.id.maintenance_shop);
        to_order_detail = (TextView) findViewById(R.id.to_order_detail);
        to_order_detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //去看工单详情
                ActivitySwitchBase.toH5Activity(getContext(), "工单详情", "http://m.baidu.com");
            }
        });
        checkStatus();
    }

    private void checkStatus() {
        if (checked) {
            rootLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            checkMark.setImageResource(R.drawable.bg_red_circle);
        } else {
            rootLayout.setBackgroundColor(Color.parseColor("#fafafa"));
            checkMark.setImageResource(R.drawable.bg_gray_circle);
        }
    }


    public void addData(String time, String count, String distance, String content, String shop) {
        maintenanceTime.setText(time);
        maintenanceCount.setText(Html.fromHtml("<font color='#de000000'>" + getContext().getString(R.string.maintenance_count)
                + "</font><font color='#ff7043'>" + OtherUtil.stringToMoney(count) + "</font>"));
//        maintenanceCount.setText(getContext().getString(R.string.maintenance_count) + OtherUtil.stringToMoney(count));
        maintenanceContent.setText(getContext().getString(R.string.maintenance_content) + content);
        maintenanceShop.setText(shop);
        drivenDistance.setText(getContext().getString(R.string.driven_distance) + distance + "公里");
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        checkStatus();
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }
}
