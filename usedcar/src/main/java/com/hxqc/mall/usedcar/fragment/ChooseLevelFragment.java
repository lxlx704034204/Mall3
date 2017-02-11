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
import com.hxqc.mall.usedcar.model.CarLevel;
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
public class ChooseLevelFragment extends FunctionFragment {
    ChooseLevelListener mListener;
    ArrayList<CarLevel> mCarLevels;
    QuickAdapter<CarLevel> mCarLevelAdapter;
    ListView mListView;
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
        mTitleView.setText("车辆级别");
        showList();
    }

    private void showList() {
        mCarLevelAdapter = new QuickAdapter<CarLevel>(getActivity(), R.layout.item_choose, mCarLevels) {
            @Override
            protected void convert(BaseAdapterHelper helper, CarLevel item) {
                helper.setText(R.id.name, item.level_name);
            }
        };
        mListView.setAdapter(mCarLevelAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.completeChooseLevel(mCarLevels.get(position).level_name);
            }
        });
    }

    @Override
    public String fragmentDescription() {
        return "二手车选择级别";
    }

    public void initCarLevel(ArrayList<CarLevel> carLevels) {
        mCarLevels = carLevels;
    }

    public void setListener(ChooseLevelListener mListener) {
        this.mListener = mListener;
    }

    //回调
    public interface ChooseLevelListener {
        void completeChooseLevel(String level);
    }
}
