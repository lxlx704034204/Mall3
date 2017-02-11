package com.hxqc.mall.usedcar.views.SellCar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.activity.SellCarActivity;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.views.RemarkView;


/**
 * @Author : 吕飞
 * @Since : 2016-12-15
 * FIXME
 * Todo 卖车页面，车主自述的EditText
 */
public class SellCarReadme extends RemarkView {
    public TextView mReadmeView;
    public SellCarReadme(Context context) {
        super(context);
    }

    public SellCarReadme(Context context, AttributeSet attrs) {
        super(context, attrs);
        mReadmeView= (TextView) findViewById(R.id.readme);
        mReadmeView.setVisibility(VISIBLE);
        mRemarkView.setVisibility(GONE);
        mTitleView.setText("车主自述：");
        mCountView.setVisibility(GONE);
        mRemarkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UsedCarActivitySwitcher.toReadme((SellCarActivity) getContext(), getText());
            }
        });
    }
    public void setText(String text){
        mReadmeView.setText(text);
    }
    public String getText(){
        return mReadmeView.getText().toString();
    }
}
