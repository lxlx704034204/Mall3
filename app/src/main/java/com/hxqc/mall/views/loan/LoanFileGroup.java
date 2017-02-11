//package com.hxqc.mall.views.loan;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
//import com.hxqc.util.DebugLog;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Author: wanghao
// * Date: 2015-10-18
// * FIXME
// * Todo
// */
//public class LoanFileGroup extends android.support.v7.widget.LinearLayoutCompat {
//
//    int mCheckedId = -1;
//    ArrayList< LoanItemFinanceModel > financeModels;
//    LoanItemFinanceModel checkModel;
//    HashMap< Integer, LoanItemFinanceModel > mFinanceMap;
//    WidgetCheckChangeListener mWidgetCheckChangeListener;
//
//    public LoanFileGroup(Context context) {
//        super(context);
//    }
//
//    public LoanFileGroup(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mWidgetCheckChangeListener = new WidgetCheckChangeListener();
//    }
//
//    public LoanFileGroup(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public void setFinanceModels(ArrayList< LoanItemFinanceModel > financeModels, String checkFinanceID) {
//        this.financeModels = financeModels;
//        DebugLog.i("TEST_loan", "setFinanceModels--=: " +checkFinanceID);
//        mFinanceMap = new HashMap<>();
//        for (LoanItemFinanceModel fm : financeModels) {
//            LoanFileItemView loanFileItemView = new LoanFileItemView(getContext());
//            loanFileItemView.setFinanceModel(fm, mWidgetCheckChangeListener);
//
//            int id = loanFileItemView.getId();
//            // generates an id if it's missing
//            if (id == View.NO_ID) {
//                id = fm.hashCode();
//                loanFileItemView.setId(id);
//            }
//
//            if (TextUtils.isEmpty(checkFinanceID)) {
//                if (fm.isChoose) {
//                    checkModel = fm;
//                    mCheckedId = id;
//                    loanFileItemView.setChecked(true);
//                }
//            } else {
//                if (fm.financeID.equals(checkFinanceID)) {
//                    checkModel = fm;
//                    mCheckedId = id;
//                    loanFileItemView.setChecked(true);
//                }
//            }
//
//            addView(loanFileItemView);
//        }
//    }
//
//    public synchronized void check(LoanItemFinanceModel itemFinanceModel, boolean isChecked) {
//        // don't even bother
//        int id = itemFinanceModel.hashCode();
//        if (id != -1 && (id == mCheckedId)) {
//            setCheckedStateForView(mCheckedId, !isChecked);
//            if (!isChecked) {
//                checkModel = itemFinanceModel;
//            } else {
//                checkModel = null;
//            }
//            return;
//        }
//        if (id != -1) {
//            setCheckedStateForView(id, true);
//        }
//        if (mCheckedId != -1) {
//            setCheckedStateForView(mCheckedId, false);
//        }
//        mCheckedId = id;
//        if (!isChecked) {
//            checkModel = itemFinanceModel;
//        } else {
//            checkModel = null;
//        }
//    }
//
//    //获取选择package
//    public LoanItemFinanceModel getCheckModel() {
//        return checkModel;
//    }
//
//    private void setCheckedStateForView(int viewId, boolean checked) {
//        View checkedView = findViewById(viewId);
//        if (checkedView != null && checkedView instanceof LoanFileItemView) {
//            ((LoanFileItemView) checkedView).setChecked(checked);
//        }
//    }
//
//
//    public interface LoanFinanceWidgetCheckChangeListener {
//        void onCheckedChanged(LoanItemFinanceModel model, boolean isChecked);
//    }
//
//
//    class WidgetCheckChangeListener implements LoanFinanceWidgetCheckChangeListener {
//
//        @Override
//        public void onCheckedChanged(LoanItemFinanceModel model, boolean isChecked) {
//            check(model, isChecked);
//        }
//    }
//
//}
