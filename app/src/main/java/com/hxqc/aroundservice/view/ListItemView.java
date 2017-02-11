package com.hxqc.aroundservice.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 06 - 14
 * FIXME
 * Todo
 */
public class ListItemView extends LinearLayout {

    private View cutOffRuleView;
    private int ruleHeight;
    private boolean ruleVisiable;

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemView);
        ruleVisiable = typedArray.getBoolean(R.styleable.ListItemView_cut_off_rule_visiable, false);
        ruleHeight = typedArray.getInteger(R.styleable.ListItemView_cut_off_rule_height, 0);
        typedArray.recycle();

        cutOffRuleView = new View(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ruleHeight);
//        layoutParams.height = ruleHeight;
        cutOffRuleView.setLayoutParams(layoutParams);
        cutOffRuleView.setBackgroundColor(getResources().getColor(R.color.bg_gray));
        if (ruleVisiable) {
            cutOffRuleView.setVisibility(VISIBLE);
        } else {
            cutOffRuleView.setVisibility(GONE);
        }
        addView(cutOffRuleView);
    }

}
