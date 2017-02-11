package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;

/**
 * Author:李烽
 * Date:2016-02-17
 * FIXME
 * Todo 保养套餐item
 */
public class MaintenanceProgramItem extends LinearLayout implements View.OnClickListener {
    private MaintenancePackage maintenancePackage = null;
    private ImageView programIcon;//套餐图标
    private TextView programContent,//套餐内容
            programName, //套餐名称
            programPrice, //套餐价格
            programCost;//套餐原价
    private OnSeeDetailClickListener onSeeDetailClickListener;

    public MaintenanceProgramItem(Context context) {
        this(context, null);
    }

    public MaintenanceProgramItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaintenanceProgramItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_maintenance_program_content, this);
        programContent = (TextView) findViewById(R.id.maintenance_program_content);
        programName = (TextView) findViewById(R.id.maintenance_program_name);
        programPrice = (TextView) findViewById(R.id.program_price);
        programCost = (TextView) findViewById(R.id.program_cost);
        programCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        programIcon = (ImageView) findViewById(R.id.maintenance_program_icon);
        findViewById(R.id.maintenance_program_detail_btn).setOnClickListener(this);
        addData(maintenancePackage);
    }

    public void addData(MaintenancePackage maintenancePackage) {
        this.maintenancePackage = maintenancePackage;
        if (this.maintenancePackage == null)
            return;
        /*添加数据*/
        if (!TextUtils.isEmpty(this.maintenancePackage.img))
            ImageUtil.setImageSquare(getContext(), programIcon, this.maintenancePackage.img);
//        Picasso.with(getContext()).load(this.maintenancePackage.img)
//                .placeholder(R.drawable.pic_normal)
//                .error(R.drawable.pic_normal)
//                .error(R.drawable.pic_icon).into(programIcon);
        if (!TextUtils.isEmpty(this.maintenancePackage.itemsNames))
            programContent.setText("保养项目：" + this.maintenancePackage.itemsNames);
        if (!TextUtils.isEmpty(this.maintenancePackage.name))
            programName.setText(this.maintenancePackage.name);
        programPrice.setText(OtherUtil.stringToMoney(this.maintenancePackage.discount));
        programCost.setText(OtherUtil.stringToMoney(this.maintenancePackage.amount));
    }

    public void setOnSeeDetailClickListener(OnSeeDetailClickListener onSeeDetailClickListener) {
        this.onSeeDetailClickListener = onSeeDetailClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.maintenance_program_detail_btn) {
            //查看套餐详情
            if (onSeeDetailClickListener != null)
                onSeeDetailClickListener.onSeeDetailClick(this);
        }
    }

    public interface OnSeeDetailClickListener {
        void onSeeDetailClick(View view);
    }


}
