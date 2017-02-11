package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.views.RecyclerViewHeader;
import com.hxqc.mall.thirdshop.R;

import java.util.ArrayList;

/**
 * 说明:
 * <p/>
 * author: 吕飞
 * since: 2015-05-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class EditTestActivity extends BackActivity {
    TextView top;
    RecyclerView listView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> strings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);
        RecyclerViewHeader header = RecyclerViewHeader.fromXml(this, R.layout.layout_confirm_order_header);
        listView = (RecyclerView) findViewById(R.id.list);
        top = (TextView) findViewById(R.id.top);
        for (int i = 0; i < 20; i++) {
            strings.add("");
        }
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
        listView.setHasFixedSize(true);
        header.attachTo(listView);
        listView.setAdapter(new EditAdapter());
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top.setText(strings.toString());
            }
        });
    }

    class EditAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
            return new ViewHolder(v, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).mTextView.setText("打得到都是" + position);
            ((ViewHolder) holder).myCustomEditTextListener.updatePosition(position);
            ((ViewHolder) holder).mEditView.setText(strings.get(position));
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            EditText mEditView;
            TextView mTextView;
            MyCustomEditTextListener myCustomEditTextListener;

            public ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
                super(v);
                mEditView = (EditText) v.findViewById(R.id.edit);
                mTextView = (TextView) v.findViewById(R.id.text);
                this.myCustomEditTextListener = myCustomEditTextListener;
                mEditView.addTextChangedListener(myCustomEditTextListener);
            }
        }

        private class MyCustomEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                strings.set(position, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        }
    }
}
