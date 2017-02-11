package com.hxqc.pay;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.pay.adapter.EventCardAdapterHolder;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    RecyclerView recyclerView;
    EventCardAdapterHolder eventCardAdapterHolder;
    private ArrayList< String > mDatas = new ArrayList<>();

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatas.add("11");
        mDatas.add("22");
        mDatas.add("22");
        mDatas.add("22");

        eventCardAdapterHolder = new EventCardAdapterHolder(mDatas);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_card_view);
        recyclerView.setAdapter(eventCardAdapterHolder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
