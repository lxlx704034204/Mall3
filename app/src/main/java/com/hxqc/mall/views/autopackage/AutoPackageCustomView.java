package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AutoPackage;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2015-11-16
 * FIXME
 * Todo 套餐包自定义文件
 */
public class AutoPackageCustomView extends LinearLayout implements Checkable, AutoPackageAdapter.OnAdapterClickListener {
    private RecyclerView goodsList;//商品列表
    private AutoPackage mAutoPackage;//数据
    private boolean checked = false;//是否选中
    private boolean isCustom = true;//是否是自定义
    private OnSelectListener onSelectListener;
    private AutoPackageAdapter adapter;


    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }


    public interface OnSelectListener {
        void onSelectClick(AutoPackage autoPackage, boolean isSelect);

        void onCustomItemClick(AutoPackage autoPackage, boolean isSelect);

        void onGoodsItemClick(AutoPackage autoPackage, boolean isSelect);
    }

    public AutoPackageCustomView(Context context) {
        this(context, null);
    }

    public AutoPackageCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPackageCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_custom_auto_package_view, this);
        goodsList = (RecyclerView) findViewById(R.id.goods_list);
        mAutoPackage = new AutoPackage();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        goodsList.setLayoutManager(manager);
        initAdapter();
    }


    /**
     * 初次加载数据
     *
     * @param autoPackage
     * @param checked
     */
    public void setAutoPackage(AutoPackage autoPackage, boolean checked) {
        mAutoPackage = autoPackage;
        this.checked = checked;
//        refreshUI();
        checkType();
        if (isCustom && !checked) {
            AutoPackage.clearTempChooseAccessory();//清除临时的
        }
        if (null == mAutoPackage)
            return;
        adapter = new AutoPackageAdapter(getContext(), mAutoPackage);
        goodsList.setAdapter(adapter);
        adapter.setOnAdapterClickListener(this);
    }

    /**
     * 刷新数据
     *
     * @param autoPackage
     * @param checked
     */
    public void notifyDataChange(AutoPackage autoPackage, boolean checked) {
        mAutoPackage = autoPackage;
        this.checked = checked;
        refreshUI();
    }


    private void initAdapter() {
        checkType();
        adapter = new AutoPackageAdapter(getContext(), mAutoPackage);
        goodsList.setAdapter(adapter);
        adapter.setOnAdapterClickListener(this);
    }

    /**
     * 填充数据
     */
    private void refreshUI() {
        checkType();
        if (isCustom && !checked) {
            AutoPackage.clearTempChooseAccessory();//清除临时的
        }
        if (null == mAutoPackage)
            return;
//        AutoPackage.PackageTypeEnum customPackage = mAutoPackage.isCustomPackage();
//        autoPackages.clear();
//        autoPackages.add(mAutoPackage);

//        adapter.notifyDataSetChanged();
        adapter = new AutoPackageAdapter(getContext(), mAutoPackage);
        goodsList.setAdapter(adapter);
        adapter.setOnAdapterClickListener(this);
    }


    @Override
    public void onItemSelect(Accessory mAccessory, boolean isSelected) {
        if (isCustom) {
            //自定义套餐的单项选择改变时
            mAutoPackage.setChooseAccessor(mAccessory, isSelected);//添加进临时容器中
            int size = AutoPackage.getTempChooseAccessory().size();
            if (null != onSelectListener)
                if (size > 0)
                    onSelectListener.onCustomItemClick(mAutoPackage, true);
                else
                    onSelectListener.onCustomItemClick(mAutoPackage, false);
        } else {
            if (null != onSelectListener) {
                onSelectListener.onGoodsItemClick(mAutoPackage, isSelected);
            }
        }
    }

    @Override
    public void onPackageSelect(View view, boolean isChecked) {
        if (null != onSelectListener) {
            onSelectListener.onSelectClick(mAutoPackage, isChecked);
        }
    }

    @Override
    public void onClearAllClick() {
        AutoPackage.clearTempChooseAccessory();
        if (null != onSelectListener)
            onSelectListener.onSelectClick(mAutoPackage, false);
        refreshUI();
    }

    /**
     * 检查是否是自定义类型套餐
     */
    private void checkType() {
        isCustom = mAutoPackage.isCustomPackage() == AutoPackage.PackageTypeEnum.custom;
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        refreshUI();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        refreshUI();
    }


}
