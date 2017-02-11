package com.hxqc.mall.views.autopackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AccessoryPhoto;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2015-11-16
 * FIXME
 * Todo 选择单项
 */
public class GoodsItem extends RelativeLayout implements Checkable {
    private TextView goodsTitle;//价格
    private TextView goodsPrice;//价格金额
    private TextView goodsTotalCost;//小计金额
    private CheckBox mCheckBox;//选中按钮
    private ImageView goodsPic;//物品图片
    private RelativeLayout container;//容器背景，用于表示选中
    private boolean checked;//选中标示
    private Accessory mAccessory = null;//数据
    private boolean isCustom;//是否是自定义

    public GoodsItem(Context context) {
        this(context, null);
    }

    public GoodsItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    private OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init(getContext());
    }

    /**
     * 选择当前项回调
     */
    public interface OnItemSelectListener {
        void onItemSelect(Accessory mAccessory, boolean isSelected);
    }


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    /**
     * 添加数据
     *
     * @param accessory
     */
    public void setAccessory(Accessory accessory, boolean isSelected, boolean isCustom) {
        mAccessory = accessory;
        this.isCustom = isCustom;
        checked = isSelected;
        addData();
        refreshUI();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_goods_item, this);
        goodsPic = (ImageView) findViewById(R.id.goods_pic);
        goodsPrice = (TextView) findViewById(R.id.goods_price);
        goodsTitle = (TextView) findViewById(R.id.goods_title);
        goodsTotalCost = (TextView) findViewById(R.id.goods_total_cost);
        mCheckBox = (CheckBox) findViewById(R.id.goods_cb);
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(mCheckBox.isChecked());
                if (null != onItemSelectListener)
                    onItemSelectListener.onItemSelect(mAccessory, mCheckBox.isChecked());
            }
        });
        container = (RelativeLayout) findViewById(R.id.goods_container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                if (null != onItemSelectListener)
                    onItemSelectListener.onItemSelect(mAccessory, checked);
            }
        });

        goodsPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener)
                    onClickListener.onClick(v);
            }
        });

        mAccessory = new Accessory();
        refreshUI();
    }

    /**
     * 添加数据到界面中
     */
    private void addData() {
        if (null == mAccessory)
            return;
        goodsTitle.setText(mAccessory.title);
        goodsPrice.setText(String.format("%s x %d", OtherUtil.stringToMoney(mAccessory.price), mAccessory.count));
        float price = Float.parseFloat(mAccessory.price);
        float totalCost = price * mAccessory.count;//总共费用
        goodsTotalCost.setText(String.format("%s", OtherUtil.stringToMoney(totalCost)));
        ArrayList<AccessoryPhoto> photo = mAccessory.photo;
        ImageUtil.setImage(getContext(), goodsPic, photo.get(0).thumb);
    }

    /**
     * 刷新
     */
    private void refreshUI() {
        if (checked) {
            //选中
            container.setBackgroundResource(R.drawable.bg_goods_item_selected);
        } else {
            //没有选中
            container.setBackgroundResource(R.drawable.bg_goods_item);
        }
        if (isCustom) {
            mCheckBox.setVisibility(View.VISIBLE);
            mCheckBox.setChecked(checked);

        } else {
            mCheckBox.setVisibility(View.INVISIBLE);
        }
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
