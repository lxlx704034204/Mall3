package com.hxqc.mall.activity.auto;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.google.gson.reflect.TypeToken;
import com.hxqc.adapter.ObjectAdapter;
import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.adapter.ParameterExpandableAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.AutoParmeterGroup;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-013
 * FIXME
 * Todo 参数列表
 */
public class ParameterActivity extends AppBackActivity implements
        AdapterView.OnItemClickListener, ExpandableListView.OnGroupClickListener {
    NewAutoClient newAutoClient = new NewAutoClient();
    public PinnedHeaderExpandableListView mExpandListView;
    String itemID;
    ParameterExpandableAdapter mParameterExpandableAdapter;
    PopupWindow mPopupWindow;
    GridView mPopupWindowView;
    AnimationSet mAnimationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        itemID = getIntent().getBundleExtra(ActivitySwitcher.KEY_DATA).getString("data");

        mExpandListView = (PinnedHeaderExpandableListView) findViewById(R.id.parameter_expand_listview);
        newAutoClient.itemParameter(getIntent().getBundleExtra(ActivitySwitcher.KEY_DATA).getBoolean("isExtID", false),
                itemID, new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {

                        ArrayList<AutoParmeterGroup> autoParameterGroups = JSONUtils.fromJson(response, new TypeToken<
                                ArrayList<AutoParmeterGroup>>() {
                        });
                        if (autoParameterGroups == null) {
                            ToastHelper.showRedToast(ParameterActivity.this, "车辆参数信息有待完善");
                            return;
                        }
                        mParameterExpandableAdapter = new ParameterExpandableAdapter(ParameterActivity.this,
                                autoParameterGroups);
                        mExpandListView.setAdapter(mParameterExpandableAdapter);
                        OtherUtil.openAllChild(mParameterExpandableAdapter, mExpandListView);
                        mExpandListView.setOnHeaderUpdateListener(mParameterExpandableAdapter);
                        mExpandListView.setOnGroupClickListener(ParameterActivity.this);

                        View view = LayoutInflater.from(ParameterActivity.this).inflate(R.layout.layout_parameter_factor, null);
                        mPopupWindowView = (GridView) view.findViewById(R.id.parameter_factor_gridview);
                        ObjectAdapter<AutoParmeterGroup> mParameterFactorGroupObjectAdapter = new ObjectAdapter<>(
                                ParameterActivity.this, autoParameterGroups, AutoParmeterGroup.class, R.layout
                                .item_parameter_factor,
                                new String[]{"groupLabel"}, new int[]{R.id.parameter_factor_label
                        }
                        );
                        mPopupWindowView.setAdapter(mParameterFactorGroupObjectAdapter);
                        mPopupWindowView.startAnimation(getAnimationSet());
                        mPopupWindow = new PopupWindow(view);
                        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                        mPopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                        mPopupWindow.setFocusable(true);
                        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                        mPopupWindowView.setOnItemClickListener(ParameterActivity.this);
                    }
                });


    }

    private AnimationSet getAnimationSet() {
        if (mAnimationSet != null) return mAnimationSet;
        mAnimationSet = new AnimationSet(false);
//scale------------------------------------------------------------------------------------
        ScaleAnimation scale = new ScaleAnimation(
                1, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0);
        scale.setDuration(200);
        scale.setFillAfter(true);
//alpha------------------------------------------------------------------------------------
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(200);
        alpha.setFillAfter(true);
        mAnimationSet.addAnimation(scale);
        mAnimationSet.addAnimation(alpha);
        return mAnimationSet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parameter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_classify) {
            if (mPopupWindow != null)
                mPopupWindow.showAsDropDown(findViewById(R.id.layout));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mExpandListView.expandGroup(position);
        mExpandListView.setSelectedGroup(position);
        mPopupWindow.dismiss();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//        mExpandListView.expandGroup(groupPosition);
        mExpandListView.setSelectedGroup(groupPosition);
        return false;
    }
}
