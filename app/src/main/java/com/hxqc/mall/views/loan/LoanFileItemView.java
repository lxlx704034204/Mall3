package com.hxqc.mall.views.loan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
import com.hxqc.mall.core.util.ImageUtil;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-10-16
 * FIXME  金融公司item
 * Todo
 */
public class LoanFileItemView extends RelativeLayout implements Checkable
//        , View.OnClickListener
{

    private ImageView bankIconView;
    private ImageView goView;
    private LinearLayout mItemLayoutView;
    private View dividerView;
    LoanItemFinanceModel financeModel;

//    LoanFileGroup.LoanFinanceWidgetCheckChangeListener checkChangeListener;

    boolean isChecked = false;//是否被选中

    public LoanFileItemView(Context context) {
        super(context);
        initView();
    }

    public LoanFileItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loan_file, this);
        RelativeLayout wholeClickView = (RelativeLayout) findViewById(R.id.rl_loan_layout);
        bankIconView = (ImageView) findViewById(R.id.iv_bank_image);
        goView = (ImageView) findViewById(R.id.iv_gogo);
        mItemLayoutView = (LinearLayout) findViewById(R.id.ll_item_content);
        dividerView = findViewById(R.id.v_loan_divider);

//        wholeClickView.setOnClickListener(this);
//        goView.setOnClickListener(this);
    }

    public void setDividerViewVisible(int visible) {
        this.dividerView.setVisibility(visible);
    }

    /**
     * 加入数据
     */
//    public void setFinanceModel(LoanItemFinanceModel financeModel, LoanFileGroup.LoanFinanceWidgetCheckChangeListener checkChangeListener) {
//
//        this.financeModel = financeModel;
//
//        if (financeModel != null) {
//            if (!TextUtils.isEmpty(financeModel.logo)) {
//            }
//
//            if (financeModel.infomation != null) {
//                if (financeModel.infomation.size() > 0) {
//                    for (int i = 0; i < financeModel.infomation.size(); i++) {
//                        LoanContentItemView view = new LoanContentItemView(getContext());
//                        view.setModel(financeModel.infomation.get(i));
//                        if (i+1 == financeModel.infomation.size()){
//                            view.setvUnderlineVisible(View.GONE);
//                        }
//                        mItemLayoutView.addView(view);
//                    }
//                }
//            }
//        }
//
//        this.checkChangeListener = checkChangeListener;
//    }

    public void setFinanceModel(LoanItemFinanceModel financeModel) {

        this.financeModel = financeModel;

        if (financeModel != null) {
            ImageUtil.setImage(getContext(), bankIconView, financeModel.logo);

            if (financeModel.infomation != null) {
                if (financeModel.infomation.size() > 0) {
                    for (int i = 0; i < financeModel.infomation.size(); i++) {
                        LoanContentItemView view = new LoanContentItemView(getContext());
                        view.setModel(financeModel.infomation.get(i));
                        if (i+1 == financeModel.infomation.size()){
                            view.setvUnderlineVisible(View.GONE);
                        }
                        mItemLayoutView.addView(view);
                    }
                }
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        financeModel.isChoose = checked;
        changeBG();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    private void changeBG() {
        if (isChecked) {
            goView.setBackgroundResource(R.drawable.ic_choose_selected);
        } else {
            goView.setBackgroundResource(R.drawable.ic_choose_normal);

        }
    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.rl_loan_layout) {
//            if (checkChangeListener!=null){
//                checkChangeListener.onCheckedChanged(financeModel, isChecked);
//            }
//        } else if (v.getId() == R.id.iv_gogo) {
//            if (checkChangeListener!=null){
//                checkChangeListener.onCheckedChanged(financeModel, isChecked);
//            }
//        }
//    }
}
