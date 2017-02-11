package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-16
 * FIXME
 * Todo 保养方案模块
 */
public class NewRecommendMaintenanceProgramView extends LinearLayout implements View.OnClickListener {
    public OnSeeDetailClickListener onSeeDetailClickListener;
//    private MaintenancePackage data;

    private ArrayList<MaintenanceItemN> maintenanceItems;
    private String maintenanceManual;

    //    private RecyclerView program_list;
    private ListViewNoSlide listView;
    private TextView total_text;
    private Button detail_btn;

    public NewRecommendMaintenanceProgramView(Context context) {
        this(context, null);
    }

    public NewRecommendMaintenanceProgramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewRecommendMaintenanceProgramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_mainteenance_program_new, this);

//        program_list = (RecyclerView) findViewById(R.id.program_list);
        listView = (ListViewNoSlide) findViewById(R.id.program_list_1);
        total_text = (TextView) findViewById(R.id.total_text);
        detail_btn = (Button) findViewById(R.id.detail_btn);

        detail_btn.setOnClickListener(this);

        LinearLayout btn = (LinearLayout) findViewById(R.id.layout_maintenance_program_title)
                .findViewById(R.id.maintenance_manual);
//        findViewById(R.id.maintenance_program_detail_btn).setOnClickListener(this);
        findViewById(R.id.layout_maintenance_program_title).findViewById(R.id.maintenance_manual)
                .setOnClickListener(this);
//        data = new MaintenancePackage();
//        data.amount = 0;
//        data.itemsNames = "";


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

    public void addData(ArrayList<MaintenanceItemN> maintenanceItems, String maintenanceManual) {
        if (maintenanceItems == null || maintenanceItems.size() == 0) {
            setVisibility(GONE);
            return;
        } else setVisibility(VISIBLE);
        this.maintenanceManual = maintenanceManual;

        total_text.setText("共" + maintenanceItems.size() + "个项目");

//        program_list.setLayoutManager(new LinearLayoutManager(getContext()));
//        program_list.setAdapter(new BaseRecyclerAdapter<MaintenanceItemN>(getContext(), maintenanceItems) {
//            @Override
//            protected void bindData(RecyclerViewHolder holder, int position, final MaintenanceItemN maintenanceItemN) {
//                holder.setText(R.id.item_name, (position + 1) + "." + maintenanceItemN.name);
//                holder.getImageView(R.id.question).setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        toSeeDialog(maintenanceItemN);
//                    }
//                });
//            }
//
//            @Override
//            protected int getItemLayout(int viewType) {
//                return R.layout.item_recommend_maintenance_program;
//            }
//
//        });

        listView.setAdapter(new CommonAdapter<MaintenanceItemN>(getContext()
                , maintenanceItems, R.layout.item_recommend_maintenance_program) {
            @Override
            public void convert(ViewHolder helper, final MaintenanceItemN item,int position) {
                helper.setText(R.id.item_name, (position + 1) + "." + item.name);
                helper.getView(R.id.question).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toSeeDialog(item);
                    }
                });
            }
        });

    }

    private void toSeeDialog(final MaintenanceItemN maintenanceItemN) {
        //点击之后的操作
        new MaintenanceClient().itemIntroduce(maintenanceItemN.itemID, new LoadingAnimResponseHandler(getContext()) {
            @Override
            public void onSuccess(String response) {
                GoodsIntroduce goodsIntroduce = JSONUtils.fromJson(response, new TypeToken<GoodsIntroduce>() {
                });
                ItemIntroduceDialog dialog = new ItemIntroduceDialog(getContext(), R.style.FullWidthDialog, goodsIntroduce, maintenanceItemN.name);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                dialog.show();
            }
        });
    }

    public void setOnSeeDetailClickListener(OnSeeDetailClickListener onSeeDetailClickListener) {
        this.onSeeDetailClickListener = onSeeDetailClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.maintenance_manual) {
            //保养手册
            ActivitySwitchBase.toH5Activity(getContext(), getContext().getString(R.string.maintenance_book), maintenanceManual);
        } else if (i == R.id.detail_btn) {
            if (onSeeDetailClickListener != null)
                onSeeDetailClickListener.onSeeDetailClick(this);
        }
    }

    public interface OnSeeDetailClickListener {
        void onSeeDetailClick(View view);
    }
}
