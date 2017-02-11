package com.hxqc.pay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.model.PaidAmountModel;
import com.hxqc.pay.model.PayOnlineRequest;
import com.hxqc.pay.model.PayPartBackDataModel;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.pay.util.MyTextUtils;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Created by CPR062 on 2015/3/10.
 * <p/>
 * 付款列表adapter
 */
public class PayMethodAdapterHolder extends RecyclerView.Adapter< RecyclerView.ViewHolder > {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_FOOTER = 1;
    final int VIEW_TYPE_HEADER = 2;
    Context context;
    String unpaid;
    String paid;
    String subscription = "0.00";//订金金额
    /**
     * 2015.9.28
     * 是否能首次支付
     */
    boolean is_first_pay = false;
    boolean is_first_pay_can = false;

//    TextView tempTVWarn;

    OnItemClickListener mItemClickListener;
    private ArrayList< PayPartBackDataModel > cacheListData;
    private int flag;

    /**
     * 2015.9.28
     *
     * @return 返回提示字符串
     */
    private String getWarnStr() {
        return "首次支付金额不得小于" + subscription + "元";
    }

    public PayMethodAdapterHolder(ArrayList< PayPartBackDataModel > cacheListData, int flag, PaidAmountModel model) {
        this.cacheListData = cacheListData;
        this.flag = flag;
        this.unpaid = model.unpaid;
        this.paid = model.paid;
        if (!TextUtils.isEmpty(model.subscription))
            this.subscription = model.subscription;

        is_first_pay = paid.equals("0.00");
        if (!is_first_pay) {
            is_first_pay_can = true;
        }


//        for (int i = 0; i < cacheListData.size(); i++) {
//            DebugLog.i("test_pay", " <_ PayMethodAdapter _> " + cacheListData.get(i).toString());
//        }
        if (unpaid.equals("0.00")) {
            for (int i = 0; i < cacheListData.size() - 1; i++) {
                if (cacheListData.get(i).pay_status_flag == 0) {
                    cacheListData.remove(i);
                }
            }
        }
//        DebugLog.i("unpaid", unpaid + " PayMethodAdapterHolder.......... ");
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        if ((position == cacheListData.size() - 1) && cacheListData.size() > 1) {
            return VIEW_TYPE_FOOTER;
        } else if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView;

        if (viewType == VIEW_TYPE_FOOTER) {

            sView = mInflater.inflate(R.layout.pay_online_amount_footer, parent, false);
            return new ViewHolderFooter(sView);

        } else if (viewType == VIEW_TYPE_HEADER) {

            sView = mInflater.inflate(R.layout.item_choose_pay_method_recyclerview, parent, false);
            return new ViewHolderHead(sView);

        } else if (viewType == VIEW_TYPE_ITEM) {
            sView = mInflater.inflate(R.layout.item_choose_pay_method_recyclerview_center, parent, false);
            return new ViewHolderItem(sView);
        }

        return null;
    }

