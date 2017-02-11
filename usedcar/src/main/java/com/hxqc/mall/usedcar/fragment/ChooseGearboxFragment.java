package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.CarGearbox;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:选择车型
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseGearboxFragment extends FunctionFragment {
    ListView mListView;
    ChooseGearboxListener mListener;
    ArrayList<CarGearbox> mCarGearboxes;
    QuickAdapter<CarGearbox> mCarLevelAdapter;
    TextView mTitleView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mTitleView = (TextView) view.findViewById(R.id.title);
        mTitleView.setText("变速箱");
        showList();
    }

    private void showList() {
        mCarLevelAdapter = new QuickAdapter<CarGearbox>(getActivity(), R.layout.item_choose, mCarGearboxes) {
            @Override
            protected void convert(BaseAdapterHelper helper, CarGearbox item) {
                helper.setText(R.id.name, item.gearbox_name);
            }
        };
        mListView.setAdapter(mCarLevelAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.completeChooseGearbox(mCarGearboxes.get(position).gearbox_name);
            }
        });

    }


    @Override
    public String fragmentDescription() {
        return "二手车选择变速箱";
    }

    public void initCarGearbox(ArrayList<CarGearbox> carGearboxes) {
        mCarGearboxes = carGearboxes;
    }

    public void setListener(ChooseGearboxListener mListener) {
        this.mListener = mListener;
    }

    //回调
    public interface ChooseGearboxListener {
        void completeChooseGearbox(String gearbox);
    }
}
