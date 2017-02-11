package com.hxqc.mall.drivingexam.ui.doexam;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.core.util.utils.rxjava.RxSchedulers;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.core.views.dialog.DialogFragment;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.api.ExamApiClient;
import com.hxqc.mall.drivingexam.biz.DoExam;
import com.hxqc.mall.drivingexam.biz.ErrorViewBiz;
import com.hxqc.mall.drivingexam.biz.InitDbData;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.drivingexam.db.ExamDAO;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA_Table;
import com.hxqc.mall.drivingexam.model.DrivingLicenseExam;
import com.hxqc.mall.drivingexam.model.QItems;
import com.hxqc.mall.drivingexam.ui.dialog.CommonDialog;
import com.hxqc.mall.drivingexam.ui.doexam.popuwindow.SubjectAmountPopuwin;
import com.hxqc.mall.drivingexam.ui.doexam.popuwindow.WrongSubjectSettingPopuwindow;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.photolibrary.activity.ImagePagerActivity;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 答题界面
 * Created by zhaofan on 2016/8/1.
 */
public class ExamActivity extends initActivity implements View.OnClickListener {
    public static final String NEXT_PAGE = "next_page";
    public static final String TO_PAGE = "to_page";
    public static final String SHOW_PHOTO_VIEW = "SHOW_PHOTO_VIEW";
    public static final String OFF_PHOTO_VIEW = "OFF_PHOTO_VIEW";
    public static final String SUBMIT_EXAM = "SUBMIT_EXAM";
    public static final String SHOW_WRONG_SUBJECT = "SHOW_WRONG_SUBJECT";
    public static final String MY_WRONG_SUBJECT = "My_WRONG_SUBJECT";
    public static final String ONLY_NEW_SUBJECT = "ONLY_NEW_SUBJECT";
    private long TOTAL_TIME = 45 * 60 * 1000;
    private int KEMU = 1;
    private boolean examMode = true; //true为模拟考试
    private boolean myWrongMode;   //错题模式
    private boolean isRemove = false;
    private boolean isShowPhotoView = false;
    private long dateTag;
    private RelativeLayout tabview;
    private CustomToolBar mToolBar;
    private TextView mToolBarRightTv;
    SubjectAmountPopuwin examPopuwin; //显示所有题数的popuwindow
    private String[] CONTENT;
    //当前题数，总共题数，正确题数，错误题数
    private TextView mCountTv, mTotalCountTv, mRightCountTv, mWrongCountTv;
    private RelativeLayout mSubmitExam;
    private TextView mSubmitTv;
    private ImageView mSubmitImg;
    private CommonDialog mTimePauseDialog, ExamSubmitDialog, mFinishExamdialog, mTimeOutdialog;
    private CommonDialog mWrongSubjectOverDialog, exitDialog;
    ViewPager pager;
    int mNowPage = 0, max = 0;
    int rightCount, wrongCount;
    List<QItems> mList;
    List<String> mIdList;
    TikTok mTikTok;
    long saveTime = 0;
    private DialogFragment cpg;


    @Override
    public int getLayoutId() {
        return R.layout.activity_doexam;
    }

    @Override
    public void init() {
        cpg =DialogFragment.builder();
        mEventBus.register(this);
        DbHelper.delete(ExamRecord.class);
        dateTag = System.currentTimeMillis();
        //   forTest();
        initToolBar();
        KEMU = getIntent().getIntExtra("kemu", 1);
        TOTAL_TIME = KEMU == 1 ? 45 * 60 * 1000 : 30 * 60 * 1000;

        //只看错题
        if (getIntent().getBooleanExtra(SHOW_WRONG_SUBJECT, false)) {
            dateTag = getIntent().getLongExtra("dateTag", 0);
            examMode = false;
            showWrongSubject(dateTag);
        }

        //我的错题
        myWrongMode = getIntent().getBooleanExtra(MY_WRONG_SUBJECT, false);
        if (myWrongMode) {
            examMode = false;
            showMyWrongSubject(KEMU);
            showSetting();
        }

        //模拟考
        if (examMode) {
            getData();
        }

        countRightAndWrong("");
        initViewPager();

    }


    /**
     * 只看错题
     */
    private void showWrongSubject(long dateTag) {
        mSubmitExam.setVisibility(View.GONE);
        mToolBar.setTitle("查看错题");
        mList = ExamDAO.queryWrongSubject(dateTag);
        if (mList.isEmpty())
            showEmpty();
        InitDbData.initWrongRecordDbData(mDiskLruCache);
        setViewPager(mList);
    }

