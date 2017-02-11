package com.hxqc.mall.drivingexam.ui;

import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.core.views.dialog.DialogFragment;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.api.ExamApiClient;
import com.hxqc.mall.drivingexam.biz.ErrorViewBiz;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.util.DebugLog;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 科目选择
 * Created by zhaofan on 2016/8/4.
 */
public class ChooseKemuActivity extends initActivity implements View.OnClickListener {
    private RelativeLayout mExamKemu1, mExamKemu4;
    private RelativeLayout mMyWrongSubject1, mMyWrongSubject4;
    private RelativeLayout mScoreRecord1, mScoreRecord4;
    private CustomToolBar mToolBar;
    private int Day = 24 * 60 * 60; //秒
    private boolean isFailed = false;
    private DialogFragment cpg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_kemu;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        mExamKemu1 = (RelativeLayout) findViewById(R.id.exam_kemu1);
        mExamKemu4 = (RelativeLayout) findViewById(R.id.exam_kemu4);
        mMyWrongSubject1 = (RelativeLayout) findViewById(R.id.my_wrong_subject_1);
        mMyWrongSubject4 = (RelativeLayout) findViewById(R.id.my_wrong_subject_4);
        mScoreRecord1 = (RelativeLayout) findViewById(R.id.score_record_1);
        mScoreRecord4 = (RelativeLayout) findViewById(R.id.score_record_4);
        initListener();
    }

    private void initListener() {
        mExamKemu1.setOnClickListener(this);
        mExamKemu4.setOnClickListener(this);
        mMyWrongSubject1.setOnClickListener(this);
        mMyWrongSubject4.setOnClickListener(this);
        mScoreRecord1.setOnClickListener(this);
        mScoreRecord4.setOnClickListener(this);
    }

    @Override
    public void init() {
        cpg = DialogFragment.builder();
        mToolBar.setTitle("驾考模拟");
        getData();

    }

    private void getData() {
        List<String> kemu1_List = str2List(mDiskLruCache.getAsString(C.KEMU_1_AMOUNT));
        List<String> kemu4_List = str2List(mDiskLruCache.getAsString(C.KEMU_4_AMOUNT));

        if (kemu1_List == null || kemu1_List.isEmpty())
            getKemuAmount(C.KEMU_1_AMOUNT, "1");
        if (kemu4_List == null || kemu4_List.isEmpty())
            getKemuAmount(C.KEMU_4_AMOUNT, "4");

    }

    private void getKemuAmount(final String key, String kemu) {
        cpg.show(mContext);
        new ExamApiClient().QusetionNumber(kemu, new LoadingAnimResponseHandler(mContext, false) {
            @Override
            public void onSuccess(String response) {
                cpg.hide();
                List<String> data = new Gson().fromJson(response, new TypeToken<List<String>>() {
                }.getType());
                DebugLog.i(key, data.size() + "");
                mDiskLruCache.put(key, response, 7 * Day);
                mSpUtils.put(key, data.size());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                cpg.hide();
                requestFail();
            }
        });

   /*     ApiClient.getInstance().mService.getExamAmount("Android", kemu)
                .compose(RxSchedulers.<List<String>>io_main())
                .subscribe(new ResponseSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> response) {
                        DebugLog.i(key, response.size() + "");
                        mDiskLruCache.put(key, response.toString());
                        mSpUtils.put(key, response.size());
                    }

                    @Override
                    public void onFailure(ErrorMsg mError) {

                    }
                });*/
    }

    private void requestFail() {
        if (!isFailed) {
            isFailed = true;
            ErrorViewBiz.requestFail(this, mToolBar.getHeight(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFailed = false;
                    getData();
                }
            });
        }
    }

    private List<String> str2List(String str) {
        return new Gson().fromJson(str, new TypeToken<List<String>>() {
        }.getType());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //模拟考试
        if (id == R.id.exam_kemu1) {
            toStartExamActivity(1);
        } else if (id == R.id.exam_kemu4) {
            toStartExamActivity(4);
        }
        //我的错题
        else if (id == R.id.my_wrong_subject_1) {
            toExamActivty_MyWrongSub(1);
        } else if (id == R.id.my_wrong_subject_4) {
            toExamActivty_MyWrongSub(4);
        }
        //kaoshijilu
        else if (id == R.id.score_record_1) {
            toRecordHistoryActivity(1);
        } else if (id == R.id.score_record_4) {
            toRecordHistoryActivity(4);
        }
    }


    /**
     * 模拟考试
     */
    private void toStartExamActivity(int kemu) {
        ActivitySwitcherExam.toStartExamActivity(this, kemu);
    }


    /**
     * 我的错题
     */
    private void toExamActivty_MyWrongSub(int kemu) {
        ActivitySwitcherExam.toExamActivity_MyWrongSub(this, kemu);
    }


    /**
     * 考试记录
     */
    private void toRecordHistoryActivity(int kemu) { 
        ActivitySwitcherExam.toRecordHistoryActivity(this, kemu);
    }


}
