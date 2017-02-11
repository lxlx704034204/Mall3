package com.hxqc.mall.views.auto;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import hxqc.mall.R;


/**
 * Author: HuJunJie
 * Date: 2015-04-09
 * FIXME
 * Todo
 *
 * @attr ref R.styleable#AutoDetailTab_label
 * @attr ref R.styleable#AutoDetailTab_tagDrawable
 * @attr ref R.styleable#AutoDetailTab_iconDrawable
 */
public class AutoDetailTabView extends LinearLayout {
    public TextView mLabelView;
    public ImageView mIconView;
    public ImageView mTypeIconView;
    String mLabelString;
    int mTagDrawable;
    int mIconDrawable;

    View mContentView;
    View mGroupView;

    public AutoDetailTabView(Context context) {
        super(context);
    }

    public AutoDetailTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AutoDetailTab);
        mLabelString = typedArray.getString(R.styleable.AutoDetailTab_tabLabel);
        mTagDrawable = typedArray.getResourceId(R.styleable.AutoDetailTab_tabTagDrawable, 0);
        mIconDrawable = typedArray.getResourceId(R.styleable.AutoDetailTab_tabIconDrawable, 0);
        typedArray.recycle();

        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_auto_tab, this);
        mLabelView = (TextView) findViewById(R.id.auto_tab_label);
        mIconView = (ImageView) findViewById(R.id.auto_tab_icon);
        mTypeIconView = (ImageView) findViewById(R.id.auto_tab_type_icon);
        mGroupView = findViewById(R.id.auto_tab_group);
        mLabelView.setText(mLabelString);
        if (mIconDrawable == 0) {
            mIconView.setVisibility(View.GONE);
        } else {
            mIconView.setImageResource(mIconDrawable);
        }
        if (mTagDrawable == 0) {
            mTypeIconView.setVisibility(View.GONE);
        } else {
            mTypeIconView.setImageResource(mTagDrawable);
        }


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewWithTag("content");
        contentViewOnClick();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

//        if(mContentView!=null){
//            mContentView.layout(0, mGroupView.getHeight(), mContentView.getWidth(), mContentView.getHeight());
//        }
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
        contentViewOnClick();
    }

    /**
     * 折叠内容
     */
    private void contentViewOnClick() {
        if (mContentView != null) {
            mGroupView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContentView.isShown()) {
                        mContentView.setVisibility(View.GONE);
                        mTypeIconView.setImageResource(R.drawable.ic_cbb_arrow_down);
                    } else {
                        mContentView.setVisibility(View.VISIBLE);
                        mTypeIconView.setImageResource(R.drawable.ic_cbb_arrow_up);
                    }
                }
            });
        }
    }


    public AutoDetailTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

}
