package com.hxqc.mall.core.views.vedit.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  wh
 * Date:  2016/11/2
 * FIXME
 * Todo
 */

public class VWholeEditManager {
    final static String Tag = "testVet";
    private Activity mA;
    private List<EditTextValidatorView> editViews;

    public VWholeEditManager(Activity activity) {
        this.mA = activity;
        editViews = new ArrayList<>();
    }

    /**
     * 自动遍历拿到待校验 EditTextValidatorView
     */
    public void autoAddVViews() {
        View view = mA.getWindow().getDecorView();
        findViewByRecursion(view);
    }

    /**
     * 遍历递归拿到待校验 EditTextValidatorView
     */
    private void findViewByRecursion(View rootView) {
        if (rootView instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) rootView;
            for (int i = 0; i < vg.getChildCount(); i++) {
                if (vg.getChildAt(i) instanceof EditTextValidatorView) {
                    editViews.add((EditTextValidatorView) vg.getChildAt(i));
                } else {
                    findViewByRecursion(vg.getChildAt(i));
                }
            }
        }
    }

    /**
     * 加入待校验view
     */
    public void addEditView(EditTextValidatorView editTextValidatorView) {
        if (editTextValidatorView != null) {
            if (editViews != null && !editViews.contains(editTextValidatorView)) {
                editViews.add(editTextValidatorView);
            }
        }
    }
    public void addEditView(EditTextValidatorView[] editTextValidatorViews) {
        for (EditTextValidatorView editTextValidatorView : editTextValidatorViews) {
            addEditView(editTextValidatorView);
        }
    }
    /**
     * 去除待校验view
     */
    public void removeEditView(EditTextValidatorView editTextValidatorView) {
        if (editTextValidatorView != null) {
            if (editViews != null && editViews.contains(editTextValidatorView)) {
                editViews.remove(editTextValidatorView);
            }
        }
    }

    private boolean getWholeVail() {
        Log.w(Tag, "getWholeVail  editViews.size(): " + editViews.size());
        if (editViews.size() <= 0) {
            return true;
        }

        if (mA.getCurrentFocus() != null)
            mA.getCurrentFocus().clearFocus();

        /**
         * 遍历所有view
         */
        for (int i = 0; i < editViews.size(); i++) {
            EditTextValidatorView validatorView = editViews.get(i);
            validatorView.setValidateIsSuccess(validatorView.validate());
            if (!validatorView.isValidateIsSuccess()) {
                Log.w(Tag, "getWholeVail  showRedToast: " + validatorView.toastForCurrentError());
                ToastHelper.showYellowToast(mA,validatorView.toastForCurrentError());
                validatorView.requestFocus();
                return false;
            }
        }
        return true;
    }

    //校验
    public boolean validate() {
        return getWholeVail();
    }

}
