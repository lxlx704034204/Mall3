package com.hxqc.mall.views.recharge;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.interfaces.TextCompleteListener;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.widget.GridViewNoSlide;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * Author:李烽
 * Date:2015-07-30
 * FIXME
 * Todo 选择充值的金额的
 */
public class ChooseNumberView extends LinearLayout implements TextView.OnEditorActionListener {
    private EditText inputEt;
    private GridViewNoSlide gridView;
    private ArrayList<Integer> txts;
    private BaseAdapter adapter;


    private int max = -1;
    private OnNumberListener onNumberListener;

    public ChooseNumberView(Context context) {
        this(context, null);
    }

    public ChooseNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_choose_sum, this);
        txts = new ArrayList<>();
        inputEt = (EditText) findViewById(R.id.charge_input_et);
        inputEt.setOnEditorActionListener(this);
        gridView = (GridViewNoSlide) findViewById(R.id.number_choose);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        adapter = new NumberAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputEt.clearFocus();
                inputEt.setText(txts.get(position) + "");
                if (onNumberListener != null)
                    onNumberListener.onNumberChange((float) (txts.get(position)));
            }
        });
        inputEt.addTextChangedListener(new TextCompleteListener() {
            @Override
            public void onCompleteText(Editable s) {
                if (s.toString().equals("."))
                    return;
                if (max != -1)
                    if (!TextUtils.isEmpty(s.toString()))
                        if (Float.parseFloat(s.toString()) > max) {
                            //超过最大值的情况
                            inputEt.setText(max + "");
                        }
                if (onNumberListener != null && !TextUtils.isEmpty(s.toString()))
                    onNumberListener.onNumberChange(Float.parseFloat(s.toString()));
            }
        });
        inputEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //将所选的取消
                    int selectedItemPosition = gridView.getCheckedItemPosition();
                    gridView.setItemChecked(selectedItemPosition, false);
                }
            }
        });
        /*测试数据*/
//        ArrayList<Integer> test = new ArrayList<>();
//        test.add(100);
//        test.add(1000);
//        test.add(500);
//        test.add(200);
//        test.add(2000);
//        test.add(5000);
//        test.add(10000);
//        test.add(20000);
//        test.add(50000);
//        setTxts(test);
    }

    public void setOnNumberListener(OnNumberListener onNumberListener) {
        this.onNumberListener = onNumberListener;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public float getNumber() {
        String s = inputEt.getText().toString();
        if (!TextUtils.isEmpty(s))
            return Float.parseFloat(s);
        else return -1;
    }

    public EditText getEditText() {
        return inputEt;
    }

    public void setTxts(ArrayList<Integer> numbers) {
        if (numbers == null || numbers.size() == 0) {
            return;
//            txts.add(100);
//            txts.add(200);
//            txts.add(500);
//            txts.add(1000);
//            txts.add(2000);
//            txts.add(5000);
        } else {
            txts.clear();
            txts.addAll(numbers);
            adapter.notifyDataSetChanged();
            gridView.setVisibility(VISIBLE);
            gridView.setItemChecked(0, true);
            inputEt.setText(numbers.get(0) + "");
        }
    }

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    private OnEditListener onEditListener;

    public interface OnEditListener {
        void onEditDone();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            OtherUtil.closeSoftKeyBoard(getContext(), inputEt);
            if (onEditListener != null)
                onEditListener.onEditDone();
            return true;
        }
        return false;

    }

    public interface OnNumberListener {
        void onNumberChange(float number);
    }

    class NumberAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return txts == null ? 0 : txts.size();
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
            ChargeNumberItem item = new ChargeNumberItem(getContext());
            item.setNumber(txts.get(position));
            return item;
        }
    }
}
