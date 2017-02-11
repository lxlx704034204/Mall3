package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ReserveDateAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.ReserveTimeAdapter;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * FIXME
 * Todo 预约时间列表
 */
public class ReserveDateView extends LinearLayout {

    private RecyclerView leftList;
    private RecyclerView rightList;
    private int leftListWidth;
    private int leftListHeight;
    private float leftListWeight;
    private int leftListBackground;

    private int rightListWidth;
    private int rightListHeight;
    private float rightListWeight;
    private int rightListBackground;

    private ReserveDateAdapter reserveDateAdapter;
    private ReserveTimeAdapter reserveTimeAdapter;

    public ReserveDateView(Context context) {
        this(context, null);
    }

    public ReserveDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReserveDateView);
        leftListWidth = typedArray.getLayoutDimension(R.styleable.ReserveDateView_reserve_date_left_list_width, "reserve_date_left_list_width");
        leftListHeight = typedArray.getLayoutDimension(R.styleable.ReserveDateView_reserve_date_left_list_height, "reserve_date_left_list_height");
        leftListWeight = typedArray.getFloat(R.styleable.ReserveDateView_reserve_date_left_list_weight, 0f);
        leftListBackground = typedArray.getResourceId(R.styleable.ReserveDateView_reserve_date_left_list_background, R.color.transparent);

        rightListWidth = typedArray.getLayoutDimension(R.styleable.ReserveDateView_reserve_date_right_list_width, "reserve_date_right_list_width");
        rightListHeight = typedArray.getLayoutDimension(R.styleable.ReserveDateView_reserve_date_right_list_height, "reserve_date_right_list_height");
        rightListWeight = typedArray.getFloat(R.styleable.ReserveDateView_reserve_date_right_list_weight, 0f);
        rightListBackground = typedArray.getResourceId(R.styleable.ReserveDateView_reserve_date_right_list_background, R.color.transparent);

        typedArray.recycle();

        leftList = new RecyclerView(context);
        rightList = new RecyclerView(context);
//        View view = new View(context);

        leftList.setBackgroundResource(leftListBackground);
        rightList.setBackgroundResource(rightListBackground);
//        view.setBackgroundColor(getResources().getColor(R.color.divider));

        LayoutParams leftLayoutParams = new LayoutParams(leftListWidth, leftListHeight, leftListWeight);

        addView(leftList, leftLayoutParams);

//        LayoutParams viewParams = new LayoutParams(1, LayoutParams.MATCH_PARENT);
//        addView(view,viewParams);

        LayoutParams rightLayoutParams = new LayoutParams(rightListWidth, rightListHeight, rightListWeight);

        addView(rightList, rightLayoutParams);

    }

    public RecyclerView getLeftList() {
        return leftList;
    }

    public void setLeftList(RecyclerView leftList) {
        this.leftList = leftList;
    }

    public RecyclerView getRightList() {
        return rightList;
    }

    public void setRightList(RecyclerView rightList) {
        this.rightList = rightList;
    }

   /* public void setAdapter(Context context) {
        reserveDateAdapter = new ReserveDateAdapter(getContext());
        leftList.setHasFixedSize(true);
        leftList.setLayoutManager(new LinearLayoutManager(getContext()));
        leftList.setAdapter(reserveDateAdapter);
        leftList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        reserveTimeAdapter = new ReserveTimeAdapter(getContext());
        rightList.setHasFixedSize(true);
        rightList.setLayoutManager(new LinearLayoutManager(getContext()));
        rightList.setAdapter(reserveTimeAdapter);
    }*/


}
