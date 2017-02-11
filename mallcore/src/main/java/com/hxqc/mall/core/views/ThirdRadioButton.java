package com.hxqc.mall.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.hxqc.mall.core.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-01
 * FIXME
 * Todo
 */
public class ThirdRadioButton extends RadioButton {
    OnThirdStatusChangeListener onThirdStatusChangeListener;
    int status = 0;//状态计算

    public ThirdRadioButton(Context context) {
        super(context);
        setOnclick();
    }

    private void setOnclick() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBackground((status++) % 2 + 1);
            }
        });
    }

    private void changeBackground(int status){
        switch (status) {
            case 0:
                if(onThirdStatusChangeListener!=null){
                    onThirdStatusChangeListener.onThirdStatusChange(this,false,0);
                }
                setBackgroundResource(R.drawable.ic_3type_sort);
                break;
            case 1:
                if(onThirdStatusChangeListener!=null){
                    onThirdStatusChangeListener.onThirdStatusChange(this,true,1);
                }
                setBackgroundResource(R.drawable.ic_3type_sortdown);
                break;
            case 2:
                if(onThirdStatusChangeListener!=null){
                    onThirdStatusChangeListener.onThirdStatusChange(this,true,2);
                }
                setBackgroundResource(R.drawable.ic_3type_sortup);
                break;
        }
    }

    public ThirdRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnclick();

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    status = 0;
                    changeBackground(status);
                }
            }
        });
    }

    public void setOnThirdStatusChangeListener(OnThirdStatusChangeListener onThirdStatusChangeListener) {
        this.onThirdStatusChangeListener = onThirdStatusChangeListener;
    }

    public interface OnThirdStatusChangeListener {
        /**
         *  状态改变
         * @param buttonView
         * @param isChecked
         * @param status 0未选中  1向上  2向下
         */
        void onThirdStatusChange(CompoundButton buttonView, boolean isChecked, int status);
    }

}
