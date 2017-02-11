package com.hxqc.mall.fragment.order;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hxqc.mall.core.adapter.order.ScreenPopWindowAdapter;
import com.hxqc.mall.core.fragment.FunctionFragment;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import hxqc.mall.R;

public class MyOrderEntranceFragment extends FunctionFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private ScreenPopWindowAdapter mScreenPopWindowAdapter;
    private OverlayDrawer mOverlayDrawer;
    private ListView mListView;
    private MyOrderListFragment mMyOrderListFragment;
    private Toolbar mToolbar;
    private ImageView mMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order_entrance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("我的订单");
        mToolbar.setTitleTextColor(Color.WHITE);

        mMenu = (ImageView) view.findViewById(R.id.menu);
        mMenu.setOnClickListener(this);
        mOverlayDrawer = (OverlayDrawer) view.findViewById(R.id.myorder_drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(true);
        mListView = (ListView) view.findViewById(R.id.myorder_pop_list);
        mScreenPopWindowAdapter = new ScreenPopWindowAdapter(getActivity());
        mListView.setAdapter(mScreenPopWindowAdapter);
        mListView.setOnItemClickListener(this);
        mMyOrderListFragment = (MyOrderListFragment) getChildFragmentManager().findFragmentById(R.id.myorder_fragment);


    }

    public void showBackButton() {

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    @Override
    public void onResume() {
        mOverlayDrawer.setMenuSize(getListViewHeight(mListView));
        super.onResume();
    }

    /**
     * huoqu ListView高度
     *
     * @param pull
     * @return
     */
    private int getListViewHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }
        totalHeight = totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));//分割线也要计算
        return totalHeight;
    }

    @Override
    public String fragmentDescription() {
        return "我的订单";
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        mScreenPopWindowAdapter.setCheckColorChange(mScreenPopWindowAdapter.getItem(position).toString());
        mMyOrderListFragment.setOrderType(mScreenPopWindowAdapter.getItem(position).toString());
        if (mOverlayDrawer.isMenuVisible())
            mOverlayDrawer.closeMenu();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu)
            if (mOverlayDrawer.isMenuVisible())
                mOverlayDrawer.closeMenu();
            else
                mOverlayDrawer.openMenu();
    }
}
