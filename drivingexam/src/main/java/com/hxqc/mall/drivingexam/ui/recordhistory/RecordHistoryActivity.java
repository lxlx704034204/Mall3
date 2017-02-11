package com.hxqc.mall.drivingexam.ui.recordhistory;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.hxqc.mall.core.base.mvp.initActivity;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.views.CustomToolBar;
import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.mall.core.views.dialog.DialogFragment;
import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.biz.ScoreRecordBiz;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.drivingexam.db.model.ScoreRecord;
import com.hxqc.mall.drivingexam.db.model.ScoreRecord_Table;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA;
import com.hxqc.mall.drivingexam.ui.dialog.CommonDialog;
import com.hxqc.mall.drivingexam.utils.ActivitySwitcherExam;
import com.hxqc.mall.drivingexam.utils.StringUtils;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;


/**
 * 成绩记录
 * Created by zhaofan on 2016/8/3.
 */
public class RecordHistoryActivity extends initActivity {
	float totalCount = 1, unFinishCount, rightCount, wrongCount;
	private CustomToolBar mToolBar;
    private PieChart mChart;
    private int kemu;
    private TextView mRightTv, mWrongTv, mUnfinishTv;
    private TextView mRightPer, mWrongPer, mUnfinishPer;
    private RecordHistoryAdapter mAdapter;
    private ListView lv;
    private TextView mKemuTv, mAmountTv;
    private TextView rightTv;
    private DialogFragment cpg;

    @Override
    public int getLayoutId() {
        return R.layout.activity_examrecord;
    }

