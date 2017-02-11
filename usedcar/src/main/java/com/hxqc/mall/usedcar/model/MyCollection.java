package com.hxqc.mall.usedcar.model;

import android.view.View;
import android.widget.ImageView;

import com.hxqc.mall.usedcar.R;


/**
 * @Author : 钟学东
 * @Since : 2015-08-24
 * FIXME
 * Todo
 */
public class MyCollection extends UsedCarBase{

    public String state;//0：普通；1：已售；2：已下架

    public void showState(ImageView imageView) {
        if(state.equals("0")){
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            if(state.equals("1")){
                imageView.setImageResource(R.mipmap.my_collection_sold);
            }else {
                imageView.setImageResource(R.mipmap.my_collection_offtheshelf);
            }
        }
    }
}
