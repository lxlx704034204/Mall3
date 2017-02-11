package com.hxqc.newenergy.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:  wh
 * Date:  2016/4/19
 * FIXME
 * Todo
 */
public class HTMLParseLayoutPieceView extends RelativeLayout {

    TextView numberView;
    TextView contentView;

    public HTMLParseLayoutPieceView(Context context) {
        super(context);
        initView();
    }

    public HTMLParseLayoutPieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.ev_html_list_item_view, this);
        numberView = (TextView) findViewById(R.id.p_item_num);
        contentView = (TextView) findViewById(R.id.p_item_text);
    }

    public void setValues(String num,String str){
        if (!TextUtils.isEmpty(num)){

            if (numberView!=null){
                numberView.setText(num);
            }

            if (contentView!=null){
                contentView.setText(str);
            }

        }
    }

}
