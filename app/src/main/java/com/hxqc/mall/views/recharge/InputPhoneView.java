package com.hxqc.mall.views.recharge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.core.model.recharge.PrepaidHistory;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-01
 * FIXME
 * Todo 填写充值人的信息
 */
public class InputPhoneView extends LinearLayout implements AdapterView.OnItemClickListener,
        TextView.OnEditorActionListener, View.OnClickListener {
    private LinearLayout listContainer;
    private EditText phoneNumberEt;
    private ImageView arrow;
    private ListView chargeRecordList;
    private ArrayList<PrepaidHistory> prepaidHistories;
    private ListAdapter adapter;

    private String name = "";

    private boolean showHistory = false;


    public InputPhoneView(Context context) {
        this(context, null);
    }

    public InputPhoneView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputPhoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_choose_phone_number, this);
        listContainer = (LinearLayout) findViewById(R.id.list_container);
        phoneNumberEt = (EditText) findViewById(R.id.phone_number_et);
        phoneNumberEt.setOnEditorActionListener(this);
        arrow = (ImageView) findViewById(R.id.arrow);
        findViewById(R.id.arrow_container).setOnClickListener(this);
        chargeRecordList = (ListView) findViewById(R.id.charge_record_list);
        prepaidHistories = new ArrayList<>();
        adapter = new ListAdapter();
        chargeRecordList.setAdapter(adapter);
        chargeRecordList.setOnItemClickListener(this);
    }

    /**
     * 加载充值历史记录
     */
    public void addHistory(ArrayList<PrepaidHistory> prepaidHistories) {
        //加载充值历史记录
        this.prepaidHistories.addAll(prepaidHistories);
        if (prepaidHistories.size() == 0)
            showHistory = false;
        changeListState(showHistory);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (prepaidHistories != null)
            if (prepaidHistories.size() > 2) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据数据改变ui
     */
    private void changeListState(boolean isShow) {
//        listContainer.setVisibility(prepaidHistories.size() == 0 ? INVISIBLE : VISIBLE);
        listContainer.setVisibility(isShow ? VISIBLE : INVISIBLE);
        arrow.setImageResource(isShow ? R.drawable.ic_triangle :
                R.drawable.ic_cbb_arrow);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PrepaidHistory history = prepaidHistories.get(position);
        phoneNumberEt.setText(history.phoneNumber);
        name = history.name;
    }

    /**
     * 获取已输入的数字
     *
     * @return
     */
    public String phoneNumber() {
        return phoneNumberEt.getText().toString().trim();
    }


    public String name() {
        return name;
    }

    public void setPhoneNumber(String number) {
        phoneNumberEt.setText(number);
        phoneNumberEt.setSelection(number.length());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (onEditNextListener != null)
                onEditNextListener.onEditNext();
            return true;
        }
        return false;
    }

    public void setOnEditNextListener(OnEditNextListener onEditNextListener) {
        this.onEditNextListener = onEditNextListener;
    }

    private OnEditNextListener onEditNextListener;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.arrow_container) {
            showHistory = !showHistory;
            changeListState(showHistory);
        }
    }

    public interface OnEditNextListener {
        void onEditNext();
    }

    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return prepaidHistories == null ? 0 : prepaidHistories.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_prepaid_history_list, null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name);
            TextView phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
//            if (position<prepaidHistories.size()) {
            nameTv.setText(prepaidHistories.get(position).name);
            phoneNumber.setText(prepaidHistories.get(position).phoneNumber);
//            }else {
//                nameTv.setText("李烽");
//                phoneNumber.setText("13071207605");
//            }
            return convertView;
        }
    }
}

