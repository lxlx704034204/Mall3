package com.hxqc.carcompare.ui.discuss;

import android.widget.ListView;

import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.thirdshop.model.newcar.UserDiscussDetail;
import com.hxqc.mall.thirdshop.model.newcar.UserGradeComment;
import com.hxqc.mall.thirdshop.views.adpter.UserGradeAdapter2;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 用户评论
 * Created by zhaofan on 2016/11/2.
 */
public class DiscussDetailActivity extends initActivity {
    private UserDiscussDetail data;
    private ListView mListView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_discuss_detail;
    }


    /**
     * From {@link com.hxqc.carcompare.ui.compare.CarCompareDetailActivity#ItemClickCallBack }
     */
    @Subscribe(sticky = true)
    public void getData(UserDiscussDetail data) {
        this.data = data;
    }

    @Override
    public void bindView() {
        mListView = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void init() {
        mEventBus.register(this);
       /* List<UserDiscussDetail> mList = new ArrayList<>();
        mList.add(data);*/

        ArrayList<UserGradeComment> mList = new ArrayList<>();
        UserGradeComment newData = new UserGradeComment(data.userInfo, data.content, data.time, data.images, data.grade);
        mList.add(newData);
        //评论列表
        UserGradeAdapter2 mUserGradeAdapter = new UserGradeAdapter2(this, false);
        mListView.setAdapter(mUserGradeAdapter);
        mUserGradeAdapter.setData(mList, false);
    }
}
