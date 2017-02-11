package com.hxqc.newenergy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.newenergy.activity.Ev_ModelAndSubsidyActivity;
import com.hxqc.newenergy.activity.Ev_NewEnergyCarTemaiActivity;
import com.hxqc.newenergy.activity.Ev_NewsWebActivity;
import com.hxqc.newenergy.util.ActivitySwitcherEV;

import hxqc.mall.R;

/**
 * 新能源菜单自定义View
 * Created by 何玉
 * on 2016/3/10.
 */
public class Menu_view extends FrameLayout implements View.OnClickListener {
    public Menu_view(Context context) {
        super(context);
        inIt();

    }

    public Menu_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public Menu_view(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Menu_view(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inIt();
    }


    public void inIt() {
        inflate(getContext(), R.layout.activity_ev_newwenergy_menu, this);
        findViewById(R.id.newwenergycardeals_button).setOnClickListener(this);
        findViewById(R.id.newwenergycar4s_button).setOnClickListener(this);
        findViewById(R.id.newwenergycartemai_button).setOnClickListener(this);
        findViewById(R.id.modelsandsubsidies_button).setOnClickListener(this);
        findViewById(R.id.encyclopedicknowledge_button).setOnClickListener(this);
        findViewById(R.id.home_more_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.newwenergycardeals_button:
                //销售
                ActivitySwitcherEV.toBrand(getContext(), null);
                break;
            case R.id.newwenergycar4s_button:
                //4S店
                break;
            case R.id.newwenergycartemai_button:
                //特卖
                if (getContext() instanceof Ev_NewEnergyCarTemaiActivity) {
                } else {
                    ActivitySwitcherEV.toTemaiActivity(getContext());
                }
                break;
            case R.id.modelsandsubsidies_button:
                //补贴
                if (getContext() instanceof Ev_ModelAndSubsidyActivity) {
                } else {
                    ActivitySwitcherEV.toModelAndSubsidyActivity(getContext());
                }
                break;
            case R.id.encyclopedicknowledge_button:
                //百科
                if (getContext() instanceof Ev_NewsWebActivity) {
                } else {
                    ActivitySwitcherEV.toWikiActivity(getContext());
                }
                break;
            case R.id.home_more_button:
                ToastHelper.showRedToast(getContext(),"更多内容,敬请期待");
                break;
        }

    }


}
