package com.hxqc.mall.drivingexam.ui.doexam.popuwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.ui.doexam.ExamActivity;
import com.hxqc.mall.drivingexam.ui.doexam.adapter.SubjectAmountAdapter;
import com.hxqc.util.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * 题数
 */
public class SubjectAmountPopuwin extends PopupWindow {
    private int nowPage, max, rightCount, wrongCount;
    private Activity context;
    private RelativeLayout mainview;
    private View mMenuView;
    WindowManager.LayoutParams lp;
    private GridView gv;
    private List<ExamRecord> examRecordList;
    private SubjectAmountAdapter mAdapter;
    private TextView wrong, right, count, totalCount;
    private boolean examMode;
    private boolean myWrongMode;
    private RelativeLayout submitxam;
    private TextView mSubmitTv;
    private ImageView mSubmitImg;


    public SubjectAmountPopuwin(final Activity context, List<ExamRecord> examRecordList, int nowPage,
                                int max, int rightCount, int wrongCount, boolean examMode, boolean myWrongMode) {
        super(context);
        this.context = context;
        this.examRecordList = examRecordList;
        this.nowPage = nowPage;
        this.max = max;
        this.rightCount = rightCount;
        this.wrongCount = wrongCount;
        this.examMode = examMode;
        this.myWrongMode = myWrongMode;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.popuwindow_exam, null);


        init();
        bindview();
        setData();
    }


    private void bindview() {
        mainview = (RelativeLayout) mMenuView.findViewById(R.id.main);
        gv = (GridView) mMenuView.findViewById(R.id.gv);
        wrong = (TextView) mMenuView.findViewById(R.id.wrong);
        right = (TextView) mMenuView.findViewById(R.id.right);
        count = (TextView) mMenuView.findViewById(R.id.count);
        totalCount = (TextView) mMenuView.findViewById(R.id.total_count);
        mSubmitTv = (TextView) mMenuView.findViewById(R.id.submit_exam_tv);
        mSubmitImg = (ImageView) mMenuView.findViewById(R.id.submit_exam_img);
        submitxam = (RelativeLayout) mMenuView.findViewById(R.id.submit_exam);
        submitxam.setVisibility(examMode ? View.VISIBLE : View.GONE);
        if (myWrongMode) {
            submitxam.setVisibility(View.VISIBLE);
            mSubmitTv.setText("移除");
            mSubmitImg.setImageResource(R.drawable.yichu);
        }
        ViewGroup.LayoutParams pp = mainview.getLayoutParams();
        pp.height = (int) (ScreenUtil.getScreenHeight(context.getApplication()) * 0.65);
        mainview.setLayoutParams(pp);

    }


    private void setData() {
        wrong.setText(wrongCount + "");
        right.setText(rightCount + "");
        count.setText((nowPage + 1) + "");
        totalCount.setText("/" + max);
        submitxam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Event(mSubmitTv.getText().toString(), ExamActivity.SUBMIT_EXAM));
            }
        });

        mAdapter = new SubjectAmountAdapter(context);
        gv.setAdapter(mAdapter);
        mAdapter.setNowPage(nowPage);
        mAdapter.setData(examRecordList);
        gv.setSelection(nowPage);
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        lp = context.getWindow().getAttributes();
        lp.alpha = 0.6f;
        context.getWindow().setAttributes(lp);
    }

    private void init() {
        this.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimSlideFromBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (y < mMenuView.findViewById(R.id.main).getTop()) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


}
