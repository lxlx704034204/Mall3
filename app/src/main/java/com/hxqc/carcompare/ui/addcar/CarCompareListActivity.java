package com.hxqc.carcompare.ui.addcar;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.carcompare.control.ErrorViewCtrl;
import com.hxqc.carcompare.db.DAO;
import com.hxqc.carcompare.ui.compare.CarCompareDetailActivity;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenu;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuCreator;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuItem;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuListView;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel_Table;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import hxqc.mall.R;

/**
 * 添加对比车型列表
 * Created by zhaofan on 2016/10/9.
 */
public class CarCompareListActivity extends initActivity implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {
    public static final String TAG = CarCompareListActivity.class.getSimpleName();
    public static final String UPDATA_STATUS = "updata_status";
    private CustomToolBar mToolBar;
    private CarCompareAdapter mCarCompareAdapter;
    private SwipeMenuListView mListView;
    private List<ChooseCarModel> mList;
    private View addCarView;
    private TextView mCompareTv, mDeleteTv;
    private TextView rightBtn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_car_compare;
    }

    @Override
    public void bindView() {
        mListView = (SwipeMenuListView) findViewById(R.id.scroll_list);
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        mCompareTv = (TextView) findViewById(R.id.btn_compare);
        mDeleteTv = (TextView) findViewById(R.id.btn_delete);
        bindListener();
    }

    private void bindListener() {
        mCompareTv.setOnClickListener(this);
        mDeleteTv.setOnClickListener(this);
        mListView.setOnMenuItemClickListener(this);
        mListView.setOnMenuStateChangeListener(menuStateListener);
    }

    @Override
    public void init() {
        mEventBus.register(this);
        initToolBar();
        initSwipeMenu();
        getCarData();
        addCarView = LayoutInflater.from(this).inflate(R.layout.view_carcompare_addcar, null, false);
        mListView.addHeaderView(addCarView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mCarCompareAdapter = new CarCompareAdapter(this, mListView);
        mListView.setAdapter(mCarCompareAdapter);

        setListData();
        //增加车型
        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toFilterAllShopBrand(mContext, true);
            }
        });


    }

    private void setListData() {
        mList = DAO.queryCarCompareList();
        rightBtn.setVisibility(mList.isEmpty() ? View.GONE : View.VISIBLE);
        mCarCompareAdapter.setData(mList);
        updataBtnText();
        if (mList.isEmpty()) {
            ErrorViewCtrl.CarEmpty(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivitySwitcherThirdPartShop.toFilterAllShopBrand(mContext, true);
                }
            });
        } else
            ErrorViewCtrl.removeCarEmpty();
    }

    private void updataBtnText() {
        int count_comp = 0;
        for (ChooseCarModel data : DAO.queryCarCompareList()) {
            if (data.getIsCheck() == 1)
                count_comp++;
        }
        int count_del = mListView.getCheckedItemCount();
        DebugLog.i("tag", mListView.getCheckedItemCount() + "");
        mDeleteTv.setText("删除" + "(" + count_del + ")");
        mCompareTv.setText("开始对比" + "(" + count_comp + ")");
    }

    private void initToolBar() {
        mToolBar.setTitle("车型对比");
        rightBtn = (TextView) mToolBar.findViewById(R.id.topbar_right_tv);
        rightBtn.setText("编辑");
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is = rightBtn.getText().toString().equals("编辑");
                if (is) {
                    showEdit(rightBtn, true);
                } else {
                    for (int i = 0; i < mCarCompareAdapter.getCount(); i++)
                        mListView.setItemChecked(i, true);
                    updataBtnText();
                }
            }
        });
    }

    private void showEdit(final TextView rightBtn, boolean is) {
        mCarCompareAdapter.setEditMode(is);
        rightBtn.setText(is ? "全选" : "编辑");
        mCompareTv.setVisibility(is ? View.GONE : View.VISIBLE);
        mDeleteTv.setVisibility(is ? View.VISIBLE : View.GONE);
        mToolBar.showLeftImg(!is);
        if (is) {
            mListView.removeHeaderView(addCarView);
            mDeleteTv.setText("删除(0)");
            mToolBar.setLeftText("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEdit(rightBtn, false);
                }
            });
        } else {
            mListView.addHeaderView(addCarView);
            mToolBar.setLeftText("", null);
        }
        mListView.clearChoices();
        updataBtnText();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(0);
            }
        }, 20);
    }


    private void getCarData() {
        String extID = ActivitySwitchBase.getBundleExtra(this).getString(FilterResultKey.EXT_ID);
        String brand = ActivitySwitchBase.getBundleExtra(this).getString(FilterResultKey.BRAND_KEY);
        String model = ActivitySwitchBase.getBundleExtra(this).getString(FilterResultKey.MODEL_KEY);
        if (TextUtils.isEmpty(extID) || TextUtils.isEmpty(brand) || TextUtils.isEmpty(model))
            return;
        ModelInfo data = new ModelInfo(brand, model, extID, "");
        addCompareCarDb(data, false);
    }


    /**
     * 添加车型
     * from {@link com.hxqc.mall.thirdshop.views.adpter.FilterCarModelAdapter#getView}
     */
    @Subscribe
    public void addCar(ModelInfo data) {
        addCompareCarDb(data, true);
    }

    private void addCompareCarDb(ModelInfo data, final boolean refreshList) {
        DAO.addCompareCarDb(data.getBrand(), data.getModelName(), data.getExtID(), new DAO.onSuccessCallBack() {
            @Override
            public void onSaveSuccess() {
                if (refreshList) {
                    setListData();
                    //mListView.setSelection(mList.size());
                    updateItemAnim(0, R.anim.fade_in_view);
                }
            }
        });
    }


    private void updateItemAnim(int index, int resId) {
        int visiblePosition = mListView.getFirstVisiblePosition();
        int pos = index - visiblePosition;
        if (pos >= 0) {
            //得到要更新的item的view
            View view = mListView.getChildAt(pos + mListView.getHeaderViewsCount());
            if (view != null)
                view.startAnimation(AnimationUtils.loadAnimation(this, resId));
        }
    }


    @Subscribe
    public void updataStatus(Event msg) {
        if (msg.what.equals(UPDATA_STATUS)) {
            updataBtnText();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //对比
        if (id == R.id.btn_compare) {
            List<ChooseCarModel> array = DAO.queryCarCompareList(ChooseCarModel_Table.isCheck.eq(1));
            DebugLog.i("array", array.toString());

            if (array.size() == 0) {
                ToastHelper.showYellowToast(mContext, "请至少选择一款车型");
                return;
            }

            mEventBus.postSticky(new Event(array, TAG));
            startActivity(new Intent(this, CarCompareDetailActivity.class));
        }
        //删除
        else if (id == R.id.btn_delete) {
            for (int i = 0; i < mList.size(); i++)
                if (mListView.isItemChecked(i)) {
                    DebugLog.i("array", i + "");
                    String extId = mList.get(i).getExtId();
                    //// TODO: 2016/10/21
                    DAO.deleteCarDb(extId);
                }
            //    mListView.clearChoices();
            setListData();
            updataBtnText();
            showEdit(rightBtn, false);
        }

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
                        R.color.main_orange_et,
                        ScreenUtil.dip2px(mContext, 70),
                        "删除",
                        16,
                        Color.WHITE,
                        0);
            }
        };
        mListView.setMenuCreator(creator);
    }

    private SwipeMenuListView.OnMenuStateChangeListener menuStateListener = new SwipeMenuListView.OnMenuStateChangeListener() {
        @Override
        public void onMenuOpen(int position) {
        }

        @Override
        public void onMenuClose(int position) {
        }
    };

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        String id = DAO.queryCarCompareList().get(position).getExtId();
        DAO.deleteCarDb(id);
        updateItemAnim(position, R.anim.fade_out);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setListData();
            }
        }, 200);


        return false;
    }
}
