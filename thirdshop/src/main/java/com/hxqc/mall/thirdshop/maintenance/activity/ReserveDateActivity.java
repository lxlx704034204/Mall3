package com.hxqc.mall.thirdshop.maintenance.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.views.ReserveDateView;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 */
@Deprecated
public class ReserveDateActivity extends NoBackActivity implements View.OnClickListener {

    private RecyclerView mReserveDateListView;
    private RecyclerView mReserveTimeList;
    private ReserveDateView mReserveDateListsView;
    private Button mReserveDateBtnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_date);

        initView();

//        initEvent();
    }

  /*  private void initEvent() {
        ReserveDateAdapter reserveDateAdapter = new ReserveDateAdapter(ReserveDateActivity.this);
        mReserveDateListView.setHasFixedSize(true);
        mReserveDateListView.setLayoutManager(new LinearLayoutManager(ReserveDateActivity.this));
        mReserveDateListView.setAdapter(reserveDateAdapter);

        ReserveTimeAdapter reserveTimeAdapter = new ReserveTimeAdapter(ReserveDateActivity.this);
        mReserveTimeList.setHasFixedSize(true);
        mReserveTimeList.setLayoutManager(new LinearLayoutManager(ReserveDateActivity.this));
        mReserveTimeList.setAdapter(reserveTimeAdapter);

//        mReserveDateBtnView.setOnClickListener(this);

    }*/

    private void initView() {
        mReserveDateListsView = (ReserveDateView) findViewById(R.id.reserve_date_lists);
//        mReserveDateBtnView = (Button) findViewById(R.id.reserve_date_btn);
        mReserveDateListView = mReserveDateListsView.getLeftList();
        mReserveTimeList = mReserveDateListsView.getRightList();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
       /* if (i == R.id.reserve_date_btn) {

        }*/
    }
}
