package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-08-02
 * FIXME
 * Todo 店铺保养首页@MaintenanceHomeFragment.class
 * 中的平台推荐保养项目
 */
public class PlatformRecommendedList extends LinearLayout {

    private TextView title;

    private TextView totalCountText;

    private LinearLayout programList;

    private Button detailBtn;

    public ShopInfo shopInfo;

    private LinearLayout noDataLayout, listLayout;
    private TextView noDataLabel;
    private ImageView noDataIcon;
    private Button nodataBtn;

    private Context context;

    public PlatformRecommendedList(Context context) {
        this(context, null);
    }

    public PlatformRecommendedList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlatformRecommendedList(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_platform_recommended_list, this);
        this.context = context;
        noDataLabel = (TextView) findViewById(R.id.no_data_lable);
        noDataIcon = (ImageView) findViewById(R.id.icon_no_m_p);
        noDataLayout = (LinearLayout) findViewById(R.id.no_data_layout);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        nodataBtn = (Button) findViewById(R.id.detail_btn2);
        nodataBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //去保养
                ActivitySwitcherMaintenance.toFourSShopMaintanence(context, shopInfo.toNMS());
            }
        });
        title = (TextView) findViewById(R.id.title);
        totalCountText = (TextView) findViewById(R.id.total_text);
        programList = (LinearLayout) findViewById(R.id.program_list);
        detailBtn = (Button) findViewById(R.id.detail_btn);
        detailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //去保养
                ActivitySwitcherMaintenance.toFourSShopMaintanence(context, shopInfo.toNMS());
            }
        });
    }

    //添加数据

    public void addData(ArrayList<MaintenanceItemGroup> data, int optionSize) {
        if (optionSize == 0) {
            noDataLabel.setText("暂无保养项目");
            noDataLayout.setVisibility(VISIBLE);
            listLayout.setVisibility(GONE);
            nodataBtn.setVisibility(GONE);
        } else {
            if (data == null || data.size() == 0) {
                noDataLayout.setVisibility(VISIBLE);
                listLayout.setVisibility(GONE);
                nodataBtn.setVisibility(GONE);
                noDataLabel.setText("暂无推荐保养项目");
            } else {
                listLayout.setVisibility(VISIBLE);
                noDataLayout.setVisibility(GONE);
                title.setText(String.format("平台为您推荐%d项保养项目", data.size()));
                String s = data.size() + "";
                int length = s.length();
                totalCountText.setText(OtherUtil.toCallText(String.format("共%d个项目", data.size()), 1, length + 1, "#ff0000"));
                programList.removeAllViews();
                for (int i = 0 ; i < data.size() ; i++){
                    LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_first_layer_v5,programList);
                    FourSShopMaintenanceFirstChildV5 firstChild = (FourSShopMaintenanceFirstChildV5) programList.getChildAt(i);
                    firstChild.initDate(data.get(i),"2",i);
                }
            }

//                programList.setAdapter(new CommonAdapter<MaintenanceItemN>(getContext(),
//                        data, R.layout.item_platform_recommended_list) {
//                    @Override
//                    public void convert(ViewHolder helper, MaintenanceItemN item, int position) {
//                        helper.setText(R.id.program_title, item.name);
//                        helper.setText(R.id.index, String.format("%d.", position + 1));
//                        try {
//                            double workCost = Double.parseDouble(item.workCost);
//                            if (workCost == 0.0) {
//                                helper.setText(R.id.program_partners, "免费");
//                            } else {
//                                helper.setText(R.id.program_partners, OtherUtil.amountFormat(item.workCost, true));
//                            }
//                            double amount = Double.parseDouble(item.amount);
//                            if (amount == 0.0) {
//                                helper.getView(R.id.program_materials).setVisibility(GONE);
//                                helper.getView(R.id.program_materials_title).setVisibility(GONE);
//                            } else
//                                helper.setText(R.id.program_materials, OtherUtil.amountFormat(item.amount, true));
//                        } catch (RuntimeException e) {
//                            e.printStackTrace();
//                            helper.setText(R.id.program_partners, OtherUtil.amountFormat(item.workCost, true));
//                            helper.setText(R.id.program_materials, OtherUtil.amountFormat(item.amount, true));
//                        }
////                        helper.setText(R.id.program_partners, OtherUtil.amountFormat(item.workCost, true));
////                        helper.setText(R.id.program_materials, OtherUtil.amountFormat(item.amount, true));
////                    helper.setText(R.id.program_detail, item.summary);
//                        ArrayList<MaintenanceGoods> goods = item.goods;
//                        String goodsName = "";
//                        if (goods != null && goods.size() > 0) {
//                            goodsName = goods.get(0).name;
//                        }
//                        helper.setText(R.id.program_detail, goodsName);
//                    }
//                });
//            }
        }

    }
}
