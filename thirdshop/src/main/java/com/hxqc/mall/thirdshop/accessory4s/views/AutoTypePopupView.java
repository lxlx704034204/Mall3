package com.hxqc.mall.thirdshop.accessory4s.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DisplayTools;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 17
 * FIXME
 * Todo 带弹窗的车类型选择控件
 */

public class AutoTypePopupView extends RelativeLayout implements ExpandableListView.OnChildClickListener {

    private PopupWindow mPopupWindow;
    private PinnedHeaderExpandableListView mExpandableListView;
    private ExpandableListView.OnChildClickListener mChildClickListener;
    private RequestFailView mRequestFailView;
    private SideBar sideBarView;
    private TextView mTextView;
    private PopupWindowListener mPopupWindowListener;
    public interface PopupWindowListener {
        void onDismiss();
    }

    public AutoTypePopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoTypePopupView);
        CharSequence mText = typedArray.getText(R.styleable.AutoTypePopupView_auto_type_popup_view_text);
        int mTextColor = typedArray.getColor(R.styleable.AutoTypePopupView_auto_type_popup_view_textColor, getResources().getColor(R.color.text_color_subheading));
        int mTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoTypePopupView_auto_type_popup_view_textSize, 12);
        int mDrawableRight = typedArray.getResourceId(R.styleable.AutoTypePopupView_auto_type_popup_view_drawableRight, 0);
        int mDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.AutoTypePopupView_auto_type_popup_view_drawablePadding, 0);

        typedArray.recycle();

        mTextView = new TextView(context, attrs);
        mTextView.setText(mText);
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(DisplayTools.px2sp(context, mTextSize));
        if (mDrawableRight != 0) {
            Drawable drawableRight = getResources().getDrawable(mDrawableRight);
            drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
            mTextView.setCompoundDrawables(null, null, drawableRight, null);
        }
        if (mDrawablePadding != 0) {
            mTextView.setCompoundDrawablePadding(mDrawablePadding);
        }
        addView(mTextView);

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_brand, null);

        mPopupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, 800);
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.brand_list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.brand_view);
        sideBarView = (SideBar) view.findViewById(R.id.brand_sidebar);

        sideBarView.setOnTouchingLetterChangedListener(
                new SideBar.OnTouchingLetterChangedListener() {
                    @Override
                    public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                        if (index == 0) {
                            mExpandableListView.smoothScrollToPosition(0);
                        } else {
                            mExpandableListView.setSelectedGroup(index - 1);
                        }

                    }

                });
    }

    public PinnedHeaderExpandableListView getExpandableListView() {
        return mExpandableListView;
    }

    /**
     * @param id
     */
    public void setDrawableRight(int id) {
        Drawable drawableRight = getResources().getDrawable(id);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        mTextView.setCompoundDrawables(null, null, drawableRight, null);
    }

    /**
     * @param isPress
     */
    public void switchBackground(boolean isPress) {
        if(isPress) {
            this.setBackgroundColor(getResources().getColor(R.color.gray_yjf));
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        mExpandableListView.setAdapter(adapter);
    }

    /**
     * @param isPress
     */
    public void switchDrawableRight(boolean isPress) {
        Drawable drawableRight = null;
        if (isPress) {
            drawableRight = getResources().getDrawable(R.drawable.ic_cbb_arrow_up);
        } else {
            drawableRight = getResources().getDrawable(R.drawable.ic_cbb_arrow_down);
        }
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        mTextView.setCompoundDrawables(null, null, drawableRight, null);
    }

    /**
     * @param childClickListener
     */
    public void setChildClickListener(ExpandableListView.OnChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    /**
     * @param l
     */
    public void setOnPopupWindowDismissListener(PopupWindowListener l) {
        this.mPopupWindowListener = l;
    }

    /**
     * 创建边侧导航
     *
     * @param brandGroups
     */
    public void initSideTag(List<BrandGroup> brandGroups) {//
        String[] tag = new String[brandGroups.size()];
        for (int i = 0; i < brandGroups.size(); i++) {
            tag[i] = brandGroups.get(i).groupTag;
        }
        sideBarView.setSideTag(tag);
    }

    protected Brand brand;

    /**
     * 计算位置
     *
     * @param brandGroups
     */
    public void aa(List<BrandGroup> brandGroups) {
        int groupCount = 0;
        int chileCount;
        for (BrandGroup brandGroup : brandGroups) {
            ++groupCount;
            chileCount = 0;
            for (Brand brand1 : brandGroup.group) {
                ++chileCount;
                if (brand.equals(brand1)) {
                    int groupNum = groupCount - 2;
                    if (groupNum < 0) {
                        groupNum = 1;
                    }
                    mExpandableListView.setSelectedChild(groupNum, chileCount, true);
                    return;
                }
            }
        }
    }

    /**
     * 显示
     */
    public void showList(final Activity context, View view) {
        if (mPopupWindow != null) {
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopupWindow.setFocusable(true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.showAsDropDown(view);
                mPopupWindow.update();
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mPopupWindowListener.onDismiss();
                    }
                });
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (mChildClickListener != null) {
            mChildClickListener.onChildClick(parent, v, groupPosition, childPosition, id);
        }
        return false;
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
