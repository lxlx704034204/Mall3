package com.hxqc.mall.drivingexam.ui.helpyoupass;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学车帮你过
 * Created by zhaofan on 2016/8/15.
 */
@Deprecated
public class HelpYouPassActivity_old extends initActivity implements AdapterView.OnItemClickListener {
    private GridView gv1, gv2;
    private CustomToolBar mToolBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helppass;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        gv1 = (GridView) findViewById(R.id.gv1);
        gv2 = (GridView) findViewById(R.id.gv2);
    }

    @Override
    public void init() {
        mToolBar.setTitle("学车帮你过");
        String[] str1 = {"安全带", "点火开关", "方向盘", "离合器", "加速踏板", "制动踏板", "驻车制动", "座椅调整", "后视镜", "经验技巧"};
        int[] img1 = {R.drawable.light, R.drawable.dianhuo, R.drawable.fangxiangp, R.drawable.lihe,
                R.drawable.jiasu, R.drawable.zhidong, R.drawable.p, R.drawable.zuoyi, R.drawable.houshi,
                R.drawable.jingyan1};

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < str1.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("item", str1[i]);
            map.put("img", img1[i]);
            list.add(map);
        }

        SimpleAdapter adapter1 = new SimpleAdapter(this, list,
                R.layout.item_helppass, new String[]{"item", "img"},
                new int[]{R.id.tv1, R.id.img});
        gv1.setAdapter(adapter1);
        gv1.setOnItemClickListener(this);

        String[] str2 = {"车距判断", "档位操作", "灯光", "直行", "经验技巧"};
        int[] img2 = {R.drawable.cheju, R.drawable.dangwei, R.drawable.dengguang, R.drawable.zhixing,
                R.drawable.jingyan};

        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < str2.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("item", str2[i]);
            map.put("img", img2[i]);
            list2.add(map);
        }

        SimpleAdapter adapter2 = new SimpleAdapter(this, list2,
                R.layout.item_helppass, new String[]{"item", "img"},
                new int[]{R.id.tv1, R.id.img});
        gv2.setAdapter(adapter2);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DebugLog.i("", parent.getId() + " " + R.id.gv1);

        ActivitySwitcherExam.toKemu2Html(this, position);
    }
}
