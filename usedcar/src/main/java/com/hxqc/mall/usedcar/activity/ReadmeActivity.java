package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;

/**
 * 说明:车主自述
 *
 * @author: 吕飞
 * @since: 2016-06-21
 * Copyright:恒信汽车电子商务有限公司
 */
public class ReadmeActivity extends NoBackActivity {
    Toolbar mToolbar;
    EditText mReadmeEditView;
    String mReadme;
    String mExample;
    TextView mCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readme);
        try {
            mExample = new UsedCarSPHelper(this).getSellCarChoose().readme_example;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mReadme = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(UsedCarActivitySwitcher.README);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("车主自述");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mReadmeEditView = (EditText) findViewById(R.id.readme_edit);
        mCountView = (TextView) findViewById(R.id.count);
        if (!TextUtils.isEmpty(mReadme)) {
            mReadmeEditView.setText(mReadme);
            mReadmeEditView.setSelection(mReadme.length());
        }
        mReadmeEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCountView.setText(charSequence.length() + "/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void writeExample(View view) {
        if (TextUtils.isEmpty(mExample)) {
            mReadmeEditView.setText("我的这辆车整体车况还不错，全程在4S店保养。全车没有什么大伤，有过一些小磕碰也都已经及时喷漆处理。车身整体比较耐看，驾驶操控性能好，动力够用，起步加速快，平时比较爱惜，上下班代步，市区道路行驶，年平均行驶里程较少，空间还可以，家用能满足各种需求。加装真皮、导航以及倒车影像。如果觉得还不错，就给我打电话看车吧。");
        } else {
            mReadmeEditView.setText(mExample);
        }
    }

    public void clear(View view) {
        mReadmeEditView.setText("");
    }

    public void complete(View view) {
        Intent intent = new Intent();
        intent.putExtra(UsedCarActivitySwitcher.README, mReadmeEditView.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }
}
