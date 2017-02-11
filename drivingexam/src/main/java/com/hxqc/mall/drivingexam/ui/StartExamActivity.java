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
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;

/**
 * Created by zhaofan on 2016/8/4.
 */
public class StartExamActivity extends initActivity implements View.OnClickListener {
    private int KEMU;
    private TextView doExam1, doExam2;
    private TextView title;
    private TextView mExamTime, mExamAmount, mTishi;
    private TextView mNickNameTv, unLogin;
    private ImageView mUserImg;
    private String picUrl, nickName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_start_exam;
    }

    @Override
    public void bindView() {
        doExam1 = (TextView) findViewById(R.id.do_exam_1);
        doExam2 = (TextView) findViewById(R.id.do_exam_2);
        title = (TextView) findViewById(R.id.title);
        mExamTime = (TextView) findViewById(R.id.exam_time);
        mExamAmount = (TextView) findViewById(R.id.exam_amount);
        mNickNameTv = (TextView) findViewById(R.id.nickname);
        mTishi = (TextView) findViewById(R.id.tishi);
        mUserImg = (ImageView) findViewById(R.id.user_img2);
        mUserImg.setOnClickListener(this);
        findViewById(R.id.goback).setOnClickListener(this);
        doExam1.setOnClickListener(this);
        doExam2.setOnClickListener(this);
    }

    @Override
    public void init() {
        KEMU = getIntent().getIntExtra("kemu", 1);
        title.setText(KEMU == 1 ? "科目一模拟考试" : "科目四模拟考试");
        mExamTime.setText(KEMU == 1 ? "45分钟" : "30分钟");
        mExamAmount.setText(KEMU == 1 ? "100题" : "50题");
        mTishi.setText(KEMU == 1 ? "模拟考试下不能修改答案，每错1题扣1分。"
                : "模拟考试下不能修改答案，每错1题扣2分。");

        //    if (UserInfoHelper.getInstance().isLogin(this))
        getUserInfo();
    }

    private void getUserInfo() {
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                picUrl = meData.avatar;
                nickName = meData.nickName;
                mNickNameTv.setText(TextUtils.isEmpty(nickName) ? "匿名用户" : nickName);
                ImageUtil.setImage(mContext, mUserImg, picUrl, R.drawable.no_user);
            }

            @Override
            public void onFinish() {

            }
        }, true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.do_exam_1) {
            ActivitySwitcherExam.toExamActivity(this, KEMU);
        } else if (id == R.id.do_exam_2) {
            ActivitySwitcherExam.toExamActivity_NewSubject(this, KEMU);
        } else if (id == R.id.goback) {
            finish();
        } else if (id == R.id.user_img2) {
            UserInfoHelper.getInstance().loginAction(this, 20, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    getUserInfo();
                }
            });
        }
    }


}
