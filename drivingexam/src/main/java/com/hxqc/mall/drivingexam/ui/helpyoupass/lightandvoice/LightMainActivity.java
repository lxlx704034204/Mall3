package com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.views.CustomToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 科目3灯光语音
 * Created by zhaofan on 2016/8/22.
 */
public class LightMainActivity extends initActivity implements View.OnClickListener {
    public static final String CHANGE_PAGE = "change_page";
    private CustomToolBar mToolBar;
    private ViewPager viewPagers;
    private TextView[] tab = new TextView[2];

    @Override
    public int getLayoutId() {
        return R.layout.activity_kemu3main;
    }

    @Override
    public void bindView() {
        mToolBar= (CustomToolBar) findViewById(R.id.topbar);
        viewPagers = (ViewPager) findViewById(R.id.viewpager);
        tab[0] = (TextView) findViewById(R.id.light_tv);
        tab[1] = (TextView) findViewById(R.id.voice_tv);
    }

    @Override
    public void init() {
        mToolBar.setTitle("灯光语音");
        changeText(0);
        List<Fragment> totalFragment = new ArrayList<Fragment>();
        totalFragment.add(new LightFragment());
        totalFragment.add(new VoiceFragment());
        viewPagers.setAdapter(new FragmentAdapter(getSupportFragmentManager(), totalFragment));
        viewPagers.setCurrentItem(0);
        viewPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeText(position);
                mEventBus.post(new Event(position, CHANGE_PAGE));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeText(int pos) {
        for (int i = 0; i < tab.length; i++) {
            tab[i].setBackgroundResource(0);
            tab[i].setTextColor(Color.parseColor("#777777"));
            tab[i].setOnClickListener(this);
        }
        tab[pos].setBackgroundResource(R.drawable.bg_flag_red_bottom);
        tab[pos].setTextColor(getResources().getColor(R.color.font_red));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.light_tv) {
            viewPagers.setCurrentItem(0);
        } else if (id == R.id.voice_tv) {
            viewPagers.setCurrentItem(1);
        }
    }


    public class FragmentAdapter extends FragmentPagerAdapter {
        List<Fragment> list;

        public FragmentAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }
}