    @Override
    public void bindView() {
        mToolBar = (CustomToolBar) findViewById(R.id.topbar);
        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        mRightTv = (TextView) findViewById(R.id.right_tv);
        mWrongTv = (TextView) findViewById(R.id.wrong_tv);
        mUnfinishTv = (TextView) findViewById(R.id.unfinish_tv);
        mRightPer = (TextView) findViewById(R.id.right_per);
        mWrongPer = (TextView) findViewById(R.id.wrong_per);
        mUnfinishPer = (TextView) findViewById(R.id.unfinish_per);
        mKemuTv = (TextView) findViewById(R.id.kemu_tv);
        mAmountTv = (TextView) findViewById(R.id.amount);
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    public void init() {
        cpg = DialogFragment.builder();
        kemu = getIntent().getIntExtra("kemu", 1);
        initToolBar();
        initDataResourse();
    }

    private void initDataResourse() {
        cpg.show(mContext);
        ScoreRecordBiz.getScoreRecord(kemu, new Action1<List<WrongA>>() {
            @Override
            public void call(List<WrongA> mWrongSubList) {
                wrongCount = mWrongSubList.size();
            }
        }).subscribe(new Action1<List<CompleteSub>>() {
            @Override
            public void call(List<CompleteSub> mCompleteSubList) {
                rightCount = mCompleteSubList.size() - wrongCount;
                totalCount = Float.parseFloat(mSpUtils.get(kemu == 1 ? C.KEMU_1_AMOUNT : C.KEMU_4_AMOUNT, 0) + "");
                unFinishCount = totalCount - mCompleteSubList.size();
                mKemuTv.setText(kemu == 1 ? "科目一" : "科目四");
                mAmountTv.setText((int) totalCount + "");
                mRightTv.setText("做对" + ((int) rightCount) + "题");
                mWrongTv.setText("做错" + ((int) wrongCount) + "题");
                mUnfinishTv.setText("未做" + ((int) unFinishCount) + "题");
                mRightPer.setText(toPercent(rightCount / totalCount));
                mWrongPer.setText(toPercent(wrongCount / totalCount));
                mUnfinishPer.setText(toPercent(unFinishCount / totalCount));
                DebugLog.i("count", unFinishCount + " " + rightCount + " " + wrongCount);
                setChart(rightCount / totalCount, unFinishCount / totalCount, wrongCount / totalCount);
                setListView();
                cpg.hide();
            }
        });
    }


    private void setChart(float... count) {
        PieData mPieData = getPieData(count);
        showChart(mChart, mPieData);
    }

    private void setListView() {
        List<ScoreRecord> mScoreRecordList = DbHelper.query(ScoreRecord.class, ScoreRecord_Table.id, false,
                ScoreRecord_Table.kumu.eq(kemu));
        if (mScoreRecordList.size() == 0) {
            showEmpty();
            rightTv.setVisibility(View.GONE);
            return;
        }
        for (ScoreRecord i : mScoreRecordList) {
            DebugLog.i("ScoreRecord", i.toString());
        }
        View v = View.inflate(this, R.layout.item_string, null);
        TextView tv = (TextView) v.findViewById(R.id.textview);
        tv.setText("考试记录");
        lv.addHeaderView(v);
        mAdapter = new RecordHistoryAdapter(this);
        lv.setAdapter(mAdapter);
        mAdapter.setData(mScoreRecordList);

    }

    private String toPercent(float f) {
        return "占" + StringUtils.DecimalFormat(f * 100, "0.#") + "%";
    }


    private void showEmpty() {
        mToolBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                ErrorView.builder(mContext).topMargin(findViewById(R.id.count_lay).getHeight())
                        .showCustom("还没有考试记录", "我要考试", R.drawable.file_search, false, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivitySwitcherExam.toStartExamActivity(mContext, kemu);
                            }
                        });
            }
        }, 20);

    }


    private void initToolBar() {
        mToolBar.setTitle("考试记录");
        rightTv = (TextView) mToolBar.findViewById(R.id.topbar_right_tv);
        rightTv.setText("清空");
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCleanDialog();
            }
        });
    }

    private void showCleanDialog() {
        CommonDialog dialog = CommonDialog.getInstance(this);
        dialog.setTitle("提示").setContent("您确定要清空所有的考试记录吗？")
                .setRightButton("确定", new CommonDialog.RightBtnClickListener() {
                    @Override
                    public void onRightBtnClickListener(View v) {
                        DbHelper.delete(ScoreRecord.class, ScoreRecord_Table.kumu.eq(kemu));
                        setListView();
                        //   ExamDAO.deleteScoreRecord(kemu);
                        //  initDataResourse();
                    }
                })
                .show("");
    }


    private void showChart(PieChart pieChart, PieData pieData) {
        //  pieChart.setHoleColorTransparent(true);
        pieChart.setHoleColor(0x00000000);
        pieChart.setHoleRadius(69f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆
        pieChart.setDescription("");
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(false);  //饼状图中间可以添加文字
        pieChart.setDrawHoleEnabled(true); //空心
        pieChart.setTouchEnabled(false); // 设置是否可以触摸
        pieChart.setDrawCenterText(false);
        pieChart.setRotationAngle(180); // 初始旋转角度
        pieChart.setRotationEnabled(false); // 可以手动旋转
        pieChart.setUsePercentValues(false);  //显示成百分比
        pieChart.setCenterText("");  //饼状图中间的文字
        pieChart.setDrawSliceText(false);
        //设置数据
        pieChart.setData(pieData);
        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setFormSize(0);
        pieChart.animateXY(0, 0);  //设置动画
    }


    /**
     * @param count 依次 做对、未做、做错
     * @return
     */
    private PieData getPieData(float... count) {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        for (int i = 0; i < count.length; i++) {
            //  xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
            xValues.add("");
        }
        ArrayList<Entry> yValues = new ArrayList<>();
        // 饼图数据
        yValues.add(new Entry(count[0], 0));  //做对的
        yValues.add(new Entry(count[1], 2)); //未做的
        yValues.add(new Entry(count[2], 1)); //做错的

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setValueTextSize(0);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(getResources().getColor(R.color.green));
        colors.add(0xFFDEDEDE);
        colors.add(getResources().getColor(R.color.font_red));

        pieDataSet.setColors(colors);
        return new PieData(xValues, pieDataSet);
    }


}
