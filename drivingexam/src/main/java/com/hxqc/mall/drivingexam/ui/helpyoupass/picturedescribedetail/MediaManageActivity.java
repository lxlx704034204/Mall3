package com.hxqc.mall.drivingexam.ui.helpyoupass.picturedescribedetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenu;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuCreator;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuItem;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuListView;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.utils.FileUtils;
import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频管理
 * Created by zhaofan on 2016/8/24.
 */
public class MediaManageActivity extends initActivity implements SwipeMenuListView.OnMenuItemClickListener {
    public static final String DELETE_MEDIA = "delete_media";
    private SwipeMenuListView mAutoInfoListView1;
    String path;
    long size, totalSize=1;
    private CustomToolBar mToolBar;
    private TextView mRightTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_media_manage;
    }

    @Override
    public void bindView() {
        mAutoInfoListView1 = (SwipeMenuListView) findViewById(R.id.lv);
        bindListener();
    }

    private void bindListener() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        assert mToolBar != null;
        mRightTv = (TextView) mToolBar.findViewById(R.id.topbar_right_tv);
        mAutoInfoListView1.setOnMenuItemClickListener(this);
        mAutoInfoListView1.setOnMenuStateChangeListener(menuStateListener);
    }

    @Override
    public void init() {
        initToolBar();
        path = getIntent().getStringExtra("path");
        totalSize = getIntent().getLongExtra("totalSize", 1);
        try {
            size = FileUtils.getFileSize(new File(path));
            DebugLog.i("size", FileUtils.generateFileSize(size) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (size < totalSize) {
            showEmpty(this);
            mRightTv.setVisibility(View.GONE);
        } else {
            mRightTv.setVisibility(View.VISIBLE);
            initSwipeMenu();
            setListView();
        }
    }


    private void initToolBar() {
        mToolBar.setTitle("视频管理");
        mRightTv.setText("编辑");
        mRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightTv.getText().toString().equals("编辑")) {
                    mAutoInfoListView1.smoothOpenMenu(0);
                    mRightTv.setText("取消");
                } else {
                    mAutoInfoListView1.smoothCloseMenu();
                    mRightTv.setText("编辑");
                }
            }
        });
    }

    private void setListView() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("size", FileUtils.generateFileSize(size) + "");
        list.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, list,
                R.layout.item_media_manage, new String[]{"size"},
                new int[]{R.id.size});
        mAutoInfoListView1.setAdapter(adapter);


    }


    private void initSwipeMenu() {
        //创建条目侧滑菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // 删除按钮
                SwipeMenuItem.createSwipeMenuItem(
                        menu,
                        getApplicationContext(),
                        R.color.font_red,
                        ScreenUtil.dip2px(mContext, 80),
                        "删除",
                        16,
                        Color.WHITE,
                        0);
            }
        };
        mAutoInfoListView1.setMenuCreator(creator);
    }


    private SwipeMenuListView.OnMenuStateChangeListener menuStateListener = new SwipeMenuListView.OnMenuStateChangeListener() {
        @Override
        public void onMenuOpen(int position) {
            mRightTv.setText("取消");
        }

        @Override
        public void onMenuClose(int position) {
            mRightTv.setText("编辑");
        }
    };

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

        FileUtils.deleteFile(path);
        init();
        EventBus.getDefault().post(new Event("", DELETE_MEDIA));
        return false;
    }

    private void showEmpty(final Activity mActivity) {
        ErrorView.builder(mActivity)
                .showCustom("暂无缓存视频", null, R.drawable.ship_onthing, false, null);

    }


}
