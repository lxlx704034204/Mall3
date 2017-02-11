package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.api.RequestFailView;

/**
 * Author :liukechong
 * Date : 2015-12-14
 * FIXME
 * Todo
 */
@Deprecated
public class RequestFailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RequestFailView mRequestFailView = new RequestFailView(getContext());
        mRequestFailView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        Bundle arguments = getArguments();
        String message = "";
        if (arguments != null) {
            message = arguments.getString("message");
        }
        if (TextUtils.isEmpty(message)) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        } else {
            mRequestFailView.setEmptyDescription(message);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        return mRequestFailView;
    }
}
