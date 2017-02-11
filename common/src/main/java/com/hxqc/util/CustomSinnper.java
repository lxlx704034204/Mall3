package com.hxqc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class CustomSinnper extends TextView {

    private final CustomSinnper topButton;
    private final Context mContext;
    protected PopupWindow popup = null;
    protected ListView mListView;
    private OnItemClickListener changListener;
    private OnClickListener mOnClickListener;
    private PopupWindow.OnDismissListener mOnDismissListener;

    /**
     * TextView topButton to addView
     *
     * @param context
     * @param attrs
     */
    @SuppressLint({"NewApi", "Recycle"})
    public CustomSinnper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        topButton = this;
        initView(mContext);

    }

    private void initView(final Context c) {
        // click button text on to popupWindow
        topButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mOnClickListener != null)
                    mOnClickListener.onClick(arg0);
                initPopupWindow(c);
            }
        });

        mListView = new ListView(c);
        mListView.setScrollbarFadingEnabled(false);
        mListView.setBackgroundResource(android.R.color.white);
        mListView.setCacheColorHint(0);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                topButton.setText(obj.toString());
                dismiss();
                if (changListener != null) {
                    changListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    protected void initPopupWindow(Context context) {
        if (popup == null) {
            popup = new PopupWindow(mContext);
            popup.setWidth(topButton.getWidth());
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setFocusable(true);
            int hight = mListView.getCount() * ScreenUtil.dip2px(context, 30);
            popup.setHeight(hight < 400 ? hight : 400);
            popup.setOutsideTouchable(true);
            popup.setContentView(mListView);
            if (mOnDismissListener != null)
                popup.setOnDismissListener(mOnDismissListener);
        }
        if (!popup.isShowing()) {
            popup.showAsDropDown(topButton);
        }
    }


    protected void dismiss() {
        if (popup.isShowing()) {
            popup.dismiss();
        }
    }

    private void setTopText(ListAdapter adapter) {
        String text;
        if (adapter.getCount() <= 0) {
            text = "select";
            topButton.setText(text);
        } else if (topButton.getText().toString().equals("")) {
            text = (String) adapter.getItem(0);
            topButton.setText(text);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (mListView == null) {
            throw new NullPointerException("Listview null");
        }
        mListView.setAdapter(adapter);
        setTopText(adapter);
    }

    public void setOnItemSeletedListener(OnItemClickListener listener) {
        this.changListener = listener;
    }

    public void setOnSinnperClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }


}
