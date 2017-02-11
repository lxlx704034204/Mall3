package com.hxqc.mall.paymethodlibrary.view.input;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hxqc.mall.paymethodlibrary.R;
import com.hxqc.mall.paymethodlibrary.adapter.PayInputAdapter;

/**
 * Author: wanghao
 * Date: 2016-03-15
 * FIXME
 * Todo
 */
public class PayNumberKeyboard extends LinearLayout implements AdapterView.OnItemClickListener{

    OnKeyboardClickListener keyboardClickListener;
    private Context ctx;
    private PayInputAdapter adapter;

    public PayNumberKeyboard(Context context) {
        super(context);
        this.ctx = context;
        init();
    }

    public PayNumberKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        init();
    }

    public void setKeyboardClickListener(OnKeyboardClickListener keyboardClickListener) {
        this.keyboardClickListener = keyboardClickListener;
    }

    private void init() {
        LayoutInflater.from(ctx).inflate(R.layout.view_number_keyboard, this);
        GridView number = (GridView) findViewById(R.id.numbers);
        adapter = new PayInputAdapter(ctx);
        number.setAdapter(adapter);
        number.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        String item = adapter.getItem(position);
        keyboardClickListener.OnKeyClick(item);
    }

    interface OnKeyboardClickListener{
        void OnKeyClick(String n);
    }
}
