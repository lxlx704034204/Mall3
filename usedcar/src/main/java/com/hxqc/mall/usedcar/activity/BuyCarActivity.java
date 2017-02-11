package com.hxqc.mall.usedcar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.fragment.BuyCarListFragment;
import com.hxqc.mall.usedcar.fragment.UsedCarSearchHotKeywordsFragment;
import com.hxqc.mall.usedcar.fragment.UsedCarSearchKeywordFragment;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.BuyCarFilterTipView;
import com.hxqc.util.DisplayTools;

import java.util.HashMap;
import java.util.Map;


/**
 * 买车
 * Created by huangyi on 15/11/19.
 */
public class BuyCarActivity extends NoBackActivity implements View.OnClickListener, BuyCarFilterTipView.OnConditionClickListener,
        UsedCarSearchKeywordFragment.SearchKeywordCallBack {

    /**
     * 搜索关键字
     **/
    public String keyword;
    /**
     * Integer(区分筛选条件)   String[](记录筛选值)第一个String id, 第二个String Value
     **/
    public HashMap<Integer, String[]> selectedTipMap = new HashMap<>();
    public FrameLayout hotKeywordsView; //热搜列表

    Toolbar mToolbar;
    TextView mKeywordView; //关键字
    ImageView mClearKeywordView; //清除关键字
    LinearLayout mConditionParentView; //已选内容 + 取消
    LinearLayout mConditionView; //已选内容
    ImageView mCancelView; //取消
    int mConditionViewWidth; //已选内容的宽度
    BuyCarListFragment mBuyCarListFragment; //车辆列表

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_car);
        if (null != getIntent())
            keyword = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("keyword");
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //BuyCarFilterActivity 车辆条件筛选界面过来
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            keyword = data.getStringExtra("keyword");
            mKeywordView.setText(keyword); //更新关键字显示
            if (!TextUtils.isEmpty(keyword)) {
                mClearKeywordView.setVisibility(View.VISIBLE);
            } else {
                mClearKeywordView.setVisibility(View.GONE);
            }
            selectedTipMap = (HashMap<Integer, String[]>) data.getSerializableExtra("selectedFilter"); //更新筛选条件
            updateConditionView();
            mBuyCarListFragment.refreshPage();
            mBuyCarListFragment.refreshData(true);
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.buy_car_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mKeywordView = (TextView) findViewById(R.id.buy_car_keyword);
        mClearKeywordView = (ImageView) findViewById(R.id.buy_car_clear);
        mConditionParentView = (LinearLayout) findViewById(R.id.buy_car_condition_parent);
        mConditionView = (LinearLayout) findViewById(R.id.buy_car_condition);

        mCancelView = (ImageView) findViewById(R.id.buy_car_cancel);
        mCancelView.measure(0, 0);
        mConditionViewWidth = DisplayTools.getScreenWidth(this) - mCancelView.getMeasuredWidth() - DisplayTools.dip2px(this, 50);

        mKeywordView.setOnClickListener(this);
        mClearKeywordView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);

        BuyCarFilterTipView mFilterTipView = (BuyCarFilterTipView) findViewById(R.id.buy_car_tip);
        mFilterTipView.setShadeView(findViewById(R.id.buy_car_shade));
        mFilterTipView.setOnConditionClickListener(this);

        mBuyCarListFragment = new BuyCarListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.buy_car_list, mBuyCarListFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.buy_car_hot, new UsedCarSearchHotKeywordsFragment()).commit();
        hotKeywordsView = (FrameLayout) findViewById(R.id.buy_car_hot);

        //关键字处理
        if (!TextUtils.isEmpty(keyword)) mClearKeywordView.setVisibility(View.VISIBLE);
        mKeywordView.setText(keyword);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buy_car_keyword) { //去搜索界面
            UsedCarActivitySwitcher.toSearch(this);

        } else if (id == R.id.buy_car_clear) { //清除关键字
            keyword = "";
            mKeywordView.setText(keyword);
            mClearKeywordView.setVisibility(View.GONE);
            mBuyCarListFragment.refreshPage();
            mBuyCarListFragment.refreshData(true);

        } else if (id == R.id.buy_car_cancel) { //清除条件
            selectedTipMap.clear();
            mConditionParentView.setVisibility(View.GONE);
            mConditionView.removeAllViews();
            mBuyCarListFragment.refreshPage();
            mBuyCarListFragment.refreshData(true);
        }
    }

    @Override
    public void onCondition(boolean isUnlimited, int ID, String[] condition) {
        if (isUnlimited) { //选择不限
            selectedTipMap.remove(ID);
        } else {
            selectedTipMap.put(ID, condition);
        }
        updateConditionView();
        mBuyCarListFragment.refreshPage();
        mBuyCarListFragment.refreshData(true);
    }

    @Override
    public void onFilter() {
        Intent intent = new Intent(this, BuyCarFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("total", mBuyCarListFragment.total);
        bundle.putString("keyword", keyword);
        bundle.putSerializable(UsedCarActivitySwitcher.SELECTED_TIP_MAP, selectedTipMap);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    public void clickKeyword(Keyword keyword) {
        this.keyword = keyword.keyword;
        mKeywordView.setText(this.keyword);
        mClearKeywordView.setVisibility(View.VISIBLE);
        mCancelView.performClick();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        UsedCarActivitySwitcher.toMain(this, 0);
        return super.onSupportNavigateUp();
    }

    /**
     * 更新已选内容
     **/
    public void updateConditionView() {
        mConditionView.removeAllViews(); //先移除所有再重新添加
        for (Map.Entry<Integer, String[]> entry : selectedTipMap.entrySet()) {
            switch (entry.getKey()) {
                case 0: //价格
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 1: //品牌
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 2: //年龄
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 3: //级别
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 4: //来源
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 5: //排量
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 6: //变速箱
                    addView(initTextView((entry.getValue())[1]));
                    break;
                case 7: //里程
                    addView(initTextView((entry.getValue())[1]));
                    break;
            }
        }
        //判断是否需要显示
        if (selectedTipMap.containsKey(0) || selectedTipMap.containsKey(1) || selectedTipMap.containsKey(2) || selectedTipMap.containsKey(3) ||
                selectedTipMap.containsKey(4) || selectedTipMap.containsKey(5) || selectedTipMap.containsKey(6) || selectedTipMap.containsKey(7)) {
            mConditionParentView.setVisibility(View.VISIBLE);
        } else {
            mConditionParentView.setVisibility(View.GONE);
        }
    }

    private TextView initTextView(String str) {
        TextView tv = new TextView(this);
        tv.setPadding(5, 5, 5, 5);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        tv.setTextColor(getResources().getColor(R.color.searchconditionword_gray));
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(str);
        return tv;
    }

    private void addView(View view) {
        view.measure(0, 0);
        int childCount = mConditionView.getChildCount();
        if (childCount == 0) { //新建一个LinearLayout添加
            initLinearLayoutAddView(view);
        } else {
            //获取mSearchConditionShowView 最末尾的一个LinearLayout
            LinearLayout linearLayout = (LinearLayout) mConditionView.getChildAt(childCount - 1);
            //获取末尾LinearLayout所有子View宽度的和
            int linearLayoutChildCountWidth = 0;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                linearLayoutChildCountWidth += linearLayout.getChildAt(i).getMeasuredWidth();
            }
            //如果新添加的View的宽度 < 末尾LinearLayout剩余的位置 直接追加
            if (view.getMeasuredWidth() < mConditionViewWidth - linearLayoutChildCountWidth) {
                linearLayout.addView(view);
            } else { //新建一个LinearLayout添加
                initLinearLayoutAddView(view);
            }
        }
    }

    private void initLinearLayoutAddView(View view) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(view);
        mConditionView.addView(linearLayout);
    }

}
