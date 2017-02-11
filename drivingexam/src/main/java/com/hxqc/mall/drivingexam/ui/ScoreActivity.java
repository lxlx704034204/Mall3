package com.hxqc.mall.drivingexam.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.drivingexam.db.model.ScoreRecord;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.drivingexam.utils.DateUtils;


/**
 * 答题得分界面
 * Created by zhaofan on 2016/8/3.
 */
public class ScoreActivity extends initActivity implements View.OnClickListener {
    private TextView mScoreTv, mLevelTv;
    private TextView mSeeWrongSub, mTryAgain;
    private TextView minuteTv, secondTv;
    private TextView mKemuTv;
    private TextView mNickName;
    private ImageView mUserImg;
    private long dateTag;
    long saveTime = 0;
    int rightCount, wrongCount;
    int score;
    int kemu;
    private String picUrl, nickName;

    @Override

    public int getLayoutId() {
        return R.layout.activity_score;
    }


    @Override
    public void bindView() {
        mScoreTv = (TextView) findViewById(R.id.score);
        mLevelTv = (TextView) findViewById(R.id.level);
        mSeeWrongSub = (TextView) findViewById(R.id.see_wrong_subject);
        mTryAgain = (TextView) findViewById(R.id.try_again);
        mKemuTv = (TextView) findViewById(R.id.kemu);
        minuteTv = (TextView) findViewById(R.id.min);
        secondTv = (TextView) findViewById(R.id.sec);
        mUserImg = (ImageView) findViewById(R.id.user_img2);
        mNickName = (TextView) findViewById(R.id.nickname);

    }

    @Override
    public void init() {
        dateTag = getIntent().getLongExtra("dateTag", 0);
        saveTime = getIntent().getLongExtra("saveTime", 0);
        rightCount = getIntent().getIntExtra("rightCount", 0);
        wrongCount = getIntent().getIntExtra("wrongCount", 0);
        kemu = getIntent().getIntExtra("kemu", 0);
        score = kemu == 1 ? rightCount : 2 * rightCount;
        initListener();
        initDataResourse();
        saveScoreRecord();

    }


    private void initDataResourse() {
        mKemuTv.setText("小车：" + (kemu == 1 ? "科目一" : "科目四"));
        mScoreTv.setText(score + "");
        mLevelTv.setText(score < 90 ? "马路杀手" : "恒信车神");
        mLevelTv.setBackgroundResource(score < 90 ? R.drawable.top_2 : R.drawable.top);
        figureTime(saveTime);
        //    if (UserInfoHelper.getInstance().isLogin(this))
        getUserInfo();
    }

    private void initListener() {
        mSeeWrongSub.setOnClickListener(this);
        mTryAgain.setOnClickListener(this);
        findViewById(R.id.goback).setOnClickListener(this);

    }

    private void getUserInfo() {
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                picUrl = meData.avatar;
                nickName = meData.nickName;
                mNickName.setText(TextUtils.isEmpty(nickName) ? "匿名用户" : nickName);
                ImageUtil.setImage(mContext, mUserImg, picUrl, R.drawable.no_user);
            }

            @Override
            public void onFinish() {
            }
        }, true);
    }


    private void saveScoreRecord() {
        String date = DateUtils.getNowForFileName();
        new ScoreRecord(kemu, score + "", saveTime, date).save();

    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        //只看错题
        if (id == R.id.see_wrong_subject) {
            toExamActivty(kemu, true);
        }
        //再来一次
        else if (id == R.id.try_again) {
            ActivitySwitcherExam.toStartExamActivity(this, kemu);
        }
        //返回
        else if (id == R.id.goback) {
            finish();
        }
    }


    private void figureTime(long millis) {
        long m, s;
        m = millis / (60 * 1000);
        s = (millis / 1000) - m * 60;
        minuteTv.setText(m + "");
        secondTv.setText(s + "");
    }


    private void toExamActivty(int kemu, boolean isShowWrong) {
        ActivitySwitcherExam.toExamActivity(this, kemu, dateTag, isShowWrong);
    }


}