    /**
     * 我的错题
     */
    private void showMyWrongSubject(int menu) {
        mToolBar.setTitle("我的错题");
        mSubmitTv.setText("移除");
        mSubmitImg.setImageResource(R.drawable.yichu);
        cpg.show(mContext);
        DoExam.searchMyWrongSubject(menu).subscribe(new Observer<List<QItems>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showEmpty();
                cpg.hide();
            }

            @Override
            public void onNext(List<QItems> qItemses) {
                mList = qItemses;
                if (!isRemove)
                    InitDbData.initRecordDbData(qItemses.size());
                else
                    InitDbData.initMyWrongRecordDbData(Integer.parseInt(mCountTv.getText().toString()));
                setViewPager(qItemses);
                pager.setCurrentItem(mNowPage, false);
                mSubmitExam.setClickable(true);
                cpg.hide();
            }
        });
    }


    @Override
    public void bindView() {
        mSubmitExam = (RelativeLayout) findViewById(R.id.submit_exam);
        mSubmitTv = (TextView) findViewById(R.id.submit_exam_tv);
        mSubmitImg = (ImageView) findViewById(R.id.submit_exam_img);
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        mCountTv = (TextView) findViewById(R.id.count);
        mTotalCountTv = (TextView) findViewById(R.id.total_count);
        pager = (ViewPager) findViewById(R.id.pager);
        mRightCountTv = (TextView) findViewById(R.id.right);
        mWrongCountTv = (TextView) findViewById(R.id.wrong);
        tabview = (RelativeLayout) findViewById(R.id.tab);

        bindListener();

    }

    private void bindListener() {
        tabview.setOnClickListener(this);
        mSubmitExam.setOnClickListener(this);
    }

    private void initToolBar() {
        mToolBar.setTitle("模拟考试");
        mToolBar.setOnGoBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });
    }


    private void showSetting() {
        mToolBarRightTv = (TextView) mToolBar.findViewById(R.id.topbar_right_tv);
        mToolBarRightTv.setText("设置");
        mToolBarRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     ActivitySwitcherExam.toWrongSubjectSettingActivity(ExamActivity.this);
                WrongSubjectSettingPopuwindow settingPopuwindow = new WrongSubjectSettingPopuwindow(mContext);
                settingPopuwindow.showAsDropDown(mToolBar);
            }
        });
    }


    private void getData() {
        //只做新题
        cpg.show(mContext);
        if (getIntent().getBooleanExtra(ONLY_NEW_SUBJECT, false)) {
            Observable.create(new Observable.OnSubscribe<List<String>>() {
                @Override
                public void call(Subscriber<? super List<String>> subscriber) {
                    subscriber.onNext(DoExam.getNewSubjectId(mDiskLruCache, KEMU));
                }
            })
                    .compose(RxSchedulers.<List<String>>io_main())
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> strings) {
                            mIdList = strings;
                            String quesionID = mIdList.toString().replace(" ", "").replace("[", "").replace("]", "");
                            getHttpRequest(quesionID);
                        }
                    });
        } else
            getHttpRequest("");
    }

    private void getHttpRequest(String quesionID) {
        new ExamApiClient().getQusetion(KEMU + "", quesionID, new LoadingAnimResponseHandler(this, false) {
            @Override
            public void onSuccess(String response) {
                DrivingLicenseExam data = new Gson().fromJson(response, DrivingLicenseExam.class);
                startTimer();
                if (!getIntent().getBooleanExtra(ONLY_NEW_SUBJECT, false) || mIdList.isEmpty()) {
                    mIdList = data.allQuestionID;
                }
                mList = data.QItems;
                InitDbData.initRecordDbData(mList.size());
                setViewPager(mList);
                cpg.hide();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestFail();
                cpg.hide();
            }
        });
    }

    private void setViewPager(List<QItems> mList) {
        if (!mList.isEmpty()) {
            max = mList.size();
            CONTENT = new String[max];
            for (int i = 0; i < max; i++) {
                CONTENT[i] = (i + 1) + "";
            }
            mCountTv.setText(CONTENT[0]);
            mTotalCountTv.setText("/" + max + "");
            FragmentStatePagerAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager());
            pager.setAdapter(adapter);
        }
    }


    private void initViewPager() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCountTv.setText(CONTENT[position]);
                mNowPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * 计算对题与错题
     */
    private void countRightAndWrong(final String questionId) {
        rightCount = DbHelper.query(ExamRecord.class, ExamRecord_Table.isRight.eq("1")).size();
        wrongCount = DbHelper.query(ExamRecord.class, ExamRecord_Table.isRight.eq("0")).size();
        mRightCountTv.setText(rightCount + "");
        mWrongCountTv.setText(wrongCount + "");
        if (examMode) {

            //保存错题
            DoExam.saveWrongSubject(mList, questionId, mNowPage, KEMU, dateTag);
            //保存已做的题
            DoExam.saveCompleteSubject(KEMU, questionId);
            //错题上限提示
            if (wrongCount == (KEMU == 1 ? 11 : 6))
                showWrongSubjectOverDialog();
            //做完全部题
            if (rightCount + wrongCount == max && max != 0) {
                finishExamDialog();
            }
        }

    }


    /**
     * 移除我的错题
     */
    private void deleteWrongSub() {
        isRemove = true;
        final List<WrongA> list = DbHelper.query(WrongA.class, WrongA_Table.kumu.eq(KEMU));
        DebugLog.e("mNowPage", mNowPage + "");
        final int pos = Integer.parseInt(mCountTv.getText().toString()) - 1;
        if (pos < list.size()) {
            // cpg.showing();
            mSubmitExam.setClickable(false);
            DoExam.deleteWrongSubject(KEMU, list.get(pos).questionId).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean isEmpty) {
                    // cpg.hide();
                    if (isEmpty)
                        showEmpty();
                    else
                        showMyWrongSubject(KEMU);
                }
            });
        }


    }

    @Subscribe
    public void getEvent(Event msg) {
        //翻页
        if (msg.what.equals(NEXT_PAGE)) {
            countRightAndWrong(((String) msg.obj));
            if (!myWrongMode)
                pager.setCurrentItem(mNowPage + 1);
            else {
                if (DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(mNowPage + 1)).getIsRight().equals("1"))
                    pager.setCurrentItem(mNowPage + 1);
            }

        }
        //移动到指定页
        else if (msg.what.equals(TO_PAGE)) {
            pager.setCurrentItem((Integer) msg.obj, false);
            if (examPopuwin.isShowing())
                examPopuwin.dismiss();
        }
        //提交试卷
        else if (msg.what.equals(SUBMIT_EXAM)) {
            if (msg.obj.equals("交卷"))
                submitExamDialog();
            else if (msg.obj.equals("移除")) {
                examPopuwin.dismiss();
                deleteWrongSub();
            }
        }
        //查看大图
        else if (msg.what.equals(SHOW_PHOTO_VIEW)) {
            isShowPhotoView = true;
            String[] url = new String[]{(String) msg.obj};
            ActivitySwitcherExam.toPhotoView(this, url, 0);
        } else if (msg.what.equals(ImagePagerActivity.CLOSE_PHOTO_VIEW)) {
            isShowPhotoView = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //底部tab
        if (id == R.id.tab) {
            examPopuwin = new SubjectAmountPopuwin(ExamActivity.this,
                    DbHelper.query(ExamRecord.class, ExamRecord_Table.num, true),
                    mNowPage, max, rightCount, wrongCount, examMode, myWrongMode);
            examPopuwin.showAtLocation(mToolBar, Gravity.BOTTOM, 0, 0);
        }
        //提交
        else if (id == R.id.submit_exam) {
            if (mSubmitTv.getText().toString().equals("交卷"))
                submitExamDialog();
            else if (mSubmitTv.getText().toString().equals("移除")) {
                deleteWrongSub();
            }
        }
    }


    private void showEmpty() {
        ErrorViewBiz.showWrongSubjectEmpty(this);
    }

    private void requestFail() {
        ErrorViewBiz.requestFail(this, mToolBar.getHeight(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!isShowPhotoView)
            onPauseDialog();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        //清除我的错题中做对的题
        if (myWrongMode && ((Boolean) mSpUtils.get(C.REMOVE_WRONG_SUB_SETTING, true)))
            DoExam.autoDeleteWrongSubjectIfRight(KEMU);
    }


    /**
     * 转换时间
     */
    private String figureTime(long millis) {
        long m, s;
        m = millis / (60 * 1000);
        s = (millis / 1000) - m * 60;
        return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        mTikTok = new TikTok(TOTAL_TIME, 1000);
        mTikTok.start();
    }


    /**
     * 继续计时
     */
    private void resumeTimer() {
        mTikTok = new TikTok(saveTime, 1000);
        mTikTok.start();

    }

    /**
     * 取消计时器
     */
    private void cancelTimer() {
        if (mTikTok != null) {
            mTikTok.cancel();
            mTikTok = null;
        }

    }


    /**
     * FragmentPagerAdapter
     */
    class MyFragmentAdapter extends FragmentStatePagerAdapter {
        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ExamFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("dateTag", dateTag);
            bundle.putInt("index", position + 1);
            bundle.putParcelable("data", mList.get(position));
            if (mIdList != null && !mIdList.isEmpty())
                bundle.putString("id", mIdList.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            // return CONTENT.length;
            return mList.size();
        }
    }


    /**
     * 倒计时类
     */
    class TikTok extends CountDownTimer {
        public TikTok(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millis) {
            saveTime = millis;
            mToolBar.setTitle("倒计时" + figureTime(millis));
        }

        @Override
        public void onFinish() {
            saveTime = 0;
            mToolBar.setTitle("考试结束");
            timeFinishDialog();
        }
    }

    boolean isShowDialog = false;

    /**
     * 错题提示Dialog
     */
    private void showWrongSubjectOverDialog() {
        if (isShowDialog)
            return;
        if (examMode) {
            isShowDialog = true;
            mWrongSubjectOverDialog = CommonDialog.getInstance(ExamActivity.this);
            mWrongSubjectOverDialog.setTitle("提示").setContent("错题已超过" + (KEMU == 1 ? 10 : 5) + "个，成绩不合格")
                    .setLeftButton("继续答题", new CommonDialog.LeftBtnClickListener() {
                        @Override
                        public void onLeftBtnClickListener(View v) {
                            mWrongSubjectOverDialog = null;
                        }
                    })
                    .setRightButton("确认交卷", new CommonDialog.RightBtnClickListener() {
                        @Override
                        public void onRightBtnClickListener(View v) {
                            toScoreActivty();
                        }
                    })
                    .banBackCancel()
                    .show("");
        } else
            finish();
    }


    /**
     * 交卷子Dialog
     */
    private void submitExamDialog() {
        if (examMode && DbHelper.query(ExamRecord.class, ExamRecord_Table.isFinish.eq("1")).size() > 0) {
            ExamSubmitDialog = CommonDialog.getInstance(ExamActivity.this);
            ExamSubmitDialog.setTitle("提示").setContent("还有" + (max - rightCount - wrongCount) + "道题未做，确定要交卷吗？")
                    .setLeftButton("继续答题", new CommonDialog.LeftBtnClickListener() {
                        @Override
                        public void onLeftBtnClickListener(View v) {
                            ExamSubmitDialog = null;
                        }
                    })
                    .setRightButton("确认交卷", new CommonDialog.RightBtnClickListener() {
                        @Override
                        public void onRightBtnClickListener(View v) {
                            toScoreActivty();
                        }
                    })
                    .banBackCancel()
                    .show("");
        } else
            finish();
    }

    /**
     * 退出Dialog
     */
    private void showExitDialog() {
        if (examMode && DbHelper.query(ExamRecord.class, ExamRecord_Table.isFinish.eq("1")).size() > 0) {
            exitDialog = CommonDialog.getInstance(ExamActivity.this);
            exitDialog.setTitle("提示").setContent("您已回答了" + (rightCount + wrongCount) + "题，是否放弃本次考试")
                    .setLeftButton("继续考试", new CommonDialog.LeftBtnClickListener() {
                        @Override
                        public void onLeftBtnClickListener(View v) {
                            exitDialog = null;
                        }
                    })
                    .setRightButton("放弃", new CommonDialog.RightBtnClickListener() {
                        @Override
                        public void onRightBtnClickListener(View v) {
                            finish();
                        }
                    })
                    .banBackCancel()
                    .show("");
        } else
            finish();
    }


    /**
     * 题做完Dialog
     */
    private void finishExamDialog() {
        cancelTimer();
        int score = KEMU == 1 ? rightCount : 2 * rightCount;
        mFinishExamdialog = CommonDialog.getInstance(ExamActivity.this);
        mFinishExamdialog.setAlertDialog("提示", "题目已做完，您本次考试得分" +
                score + "分，" + "成绩" + (score < 90 ? "不及格" : "及格") + "，请交卷")
                .setRightButton("交卷", new CommonDialog.RightBtnClickListener() {
                    @Override
                    public void onRightBtnClickListener(View v) {
                        toScoreActivty();
                    }
                })
                .show("");
    }

    /**
     * 暂停Dialog
     */
    private void onPauseDialog() {
        if (mTimePauseDialog == null && ExamSubmitDialog == null
                && mFinishExamdialog == null && mTimeOutdialog == null && exitDialog == null
                && mWrongSubjectOverDialog == null && saveTime > 0) {
            cancelTimer();
            mTimePauseDialog = CommonDialog.getInstance(ExamActivity.this);
            mTimePauseDialog.setAlertDialog("提示", "您还剩余" + (max - rightCount - wrongCount) + "题未完成，"
                    + "剩余" + figureTime(saveTime).replace(":", "分") + "秒")
                    .setRightButton("继续答题", new CommonDialog.RightBtnClickListener() {
                        @Override
                        public void onRightBtnClickListener(View v) {
                            resumeTimer();
                            mTimePauseDialog = null;
                        }
                    })
                    .show("");
        }
    }

    /**
     * 时间到Dialog
     */
    private void timeFinishDialog() {
        if (mTimePauseDialog == null) {
            int score = KEMU == 1 ? rightCount : 2 * rightCount;
            mTimeOutdialog = CommonDialog.getInstance(ExamActivity.this);
            mTimeOutdialog.setAlertDialog("提示", "考试已结束，您本次考试得分" +
                    score + "分，" + "成绩" + (score < 90 ? "不及格" : "及格") + "，请交卷")
                    .setRightButton("交卷", new CommonDialog.RightBtnClickListener() {
                        @Override
                        public void onRightBtnClickListener(View v) {
                            toScoreActivty();
                        }
                    })
                    .show(getFragmentManager(), "");
        }
    }


    /**
     * 跳转到得分Activty
     */
    private void toScoreActivty() {
        cancelTimer();
        List<ExamRecord> ExamRecordList = DbHelper.query(ExamRecord.class, ExamRecord_Table.isRight.eq("0"));
        List<ExamRecord> wrongRecordList = new ArrayList<>();
        for (int i = 0; i < ExamRecordList.size(); i++) {
            ExamRecord entity = ExamRecordList.get(i);
            wrongRecordList.add(new ExamRecord(i + 1, "1", entity.getChoose(), entity.getAnswer(), "0"));
        }
        mDiskLruCache.put("wronglist", new Gson().toJson(wrongRecordList));

        ActivitySwitcherExam.toScoreActivity(this, dateTag, TOTAL_TIME - saveTime, rightCount, wrongCount, KEMU);
        finish();
    }


    private void forTest() {
        //当前题目 记录
        mCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ExamRecord> data = DbHelper.query(ExamRecord.class, ExamRecord_Table.num, true);
                for (ExamRecord i : data) {
                    Log.e("query2", i.toString());
                }
            }
        });


        //只看错题
        mTotalCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ExamRecord> ExamRecordList = DbHelper.query(ExamRecord.class, ExamRecord_Table.isRight.eq("0"));
                List<ExamRecord> ExamRecordList2 = new ArrayList<>();
                for (int i = 0; i < ExamRecordList.size(); i++) {
                    ExamRecord entity = ExamRecordList.get(i);
                    ExamRecordList2.add(new ExamRecord(i + 1, "1", entity.getChoose(), entity.getAnswer(), "0"));
                }
                for (ExamRecord a : ExamRecordList2) {
                    DebugLog.d("ExamRecord", a.toString());
                }

                for (QItems i : ExamDAO.queryWrongSubject(dateTag)) {
                    DebugLog.i("mQItemsList", i.toString());
                }
            }
        });

        //总共做过的题
        mRightCountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CompleteSub i : DbHelper.query(CompleteSub.class)) {
                    DebugLog.i("total", i.toString());
                }

            }
        });

        //做错的题
        mWrongCountTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (QItems i : ExamDAO.queryAllWrongSubject(KEMU)) {
                    DebugLog.i("mQItemsList", i.toString());
                }
            }
        });


    }


}