    // 支付條目操作
    private void sortOperationForClick(View v, int p, String bankTypeStr, String moneyStr) {

        if (v.getId() == R.id.tv_del) {
            mItemClickListener.onItemClick(v, p, ConstantValue.PAY_METHOD_ITEM_DEL, null);
        } else if (v.getId() == R.id.mcit_get_bank_rcv) {
            mItemClickListener.onItemClick(v, p, ConstantValue.PAY_METHOD_ITEM_SELECT_BANK, null);
        } else if (v.getId() == R.id.tv_go_to_pay) {
            if (TextUtils.isEmpty(bankTypeStr)) {
                ToastHelper.showYellowToast(v.getContext(), "请选择支付方式");
            } else {
                if (TextUtils.isEmpty(moneyStr)) {
                    ToastHelper.showYellowToast(v.getContext(), "请输入付款金额");
                } else {
                    PayOnlineRequest payOnlineRequest = new PayOnlineRequest();
                    payOnlineRequest.money = moneyStr;
                    if (is_first_pay_can) {
                        mItemClickListener.onItemClick(v, p, ConstantValue.GO_TO_PAY, payOnlineRequest);
                    } else {
                        ToastHelper.showYellowToast(v.getContext(), getWarnStr());
                    }
                }
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PayPartBackDataModel data = cacheListData.get(position);

        if (position == 0) {
            ViewHolderHead head = (ViewHolderHead) holder;
            head.mDel.setVisibility(View.INVISIBLE);

            if (data.pay_status_flag == 0) {
                head.bankType.setText("");
                moneyOperat(head.mMoney, head.mUpperMoney, head.mWarnFirstTimeView);
                head.bankType.setEnabled(true);
                head.bankType.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        context.getResources().getDrawable(R.drawable.ic_cbb_arrow), null);
                head.mMoney.setEnabled(true);
                head.mDel.setVisibility(View.INVISIBLE);
                head.mPay.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R
                        .drawable.orderstep2_list_arrow), null);
                head.mPay.setText("去支付 ");
                head.mPay.setClickable(true);
                head.mPay.setEnabled(true);
                head.mPay.setTextColor(context.getResources().getColor(R.color.text_color_orange));
//                head.mMoney.setTextColor(context.getResources().getColor(R.color.title_and_main_text));
//                head.bankType.setTextColor(context.getResources().getColor(R.color.title_and_main_text));

            } else if (data.pay_status_flag == 1) {
                head.bankType.setText(data.bank_title);
                head.bankType.setEnabled(false);
                head.bankType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                head.mMoney.setText("¥" + data.amount);
                upperMoney(data.amount, head.mUpperMoney);
                head.mMoney.setEnabled(false);
                head.mDel.setVisibility(View.INVISIBLE);
                head.mPay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                head.mPay.setGravity(Gravity.CENTER_HORIZONTAL);
                head.mPay.setText("已支付");
                head.mPay.setClickable(false);
                head.mPay.setEnabled(false);
                head.mPay.setTextColor(context.getResources().getColor(R.color.white));


//                head.mMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
//                head.bankType.setTextColor(context.getResources().getColor(R.color.text_gray));
            }

        } else if ((position == cacheListData.size() - 1) && cacheListData.size() > 1) {
            ViewHolderFooter footer = (ViewHolderFooter) holder;

            if (unpaid.equals("0.00")) {
                footer.mAdd.setEnabled(false);
                footer.mAdd.setText("已完成支付");
                footer.mAdd.setTextColor(context.getResources().getColor(R.color.font_green));
                footer.mAdd.setCompoundDrawables(null, null, null, null);
            } else {
                footer.mAdd.setVisibility(View.VISIBLE);
            }

            if (flag == PayConstant.PAY_ONLY_ONLINE_PAID) {
                footer.mCompleteInfo.setText("完成支付");
                footer.mCompleteInfo.setVisibility(View.INVISIBLE);
            } else {
                footer.mCompleteInfo.setText("完善信息");
                footer.mCompleteInfo.setVisibility(View.VISIBLE);
            }

        } else {
            ViewHolderItem item = (ViewHolderItem) holder;

            if (data.pay_status_flag == 0) {
                item.bankType.setText("");
                item.mMoney.setText("");
//                item.mMoney.setTextColor(context.getResources().getColor(R.color.title_and_main_text));
//                item.bankType.setTextColor(context.getResources().getColor(R.color.title_and_main_text));
                moneyOperat(item.mMoney, item.mUpperMoney, item.mWarnFirstTimeView);
                item.bankType.setEnabled(true);
                item.bankType.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable
                        (R.drawable.ic_cbb_arrow), null);
                item.mMoney.setEnabled(true);
                item.mDel.setVisibility(View.VISIBLE);
                item.mPay.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R
                        .drawable.orderstep2_list_arrow), null);
                item.mPay.setText("去支付 ");
                item.mPay.setClickable(true);
                item.mPay.setEnabled(true);
                item.mPay.setTextColor(context.getResources().getColor(R.color.text_color_orange));
            } else if (data.pay_status_flag == 1) {
                item.bankType.setText(data.bank_title);
                item.bankType.setEnabled(false);
                item.bankType.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                item.mMoney.setText("¥" + data.amount);
//                item.mMoney.setTextColor(context.getResources().getColor(R.color.text_gray));
//                item.bankType.setTextColor(context.getResources().getColor(R.color.text_gray));
                item.mMoney.setEnabled(false);
                upperMoney(data.amount, item.mUpperMoney);
                item.mDel.setVisibility(View.INVISIBLE);
                item.mPay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                item.mPay.setGravity(Gravity.CENTER_HORIZONTAL);
                item.mPay.setText("已支付");
                item.mPay.setClickable(false);
                item.mPay.setEnabled(false);
                item.mPay.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    public boolean isNoPaid() {
        return paid.equals("0.00");
    }

    //金额变化与设置
    public void moneyOperat(final EditText mMoney, final TextView mUpperMoney, final TextView warnView) {

        mMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String moneyStr = mMoney.getText().toString();


                if (!TextUtils.isEmpty(moneyStr)) {
                    if ((".").equals(moneyStr.substring(0, 1))) {
                        mMoney.setText("");
                        mMoney.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    } else {
                        mMoney.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable
                                .ic_money), null, null, null);
                        mMoney.setCompoundDrawablePadding(6);
                        DebugLog.i("unpaid", unpaid + " p_adapter --- ");
                        double unpaid_m = Double.parseDouble(unpaid);

                        if (moneyStr.contains("¥")) {
                            moneyStr = moneyStr.substring(1);

                        }
                        DebugLog.i("unpaid", unpaid_m + " p_adapter --- " + moneyStr);
                        double moneyDouble = Double.parseDouble(moneyStr);
                        DebugLog.i("unpaid", unpaid_m + " p_adapter --- " + moneyDouble);
                        if (moneyDouble > unpaid_m) {
//                            ToastHelper.showRedToast(context, "最多还能付：" + unpaid_m);
                            mMoney.setText(String.format("%.2f", unpaid_m));
                            mMoney.setSelection(mMoney.getText().toString().length());
                        }
                    }
                } else {
                    mMoney.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mUpperMoney.setText(" ");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String moneyStr = mMoney.getText().toString();

                if (!TextUtils.isEmpty(moneyStr)) {
                    if (("build/intermediates/exploded-aar/com.android.support/support-v4/21.0.3/res").equals
                            (moneyStr.substring(0, 1))) {
                        mMoney.setText("");
                        mMoney.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    } else {
                        if (moneyStr.contains("¥")) {
                            moneyStr = moneyStr.substring(1);
                        }

                        /**
                         * 2015.9.28
                         * 订金提示
                         */
                        if (is_first_pay)
                            changeWarnView(Double.parseDouble(moneyStr), warnView);

                        upperMoney(moneyStr, mUpperMoney);
                    }
                } else {
                    warnView.setVisibility(View.GONE);
                    mUpperMoney.setText(" ");
                }
            }
        });
    }

    private void upperMoney(String moneyStr, TextView mUpperMoney) {
        double moneyDouble = Double.parseDouble(moneyStr);
        String uppercase = MyTextUtils.digitUppercase(moneyDouble);
        mUpperMoney.setText(uppercase);
    }

    /**
     * 2015.9.28
     * 判断是否显示首次支付金额提示
     *
     * @param money
     *         输入的金额
     * @param warnView
     *         提示view
     */
    private void changeWarnView(double money, TextView warnView) {

        double sub = Double.parseDouble(subscription);

        if (money >= 0 && money < sub) {
            is_first_pay_can = false;
            warnView.setVisibility(View.VISIBLE);
            warnView.setText(getWarnStr());
        } else if (money >= sub) {
            is_first_pay_can = true;
            warnView.setVisibility(View.GONE);
        } else {
            is_first_pay_can = false;
            warnView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return cacheListData.size();
    }

    //--------------------
    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type, PayOnlineRequest payOnlineRequest);
    }

    // 支付 中間條目
    public class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageButton mDel;
        public EditText bankType;
        public Button mPay;
        public EditText mMoney;
        public TextView mUpperMoney;
        public TextView mWarnFirstTimeView;

        public ViewHolderItem(View itemView) {
            super(itemView);
            mDel = (ImageButton) itemView.findViewById(R.id.tv_del);
            bankType = (EditText) itemView.findViewById(R.id.mcit_get_bank_rcv);
            mPay = (Button) itemView.findViewById(R.id.tv_go_to_pay);
            mMoney = (EditText) itemView.findViewById(R.id.et_get_money);
            mUpperMoney = (TextView) itemView.findViewById(R.id.tv_upper_money);
            mWarnFirstTimeView = (TextView) itemView.findViewById(R.id.tv_pay_notify_first_time);

            mPay.setOnClickListener(this);
            bankType.setOnClickListener(this);
            mDel.setOnClickListener(this);
            if (is_first_pay) {
                mMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mWarnFirstTimeView.setText(getWarnStr());
                            mWarnFirstTimeView.setVisibility(View.VISIBLE);
                        } else {
                            mWarnFirstTimeView.setVisibility(View.GONE);
                        }
                    }
                });
            }

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                sortOperationForClick(v, getPosition(), bankType.getText().toString(), mMoney.getText().toString());
            }
        }
    }

    // 支付第一個條目
    public class ViewHolderHead extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageButton mDel;
        public EditText bankType;
        public TextView mPay;
        public EditText mMoney;
        public TextView mUpperMoney;
        public TextView mWarnFirstTimeView;

        public ViewHolderHead(View itemView) {
            super(itemView);
            mDel = (ImageButton) itemView.findViewById(R.id.tv_del);
            bankType = (EditText) itemView.findViewById(R.id.mcit_get_bank_rcv);
            mPay = (TextView) itemView.findViewById(R.id.tv_go_to_pay);
            mMoney = (EditText) itemView.findViewById(R.id.et_get_money);
            mUpperMoney = (TextView) itemView.findViewById(R.id.tv_upper_money);
            mWarnFirstTimeView = (TextView) itemView.findViewById(R.id.tv_pay_notify_first_time);

            mPay.setOnClickListener(this);
            bankType.setOnClickListener(this);
            mDel.setOnClickListener(this);
            if (is_first_pay) {
                mMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mWarnFirstTimeView.setText(getWarnStr());
                            mWarnFirstTimeView.setVisibility(View.VISIBLE);
                        } else {
                            mWarnFirstTimeView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                sortOperationForClick(v, getPosition(), bankType.getText().toString(), mMoney.getText().toString());
            }
        }
    }

    // 底部完善信息條目
    public class ViewHolderFooter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mAdd;
        Button mCompleteInfo;

        public ViewHolderFooter(View itemView) {
            super(itemView);
            mAdd = (TextView) itemView.findViewById(R.id.tv_add);
            mAdd.setCompoundDrawablePadding(4);
            mCompleteInfo = (Button) itemView.findViewById(R.id.btn_complete);
            mAdd.setOnClickListener(this);
            mCompleteInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v.getId() == R.id.tv_add) {
                    mItemClickListener.onItemClick(v, getPosition(), ConstantValue.PAY_METHOD_ITEM_ADD, null);
                } else if (v.getId() == R.id.btn_complete) {
                    mItemClickListener.onItemClick(v, getPosition(), ConstantValue.COMPLETE_INFO, null);
                }
            }
        }
    }

}
