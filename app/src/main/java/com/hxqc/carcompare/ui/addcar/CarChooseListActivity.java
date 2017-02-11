package com.hxqc.carcompare.ui.addcar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hxqc.carcompare.ui.compare.CarCompareDetailActivity;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hxqc.mall.R;

/**
 * 添加车辆列表
 * Created by zhaofan on 2016/12/8.
 */
public class CarChooseListActivity extends initActivity implements AdapterView.OnItemClickListener {
    public static final String FINISH = "CarChooseListActivity_finish";
    private CustomToolBar mToolbar;
    private ListView lv;
    private List<ChooseCarModel> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_car_choose_list;
    }

    @Subscribe(sticky = true)
    public void getList(Event msg) {
        if (msg.what.equals(CarCompareDetailActivity.TAG)) {
            this.list = (List<ChooseCarModel>) msg.obj;
        }
    }

    @Override
    public void bindView() {
        mToolbar = (CustomToolBar) findViewById(R.id.topbar);
        lv = (ListView) findViewById(R.id.listview);

    }

    @Override
    public void init() {
        mEventBus.register(this);
        List<Map<String, Object>> mList = new ArrayList<>();
        for (ChooseCarModel entity : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("item", entity.getModelName());
            mList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, mList,
                R.layout.item_filter_carmodel, new String[]{"item"}, new int[]{R.id.text1});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        mToolbar.setRightTitle("添加", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitcherThirdPartShop.toFilterAllShopBrand(mContext, true);
            }
        });
    }


    @Subscribe
    public void onFinish(String msg) {
        if (msg.equals(FINISH)) {
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        ChooseCarModel data = list.get(pos);
        final ModelInfo entity = new ModelInfo(data.brand, data.modelName, data.extId, "");
        EventBus.getDefault().post(entity);
        finish();
    }
}
