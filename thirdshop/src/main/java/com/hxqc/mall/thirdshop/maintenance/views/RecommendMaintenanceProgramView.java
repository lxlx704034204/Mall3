package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;

/**
 * Author:李烽
 * Date:2016-02-16
 * FIXME
 * Todo 保养方案模块
 */
public class RecommendMaintenanceProgramView extends LinearLayout implements View.OnClickListener {
    public OnSeeDetailClickListener onSeeDetailClickListener;
    private MaintenancePackage data;
    private ImageView programIcon;//套餐图标
    private TextView programContent,//套餐内容
    //            programName, //套餐名称
    programPrice, //套餐价格
            programCost;//套餐原价
    private String maintenanceManual;

    public RecommendMaintenanceProgramView(Context context) {
        this(context, null);
    }

    public RecommendMaintenanceProgramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecommendMaintenanceProgramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_mainteenance_program, this);
        programContent = (TextView) findViewById(R.id.maintenance_program_content);
//        programName = (TextView) findViewById(R.id.maintenance_program_name);
        programPrice = (TextView) findViewById(R.id.program_price);
        programCost = (TextView) findViewById(R.id.program_cost);
        programCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        programIcon = (ImageView) findViewById(R.id.maintenance_program_icon);
        LinearLayout btn = (LinearLayout) findViewById(R.id.layout_maintenance_program_title).findViewById(R.id.maintenance_manual);
        findViewById(R.id.maintenance_program_detail_btn).setOnClickListener(this);
        findViewById(R.id.layout_maintenance_program_title).findViewById(R.id.maintenance_manual)
                .setOnClickListener(this);
        data = new MaintenancePackage();
        data.amount = 0;
        data.itemsNames = "";
//        addData(data);
        TextView title = (TextView) findViewById(R.id.layout_maintenance_program_title)
                .findViewById(R.id.program_title);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecommendMaintenanceProgramView);
        String titleStr = typedArray.getString(R.styleable.RecommendMaintenanceProgramView_program_title);
        boolean showBtn = typedArray.getBoolean(R.styleable.RecommendMaintenanceProgramView_show_button, false);
        if (!TextUtils.isEmpty(titleStr))
            title.setText(titleStr);
        btn.setVisibility(showBtn ? VISIBLE : GONE);
        typedArray.recycle();
    }

    public void addData(MaintenancePackage maintenancePackage,String maintenanceManual) {
        if (maintenancePackage == null || TextUtils.isEmpty(maintenancePackage.packageID)) {
            setVisibility(GONE);
            return;
        } else setVisibility(VISIBLE);
        this.maintenanceManual=maintenanceManual;
//        this.item.addData(item);
         /*添加数据*/
//        if (!TextUtils.isEmpty(img))
//            ImageUtil.setImage(getContext(), programIcon, img);
        if (!TextUtils.isEmpty(maintenancePackage.itemsNames))
            programContent.setText("保养项目：" + maintenancePackage.itemsNames);
//        if (!TextUtils.isEmpty(name))
//            programName.setText(name);
//        if (discount!=null)
        programPrice.setText(OtherUtil.stringToMoney(maintenancePackage.discount));
//        if (!TextUtils.isEmpty(amount))
        programCost.setText(OtherUtil.stringToMoney(maintenancePackage.amount));
        if (!TextUtils.isEmpty(maintenancePackage.img))
            ImageUtil.setImageSquare(getContext(), programIcon, maintenancePackage.img);
    }

    public void setOnSeeDetailClickListener(OnSeeDetailClickListener onSeeDetailClickListener) {
        this.onSeeDetailClickListener = onSeeDetailClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.maintenance_manual) {
            //保养手册
//            ActivitySwitcherMaintenance.toMaintenanceBook(getContext(), "");
            ActivitySwitchBase.toH5Activity(getContext(), getContext().getString(R.string.maintenance_book), maintenanceManual);
        } else if (i == R.id.maintenance_program_detail_btn) {
            if (onSeeDetailClickListener != null)
                onSeeDetailClickListener.onSeeDetailClick(this);
        }
    }

    public interface OnSeeDetailClickListener {
        void onSeeDetailClick(View view);
    }
}
