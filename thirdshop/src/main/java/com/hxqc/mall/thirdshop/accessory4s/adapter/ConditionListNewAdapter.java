package com.hxqc.mall.thirdshop.accessory4s.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.Condition;
import com.hxqc.mall.thirdshop.accessory.model.ProductsForProperty;
import com.hxqc.mall.thirdshop.accessory4s.model.ConditionTemp;
import com.hxqc.mall.thirdshop.accessory4s.model.ConditionValueTemp;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

/**
 * 商品详情Dialog弹窗ListView Adapter
 * Created by huangyi on 15/12/17.
 */
public class ConditionListNewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ProductsForProperty> mProductsForProperty;
    OnRefreshListener mOnRefreshListener;
    ArrayList<ConditionTemp> mTemp;
    LayoutInflater mLayoutInflater;
    String[] mSelected = new String[3]; //已选条件 null或者""代表没有选择;

    public ConditionListNewAdapter(Context mContext, ArrayList<ProductsForProperty> mProductsForProperty, OnRefreshListener mOnRefreshListener) {
        this.mContext = mContext;
        this.mProductsForProperty = mProductsForProperty;
        this.mOnRefreshListener = mOnRefreshListener;
        this.mTemp = new ArrayList<>();

        ArrayList<ConditionValueTemp> t1 = new ArrayList<>();
        ArrayList<ConditionValueTemp> t2 = new ArrayList<>();
        ArrayList<ConditionValueTemp> t3 = new ArrayList<>();
        for (int i = 0; i < this.mProductsForProperty.size(); i++) {
            ProductsForProperty p = this.mProductsForProperty.get(i);

            for (int j = 0; j < p.conditions.size(); j++) {
                String value = p.conditions.get(j).conditionValue;
                switch (j) {
                    case 0:
                        if (!isContains(t1, value))
                            t1.add(new ConditionValueTemp(value, ConditionValueTemp.Status.NORMAL));
                        if (i == this.mProductsForProperty.size() - 1) {
                            this.mTemp.add(new ConditionTemp(p.conditions.get(0).conditionName, t1));
                        }
                        break;

                    case 1:
                        if (!isContains(t2, value))
                            t2.add(new ConditionValueTemp(value, ConditionValueTemp.Status.NORMAL));
                        if (i == this.mProductsForProperty.size() - 1) {
                            this.mTemp.add(new ConditionTemp(p.conditions.get(1).conditionName, t2));
                        }
                        break;

                    case 2:
                        if (!isContains(t3, value))
                            t3.add(new ConditionValueTemp(value, ConditionValueTemp.Status.NORMAL));
                        if (i == this.mProductsForProperty.size() - 1) {
                            this.mTemp.add(new ConditionTemp(p.conditions.get(2).conditionName, t3));
                        }
                        break;
                }
            }
        }
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTemp.size() > 3 ? 3 : mTemp.size();
    }

    @Override
    public ConditionTemp getItem(int position) {
        return mTemp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_condition_list_new, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.condition_list_name);
            mViewHolder.mTagView = (TagFlowLayout) convertView.findViewById(R.id.condition_list_tag);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ConditionTemp model = mTemp.get(position);
        mViewHolder.mNameView.setText(model.name);
        TagAdapter adapter = new TagAdapter<ConditionValueTemp>(model.values) {
            @Override
            public View getView(FlowLayout parent, int p, ConditionValueTemp value) {
                TextView tv = (TextView) mLayoutInflater.inflate(R.layout.item_tag, null);
                tv.setText(value.value);
                if (ConditionValueTemp.Status.UNUSABLE == value.status) {
                    tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.text_color_subheading));
                    tv.setBackgroundResource(R.drawable.bg_tag_unusable);
                } else if (ConditionValueTemp.Status.NORMAL == value.status) {
                    tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.text_color_subheading));
                    tv.setBackgroundResource(R.drawable.bg_tag_normal);
                } else if (ConditionValueTemp.Status.CHECKED == value.status) {
                    tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.white));
                    tv.setBackgroundResource(R.drawable.bg_tag_checked);
                }
                return tv;
            }
        };
        mViewHolder.mTagView.setAdapter(adapter);
        mViewHolder.mTagView.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int p, FlowLayout parent) {
                ConditionValueTemp.Status status = mTemp.get(position).values.get(p).status;
                if (ConditionValueTemp.Status.NORMAL == status) {
                    //还原本组其它条件
                    restore(mTemp, position);
                    mSelected[position] = mTemp.get(position).values.get(p).value;
                    mTemp.get(position).values.get(p).status = ConditionValueTemp.Status.CHECKED;

                } else if (ConditionValueTemp.Status.CHECKED == status) {
                    //还原本组其它条件
                    restore(mTemp, position);
                    mSelected[position] = "";
                }

                //刷新数据
                switch (mProductsForProperty.get(0).conditions.size()) {
                    case 1:
                        if (!TextUtils.isEmpty(mSelected[0]) && null != mOnRefreshListener)
                            mOnRefreshListener.onRefreshData();
                        break;

                    case 2:
                        if (!TextUtils.isEmpty(mSelected[0]) && !TextUtils.isEmpty(mSelected[1]) && null != mOnRefreshListener)
                            mOnRefreshListener.onRefreshData();
                        break;

                    case 3:
                        if (!TextUtils.isEmpty(mSelected[0]) && !TextUtils.isEmpty(mSelected[1]) && !TextUtils.isEmpty(mSelected[2])
                                && null != mOnRefreshListener) mOnRefreshListener.onRefreshData();
                        break;
                }
                StringBuilder selected = new StringBuilder();
                for (int i = 0; i < mSelected.length; i++) {
                    if (!TextUtils.isEmpty(mSelected[i])) selected.append(mSelected[i]).append(" ");
                }
                mOnRefreshListener.onRefreshSelected(selected.toString());

                update();
                notifyDataSetChanged();
                return true;
            }
        });
        return convertView;
    }

    private boolean isContains(ArrayList<ConditionValueTemp> list, String value) {
        for (ConditionValueTemp c : list) {
            if (value.equals(c.value)) {
                return true;
            }
        }
        return false;
    }

    private void restore(ArrayList<ConditionTemp> list, int position) {
        for (ConditionValueTemp c : list.get(position).values) {
            c.status = ConditionValueTemp.Status.NORMAL;
        }
    }

    private void update() {
        if (!TextUtils.isEmpty(mSelected[0])) { //第1组有选择
            //临时记录第2组第3组可用条件
            ArrayList<String> t1 = new ArrayList<>();
            ArrayList<String> t2 = new ArrayList<>();
            for (int i = 0; i < mProductsForProperty.size(); i++) {
                ProductsForProperty products = mProductsForProperty.get(i);
                for (int j = 0; j < products.conditions.size(); j++) {
                    Condition condition = products.conditions.get(j);
                    if (j == 0 && !mSelected[0].equals(condition.conditionValue)) break;

                    switch (j) {
                        case 1:
                            if (!t1.contains(condition.conditionValue))
                                t1.add(condition.conditionValue);
                            break;
                        case 2:
                            if (!t2.contains(condition.conditionValue))
                                t2.add(condition.conditionValue);
                            break;
                    }
                }
            }
            //mTemp 1 2 3 组状态改变
            for (ConditionValueTemp condition : mTemp.get(0).values) {
                if (ConditionValueTemp.Status.CHECKED != condition.status)
                    condition.status = ConditionValueTemp.Status.NORMAL;
            }
            if (mTemp.size() >= 2) {
                if (t1.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        for (String s : t1) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            }
            if (mTemp.size() == 3) {
                if (t2.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(2).values) {
                        for (String s : t2) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(2).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mSelected[1])) { //第2组有选择
            //临时记录第1组第3组可用条件
            ArrayList<String> t0 = new ArrayList<>();
            ArrayList<String> t2 = new ArrayList<>();
            for (int i = 0; i < mProductsForProperty.size(); i++) {
                ProductsForProperty products = mProductsForProperty.get(i);
                for (int j = 0; j < products.conditions.size(); j++) {
                    if (mSelected[1].equals(products.conditions.get(1).conditionValue)) {
                        if (!t0.contains(products.conditions.get(0).conditionValue))
                            t0.add(products.conditions.get(0).conditionValue);
                        if (j == 2 && !t2.contains(products.conditions.get(2).conditionValue))
                            t2.add(products.conditions.get(2).conditionValue);
                    }
                }
            }
            //mTemp 1 2 3 组状态改变
            if (t0.size() != 0) {
                for (ConditionValueTemp condition : mTemp.get(0).values) {
                    for (String s : t0) {
                        if (condition.value.equals(s)) {
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.NORMAL;
                            break;
                        }
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            } else {
                for (ConditionValueTemp condition : mTemp.get(0).values) {
                    if (ConditionValueTemp.Status.CHECKED != condition.status)
                        condition.status = ConditionValueTemp.Status.UNUSABLE;
                }
            }
            if (TextUtils.isEmpty(mSelected[0])) {
                for (ConditionValueTemp condition : mTemp.get(1).values) {
                    if (ConditionValueTemp.Status.CHECKED != condition.status)
                        condition.status = ConditionValueTemp.Status.NORMAL;
                }
            }
            //要额外考虑第1组有选择的情况
            if (mTemp.size() == 3) {
                if (TextUtils.isEmpty(mSelected[0])) {
                    if (t2.size() != 0) {
                        for (ConditionValueTemp condition : mTemp.get(2).values) {
                            for (String s : t2) {
                                if (condition.value.equals(s)) {
                                    if (ConditionValueTemp.Status.CHECKED != condition.status)
                                        condition.status = ConditionValueTemp.Status.NORMAL;
                                    break;
                                }
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.UNUSABLE;
                            }
                        }
                    } else {
                        for (ConditionValueTemp condition : mTemp.get(2).values) {
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    ArrayList<String> tt2 = new ArrayList<>(); //第1组第2组共同筛选下 第3组可用条件
                    for (int i = 0; i < mProductsForProperty.size(); i++) {
                        ProductsForProperty products = mProductsForProperty.get(i);
                        for (int j = 0; j < products.conditions.size(); j++) {
                            if (mSelected[0].equals(products.conditions.get(0).conditionValue) && mSelected[1].equals(products.conditions.get(1).conditionValue)) {
                                if (!tt2.contains(products.conditions.get(2).conditionValue))
                                    tt2.add(products.conditions.get(2).conditionValue);
                            }
                        }
                    }
                    if (tt2.size() != 0) {
                        for (ConditionValueTemp condition : mTemp.get(2).values) {
                            for (String s : tt2) {
                                if (condition.value.equals(s)) {
                                    if (ConditionValueTemp.Status.CHECKED != condition.status)
                                        condition.status = ConditionValueTemp.Status.NORMAL;
                                    break;
                                }
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.UNUSABLE;
                            }
                        }
                    } else {
                        for (ConditionValueTemp condition : mTemp.get(2).values) {
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mSelected[2])) { //第3组有选择
            //临时记录第1组第2组可用条件
            ArrayList<String> t0 = new ArrayList<>();
            ArrayList<String> t1 = new ArrayList<>();
            for (int i = 0; i < mProductsForProperty.size(); i++) {
                ProductsForProperty products = mProductsForProperty.get(i);
                for (int j = 0; j < products.conditions.size(); j++) {
                    if (mSelected[2].equals(products.conditions.get(2).conditionValue)) {
                        if (!t0.contains(products.conditions.get(0).conditionValue))
                            t0.add(products.conditions.get(0).conditionValue);
                        if (!t1.contains(products.conditions.get(1).conditionValue))
                            t1.add(products.conditions.get(1).conditionValue);
                    }
                }
            }
            //mTemp 1 组状态改变
            if (TextUtils.isEmpty(mSelected[1])) {
                if (t0.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(0).values) {
                        for (String s : t0) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(0).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            } else {
                ArrayList<String> tt0 = new ArrayList<>(); //第2组第3组共同筛选下 第1组可用条件
                for (int i = 0; i < mProductsForProperty.size(); i++) {
                    ProductsForProperty products = mProductsForProperty.get(i);
                    for (int j = 0; j < products.conditions.size(); j++) {
                        if (mSelected[1].equals(products.conditions.get(1).conditionValue) && mSelected[2].equals(products.conditions.get(2).conditionValue)) {
                            if (!tt0.contains(products.conditions.get(0).conditionValue))
                                tt0.add(products.conditions.get(0).conditionValue);
                        }
                    }
                }
                if (tt0.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(0).values) {
                        for (String s : tt0) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(0).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            }
            //mTemp 2 组状态改变
            if (TextUtils.isEmpty(mSelected[0])) {
                if (t1.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        for (String s : t1) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            } else {
                ArrayList<String> tt1 = new ArrayList<>(); //第1组第3组共同筛选下 第2组可用条件
                for (int i = 0; i < mProductsForProperty.size(); i++) {
                    ProductsForProperty products = mProductsForProperty.get(i);
                    for (int j = 0; j < products.conditions.size(); j++) {
                        if (mSelected[0].equals(products.conditions.get(0).conditionValue) && mSelected[2].equals(products.conditions.get(2).conditionValue)) {
                            if (!tt1.contains(products.conditions.get(1).conditionValue))
                                tt1.add(products.conditions.get(1).conditionValue);
                        }
                    }
                }
                if (tt1.size() != 0) {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        for (String s : tt1) {
                            if (condition.value.equals(s)) {
                                if (ConditionValueTemp.Status.CHECKED != condition.status)
                                    condition.status = ConditionValueTemp.Status.NORMAL;
                                break;
                            }
                            if (ConditionValueTemp.Status.CHECKED != condition.status)
                                condition.status = ConditionValueTemp.Status.UNUSABLE;
                        }
                    }
                } else {
                    for (ConditionValueTemp condition : mTemp.get(1).values) {
                        if (ConditionValueTemp.Status.CHECKED != condition.status)
                            condition.status = ConditionValueTemp.Status.UNUSABLE;
                    }
                }
            }
            //mTemp 3 组状态改变
            if (TextUtils.isEmpty(mSelected[0]) && TextUtils.isEmpty(mSelected[1])) {
                for (ConditionValueTemp condition : mTemp.get(2).values) {
                    if (ConditionValueTemp.Status.CHECKED != condition.status)
                        condition.status = ConditionValueTemp.Status.NORMAL;
                }
            }
        }

        if (TextUtils.isEmpty(mSelected[0]) && TextUtils.isEmpty(mSelected[1]) && TextUtils.isEmpty(mSelected[2])) {
            if (mTemp.size() == 1) {
                restore(mTemp, 0);
            }

            if (mTemp.size() == 2) {
                restore(mTemp, 0);
                restore(mTemp, 1);
            }

            if (mTemp.size() == 3) {
                restore(mTemp, 0);
                restore(mTemp, 1);
                restore(mTemp, 2);
            }
        }
    }

    public void init(String s0, String s1, String s2) {
        mSelected[0] = s0;
        mSelected[1] = s1;
        mSelected[2] = s2;

        if (!TextUtils.isEmpty(s0)) {
            restore(mTemp, 0);
            for (ConditionValueTemp c : mTemp.get(0).values) {
                if (s0.equals(c.value))
                    c.status = ConditionValueTemp.Status.CHECKED;
            }
        }
        if (!TextUtils.isEmpty(s1)) {
            restore(mTemp, 1);
            for (ConditionValueTemp c : mTemp.get(1).values) {
                if (s1.equals(c.value))
                    c.status = ConditionValueTemp.Status.CHECKED;
            }
        }
        if (!TextUtils.isEmpty(s2)) {
            restore(mTemp, 2);
            for (ConditionValueTemp c : mTemp.get(2).values) {
                if (s2.equals(c.value))
                    c.status = ConditionValueTemp.Status.CHECKED;
            }
        }

        update();
        notifyDataSetChanged();
    }

    public boolean isSelected() {
        switch (mProductsForProperty.get(0).conditions.size()) {
            case 1:
                if (TextUtils.isEmpty(mSelected[0])) return false;
                break;

            case 2:
                if (TextUtils.isEmpty(mSelected[0]) || TextUtils.isEmpty(mSelected[1]))
                    return false;
                break;

            case 3:
                if (TextUtils.isEmpty(mSelected[0]) || TextUtils.isEmpty(mSelected[1]) || TextUtils.isEmpty(mSelected[2]))
                    return false;
                break;
        }
        return true;
    }

    public String getProductID() {
        String productID = "";
        for (ProductsForProperty p : mProductsForProperty) {
            boolean flag = true;
            for (int i = 0; i < p.conditions.size(); i++) {
                if (!p.conditions.get(i).conditionValue.equals(mSelected[i])) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                productID = p.productID;
                break;
            }
        }
        return productID;
    }

    public String getToast() {
        switch (mProductsForProperty.get(0).conditions.size()) {
            case 1:
                if (TextUtils.isEmpty(mSelected[0]))
                    return mProductsForProperty.get(0).conditions.get(0).conditionName;
                break;

            case 2:
                if (TextUtils.isEmpty(mSelected[0])) {
                    return mProductsForProperty.get(0).conditions.get(0).conditionName;
                } else if (TextUtils.isEmpty(mSelected[1])) {
                    return mProductsForProperty.get(0).conditions.get(1).conditionName;
                }
                break;

            case 3:
                if (TextUtils.isEmpty(mSelected[0])) {
                    return mProductsForProperty.get(0).conditions.get(0).conditionName;
                } else if (TextUtils.isEmpty(mSelected[1])) {
                    return mProductsForProperty.get(0).conditions.get(1).conditionName;
                } else if (TextUtils.isEmpty(mSelected[2])) {
                    return mProductsForProperty.get(0).conditions.get(2).conditionName;
                }
                break;
        }
        return "";
    }

    public interface OnRefreshListener {
        void onRefreshData();
        void onRefreshSelected(String selected);
    }

    class ViewHolder {
        TextView mNameView;
        TagFlowLayout mTagView;
    }

}
