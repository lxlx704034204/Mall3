package com.hxqc.mall.drivingexam.ui.helpyoupass.drivingskill;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.core.views.CustomToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学车技巧
 * Created by zhaofan on 2016/8/24.
 */
public class DrivingExperienceActivity extends initActivity implements AdapterView.OnItemClickListener {
    private GridView gv;
    private CustomToolBar mToolBar;
    String[] str1;
    int[] img1;
    private int kemu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_driving_experience;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        gv = (GridView) findViewById(R.id.gv);
    }

    @Override
    public void init() {

        kemu = getIntent().getIntExtra("kemu", 2);
        if (kemu == 2) {
            mToolBar.setTitle("科目二 学车技巧");
            str1 = new String[]{"安全带", "点火开关", "方向盘", "离合器", "加速踏板", "制动踏板", "驻车制动", "座椅调整", "后视镜", "经验技巧"};
            img1 = new int[]{R.drawable.light, R.drawable.dianhuo, R.drawable.fangxiangp, R.drawable.lihe,
                    R.drawable.jiasu, R.drawable.zhidong, R.drawable.p, R.drawable.zuoyi, R.drawable.houshi,
                    R.drawable.jingyan1};
        } else {
            mToolBar.setTitle("科目三 学车技巧");
            str1 = new String[]{"车距判断", "档位操作", "灯光", "直行", "经验技巧", ""};
            img1 = new int[]{R.drawable.cheju, R.drawable.dangwei, R.drawable.dengguang, R.drawable.zhixing,
                    R.drawable.jingyan, 0};
        }


        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < str1.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("item", str1[i]);
            map.put("img", img1[i]);
            list.add(map);
        }

        SimpleAdapter adapter1 = new SimpleAdapter(this, list,
                R.layout.item_driving_skill, new String[]{"item", "img"},
                new int[]{R.id.tv1, R.id.img});
        gv.setAdapter(adapter1);
        gv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (kemu == 3 && position == str1.length - 1) {
            return;
        }
        if (kemu == 2) {
            ActivitySwitcherExam.toKemu2Html(this, position);
        }
        else if (kemu == 3){
            ActivitySwitcherExam.toKemu3Html(this, position);
        }
    }
}
