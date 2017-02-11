package com.hxqc.mall.core.views;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

/**
 * Author: HuJunJie
 * Date: 2015-04-07
 * FIXME
 * Todo
 */
public class SpinnerPopWindow extends RelativeLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListAdapter mListAdapter;
    AdapterView.OnItemClickListener mOnItemClickList;
    ViewGroup mContentView;//显示选择内容
    ListPopupWindow popupWindow;
    int mPopHeight=500;

    public SpinnerPopWindow(Context context) {
        super(context);
    }


    public SpinnerPopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        popupWindow = new ListPopupWindow(getContext());
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setModal(true);//设置是否是模式
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(this);
        int spinner_content = getResources().getIdentifier("spinner_content", "id", getContext().getPackageName());
        mContentView = (ViewGroup) findViewById(spinner_content);

    }

    public void setAdapter(ListAdapter listAdapter) {
        this.mListAdapter = listAdapter;
        popupWindow.setAdapter(mListAdapter);
        popupWindow.setOnItemClickListener(this);
    }

    public void setOnItemClickList(AdapterView.OnItemClickListener onItemClickList) {
        this.mOnItemClickList = onItemClickList;
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        setCurrentItem(position);
        if (mOnItemClickList != null){
            mOnItemClickList.onItemClick(parent, view, position, id);
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 选择项
     *
     */
    public boolean setCurrentItem(int position) {
        if (mContentView == null || mListAdapter == null) return true;
        mContentView.removeAllViews();
        mContentView.addView(mListAdapter.getView(position, null, null), 0);
        return false;
    }

    public void clearValue() {
        mContentView.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        showPopupWindow(v);
    }

    public void showPopupWindow(View clickView) {
        int mPopWidth = clickView.getWidth() + 24;
        popupWindow.setWidth(mPopWidth);

        if (mListAdapter != null && mListAdapter.getCount() > 3) {
            popupWindow.setHeight(mPopHeight);
        } else {
            popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setAnchorView(this);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        popupWindow.show();

    }

    public void setPopHeight(int mPopHeight) {
        this.mPopHeight = mPopHeight;
    }
}
