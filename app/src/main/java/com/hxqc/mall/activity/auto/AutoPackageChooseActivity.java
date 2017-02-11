package com.hxqc.mall.activity.auto;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.control.AutoItemDataControl;
import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.views.autopackage.AutoPackageCustomView;
import com.hxqc.mall.views.autopackage.AutoPackageListAdapter;
import com.hxqc.mall.views.autopackage.TopBar;

import java.util.ArrayList;
import java.util.Set;

import hxqc.mall.R;

/**
 * Author: 李烽
 * Date: 2015-10-9
 * FIXME
 * Todo  套餐选择列表
 */
public class AutoPackageChooseActivity extends NoBackActivity implements TopBar.OnSelectListener,
        AutoPackageCustomView.OnSelectListener, ViewPager.OnPageChangeListener {

    AutoItemDataControl mAutoItemDataControl;
    ArrayList<AutoPackage> mAutoPackages = new ArrayList<>();
    TopBar mTopBar;
    LinearLayout topBarLayout;
    ViewPager mViewPager;//
    private int checkedIndex;//选中的脚标
    private AutoPackage autoPackage;//当前展示的套餐包数据
    private static AutoPackage mTempChoosePackage;
    private int position = -1;//初始加载
    private ArrayList<View> views;//存储view的
    private AutoPackageListAdapter viewAdapter;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_package_choose);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        position = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(ActivitySwitchBase.KEY_DATA, 0);

        initView();
        setListener();
    }

    private void initView() {
        mAutoItemDataControl = AutoItemDataControl.getInstance();
        mAutoPackages = mAutoItemDataControl.getAutoPackages();
        mTempChoosePackage = mAutoItemDataControl.getCheckAutoPackage();

        mTopBar = (TopBar) findViewById(R.id.choose_auto_package_topBar);
        topBarLayout = (LinearLayout) findViewById(R.id.topBar_container);
        mViewPager = (ViewPager) findViewById(R.id.auto_package_viewPager);
        views = new ArrayList<>();
    }

    private void setListener() {
        mTopBar.setOnSelectListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mTempChoosePackage != null) {
            AutoPackage.clearTempChooseAccessory();
            mTempChoosePackage = null;
        }
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (mTempChoosePackage != null) {
            AutoPackage.clearTempChooseAccessory();
            mTempChoosePackage = null;
        }
        finish();
        super.onBackPressed();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        String[] barNames = new String[mAutoPackages.size()];
        for (int i = 0; i < mAutoPackages.size(); i++) {
            barNames[i] = mAutoPackages.get(i).title;
            AutoPackageCustomView view = new AutoPackageCustomView(this);
            view.setAutoPackage(mAutoPackages.get(i), checkIsChoosed(i));
            view.setOnSelectListener(this);
            views.add(view);
        }
        viewAdapter = new AutoPackageListAdapter(views);
        mViewPager.setAdapter(viewAdapter);
        mTopBar.setTexts(barNames);
        setTopBar();
        checkedIndex = mViewPager.getCurrentItem();
        autoPackage = mAutoPackages.get(checkedIndex);
        setMenuText();
    }

    /**
     * 右侧按钮文字
     */
    private void setMenuText() {
        if (null == item) {
            return;
        }
        if (mTempChoosePackage == null) {
            item.setTitle(getString(R.string.auto_package_number));
            return;
        }
        switch (mTempChoosePackage.isCustomPackage()) {
            case custom:
                Set<Accessory> customChooseAccessory = AutoPackage.getTempChooseAccessory();
                if (customChooseAccessory != null) {
                    int size = customChooseAccessory.size();
                    item.setTitle(getString(R.string.auto_package_number)
                            + "(" + size + ")");
                }
                break;
            case combo:
                int size = mTempChoosePackage.accessory.size();
                item.setTitle(getString(R.string.auto_package_number)
                        + "(" + size + ")");
                break;
            default:
                break;
        }
    }


    /**
     * 切换套餐操作
     */
    private void refreshPackage(boolean isItem) {
        checkedIndex = mViewPager.getCurrentItem();
        autoPackage = mAutoPackages.get(checkedIndex);
        refreshViewPager(isItem);
        setMenuText();
    }


    /**
     * 刷新Viewpager
     */
    private void refreshViewPager(boolean isItem) {
        for (int i = 0; i < mAutoPackages.size(); i++) {
            if (isItem)
                if (mAutoPackages.get(i).equals(mTempChoosePackage)
                        || mTempChoosePackage == null)
                    continue;
            AutoPackageCustomView view = new AutoPackageCustomView(this);
            view.notifyDataChange(mAutoPackages.get(i), checkIsChoosed(i));
            view.setOnSelectListener(this);
            views.remove(i);
            views.add(i, view);
        }
        viewAdapter.notifyDataSetChanged();
    }


    /**
     * 设置顶部的TopBar的选择
     */
    private void setTopBar() {
        if (mAutoPackages.size() == 1) {
            mTopBar.setVisibility(View.GONE);
            topBarLayout.setVisibility(View.GONE);
        }
        int index = 0;
        if (null != mTempChoosePackage) {
            switch (mTempChoosePackage.isCustomPackage()) {
                case custom:
                    mTempChoosePackage.seTempChooseAccessory();
                    break;
                default:
                    break;
            }
            if (position == -1) {
                //点击横条进去的
                index = mTempChoosePackage.position;
            } else {
                index = position;
            }
        } else {
            if (position != -1)
                index = position;
        }

        mTopBar.setCheckedIndex(index);
        mViewPager.setCurrentItem(index);
    }

    /**
     * 检查当前是否是选中
     *
     * @return
     */
    private boolean checkIsChoosed(int position) {
        return null != mTempChoosePackage && mTempChoosePackage.equals(mAutoPackages.get(position));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_choose_package, menu);
        item = menu.getItem(0);
        setMenuText();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_confirm) {
            setChoosePackage();
            finish();
        }
        return false;
    }

    /**
     * 设置选择的套餐
     */
    private void setChoosePackage() {
        if (null != mTempChoosePackage) {
            if (mTempChoosePackage.isCustomPackage() == AutoPackage.PackageTypeEnum.custom)
                AutoPackage.setCustomChooseAccessory();
            mAutoItemDataControl.setCheckAutoPackage(mTempChoosePackage);//设置选择套餐
            mTempChoosePackage = null;
        } else {
            mAutoItemDataControl.setCheckAutoPackage(null);
        }
    }

    /**
     * topbar选择的回调
     *
     * @param view
     * @param position
     */
    @Override
    public void onSelect(View view, int position) {
        refreshPackage(false);
        mViewPager.setCurrentItem(position);
    }

    /**
     * 当套餐选择的时候回调
     *
     * @param autoPackage
     * @param isSelect
     */
    @Override
    public void onSelectClick(AutoPackage autoPackage, boolean isSelect) {
        if (isSelect)
            //当选择了时候清空其他的，加入传回的
            mTempChoosePackage = autoPackage;
        else
            mTempChoosePackage = null;
        refreshPackage(false);
    }


    /**
     * 当自定义套餐的item点击的时候回调
     *
     * @param autoPackage
     * @param isSelect
     */
    @Override
    public void onCustomItemClick(AutoPackage autoPackage, boolean isSelect) {
        if (isSelect)
            mTempChoosePackage = autoPackage;
        else
            mTempChoosePackage = null;
        refreshPackage(true);
    }

    /**
     * 当套餐item点击的时候回调
     *
     * @param autoPackage
     * @param isSelect
     */
    @Override
    public void onGoodsItemClick(AutoPackage autoPackage, boolean isSelect) {
        if (isSelect)
            mTempChoosePackage = autoPackage;
        else
            mTempChoosePackage = null;
        refreshPackage(true);
    }

    /**
     * 获取临时选择包
     *
     * @return
     */
    public synchronized static AutoPackage getTempChoosePackage() {
        return mTempChoosePackage;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTopBar.setCheckedIndex(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
