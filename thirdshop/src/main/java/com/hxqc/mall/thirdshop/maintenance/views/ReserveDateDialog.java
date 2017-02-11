package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ReserveDateAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.ReserveTimeAdapter;
import com.hxqc.mall.thirdshop.maintenance.model.ApppintmentDateNew;
import com.hxqc.mall.thirdshop.maintenance.utils.CalendarUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * Des: 预约时间弹窗
 * FIXME
 * Todo
 */
public class ReserveDateDialog implements View.OnClickListener, ReserveDateAdapter.OnDateItemClickListener, ReserveTimeAdapter.OnTimeItemClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    public OnFinishClickListener mOnFinishClickListener;
    public ArrayList<String> mApppintmentDate;
    private RecyclerView mReserveDateListView;
    private RecyclerView mReserveTimeList;
    private ReserveDateView mReserveDateListsView;
    private ReserveDateAdapter reserveDateAdapter;
    private ReserveTimeAdapter reserveTimeAdapter;
    private String date;
    private String time;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View view;
    private AlertDialog.Builder mBuilder;
    private String mDateATime;
    private String mRecommendApppintmentDate;
    private String[] mSplit;
    private String mRecommendApppintmentDateD;
    private String mRecommendApppintmentTime;
    private ArrayList<ApppintmentDateNew> mDateNew;
    private Map<String, List<ApppintmentDateNew>> mDateMap;

    public ReserveDateDialog(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ReserveDateDialog(Context context, ArrayList<String> apppintmentDate) {
        this.mContext = context;
        this.mApppintmentDate = apppintmentDate;
        analysisDate(apppintmentDate);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ReserveDateDialog(Context context, String dateATime) {
        this.mContext = context;
        this.mDateATime = dateATime;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<String> apppintmentDate, String Date) {
        this.mApppintmentDate = apppintmentDate;
        analysisDate(apppintmentDate);
        this.mRecommendApppintmentDate = Date;
        reserveDateAdapter.setData(mDateAndTimes);
        reserveTimeAdapter.setData(mDateAndTimes.get(0));
        if (!TextUtils.isEmpty(Date)) {
            checkDate(Date);
        }
    }

    public void setData(ArrayList<String> apppintmentDate, String recommendApppintmentDate, String chooseDate) {
        this.mApppintmentDate = apppintmentDate;
        analysisDate(apppintmentDate);
        this.mRecommendApppintmentDate = recommendApppintmentDate;
        reserveDateAdapter.setData(mDateAndTimes);
        reserveTimeAdapter.setData(mDateAndTimes.get(0));

        if (TextUtils.isEmpty(chooseDate)) {
            if (!TextUtils.isEmpty(mRecommendApppintmentDate)) {
                checkDate(mRecommendApppintmentDate);
            }
        } else {
            if (CalendarUtil.getDateToMillisecond(chooseDate) >= CalendarUtil.getDateToMillisecond(mApppintmentDate.get(0)) && CalendarUtil.getDateToMillisecond(chooseDate) <= CalendarUtil.getDateToMillisecond(mApppintmentDate.get(mApppintmentDate.size() - 1))) {
                checkDate(chooseDate);
            } else {
                checkDate(mRecommendApppintmentDate);
            }
        }
    }

    /**
     * 修车预约-数据设置
     *
     * @param apppintmentDateNews
     * @param Date
     */
    public void setDataNew(ArrayList<ApppintmentDateNew> apppintmentDateNews, String Date) {
        analysisDateNew(apppintmentDateNews);
        this.mRecommendApppintmentDate = Date;
//        reserveDateAdapter.setData(mDateAndTimes);
//        reserveTimeAdapter.setData(mDateAndTimes.get(0));
        reserveDateAdapter.setDataNew(mDateNew);
        for (int i = 0; i < mDateNew.size(); i++) {
            if (mDateNew.get(i).enable == 1) {
                deflag = i;
                DebugLog.i(AutoInfoContants.LOG_TEST_J, "deflag: " + deflag);
            }
            break;
        }
        DebugLog.i(AutoInfoContants.LOG_TEST_J, "deflag: " + deflag + "----" + mDateNew.get(deflag).time);
        reserveTimeAdapter.setDataNew(mDateMap.get(mDateNew.get(deflag).time));
        if (!TextUtils.isEmpty(Date)) {
            try {
                checkDateNew(Date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int deflag = -1;

    /**
     * 保养-数据设置
     *
     * @param apppintmentDateNews
     * @param recommendApppintmentDate
     * @param chooseDate
     */
    public void setDataNew(ArrayList<ApppintmentDateNew> apppintmentDateNews, String recommendApppintmentDate, String chooseDate) {
        analysisDateNew(apppintmentDateNews);
        this.mRecommendApppintmentDate = recommendApppintmentDate;
//        reserveDateAdapter.setData(mDateAndTimes);
//        reserveTimeAdapter.setData(mDateAndTimes.get(0));
        reserveDateAdapter.setDataNew(mDateNew);
        for (int i = 0; i < mDateNew.size(); i++) {
            if (mDateNew.get(i).enable == 1) {
                deflag = i;
                DebugLog.i(AutoInfoContants.LOG_TEST_J, "deflag: " + deflag);
            }
            break;
        }
        DebugLog.i(AutoInfoContants.LOG_TEST_J, "deflag: " + deflag + "----" + mDateNew.get(deflag).time);
        reserveTimeAdapter.setDataNew(mDateMap.get(mDateNew.get(deflag).time));
        if (TextUtils.isEmpty(chooseDate)) {
            if (!TextUtils.isEmpty(mRecommendApppintmentDate)) {
                checkDateNew(mRecommendApppintmentDate);
            }
        } else {
            if (CalendarUtil.getDateToMillisecond(chooseDate) >= CalendarUtil.getDateToMillisecond(apppintmentDateNews.get(0).time) && CalendarUtil.getDateToMillisecond(chooseDate) <= CalendarUtil.getDateToMillisecond(apppintmentDateNews.get(apppintmentDateNews.size() - 1).time)) {
                checkDateNew(chooseDate);
            } else {
                checkDateNew(mRecommendApppintmentDate);
            }
        }
    }

    @Override
    public void onDateItemClick(View v, String date, int position) {
        this.date = date;
        /*if (reserveTimeAdapter != null && mDateAndTimes != null) {
            reserveTimeAdapter.notifyData(mDateAndTimes.get(position));
        }*/
        if (reserveTimeAdapter != null && mDateMap != null) {
            reserveTimeAdapter.notifyDataNew(mDateMap.get(date));
        }
    }

    @Override
    public void onTimeItemClick(View v, String time) {
        this.time = time;
    }

    public void setOnFinishClickListener(OnFinishClickListener l) {
        this.mOnFinishClickListener = l;
    }

    public void build() {
        view = mLayoutInflater.inflate(R.layout.activity_reserve_date, null);
        initView();
        initEvent();
        mBuilder = new AlertDialog.Builder(mContext, R.style.MMaterialDialog);
    }

    public void create() {
        /*if (mDateAndTimes.size() <= 2) {
            DebugLog.i(TAG, "大于2");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = 3 * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        } else if (mDateAndTimes.size() >= 5) {
            DebugLog.i(TAG, "大于5");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = 5 * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        } else if (mDateAndTimes.size() > 2 && mDateAndTimes.size() < 5) {
            DebugLog.i(TAG, "大于2小于5");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = mDateAndTimes.size() * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        }*/

        if (mDateNew.size() <= 2) {
            DebugLog.i(TAG, "大于2");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = 3 * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        } else if (mDateNew.size() >= 5) {
            DebugLog.i(TAG, "大于5");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = 5 * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        } else if (mDateNew.size() > 2 && mDateNew.size() < 5) {
            DebugLog.i(TAG, "大于2小于5");
            ViewGroup.LayoutParams layoutParams = mReserveDateListsView.getLayoutParams();
            layoutParams.height = mDateNew.size() * ScreenUtil.dip2px(mContext, 58);
            mReserveDateListsView.setLayoutParams(layoutParams);
        }

        mBuilder.setTitle("预约时间");
        mBuilder.setView(view);
        mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                date = reserveDateAdapter.getDefDate();
//                time = reserveTimeAdapter.getDefTime();
//                date = reserveDateAdapter.getDefDateNew();
                time = reserveTimeAdapter.getDefTimeNew();
//                mDateATime = date + " " + time;
                mDateATime = time;
                DebugLog.i(TAG, "Date:" + mDateATime);
                mOnFinishClickListener.onFinishClick(null, mDateATime);
            }
        });
        mBuilder.create();
        mBuilder.show();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reserve_date);
        initView();

        initEvent();
    }*/

    private void initEvent() {

        reserveDateAdapter = new ReserveDateAdapter(mContext);
        mReserveDateListView.setHasFixedSize(true);
        mReserveDateListView.setLayoutManager(new LinearLayoutManager(mContext));
        mReserveDateListView.setAdapter(reserveDateAdapter);

//        mReserveDateListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        reserveTimeAdapter = new ReserveTimeAdapter(mContext);
        mReserveTimeList.setHasFixedSize(true);
        mReserveTimeList.setLayoutManager(new LinearLayoutManager(mContext));
        mReserveTimeList.setAdapter(reserveTimeAdapter);

//        mReserveDateBtnView.setOnClickListener(this);
        reserveDateAdapter.setOnDateItemClickListener(this);
        reserveTimeAdapter.setOnTimeItemClickListener(this);

    }

    private void checkDate(String date) {

        if (date.length() < 11 && date.length() > 0) {
            mRecommendApppintmentDateD = date.substring(0, 10);
        } else if (date.length() > 11) {
            mRecommendApppintmentDateD = date.substring(0, 10);
            mRecommendApppintmentTime = date.substring(11, date.length());
            DebugLog.i(TAG, mRecommendApppintmentDateD + "-----" + mRecommendApppintmentTime);
        }

        int flagDate = -1;
        int flagTime = -1;
        for (int i = 0; i < mDateAndTimes.size(); i++) {
            if (mRecommendApppintmentDateD.equals(mDateAndTimes.get(i).get(0)[0])) {
                DebugLog.i(TAG, "date true");
                flagDate = i;
            }
        }
        if (flagDate != -1) {
            for (int j = 0; j < mDateAndTimes.get(flagDate).size(); j++) {
                if (mRecommendApppintmentTime.equals(mDateAndTimes.get(flagDate).get(j)[1])) {
                    DebugLog.i(TAG, "time true");
                    flagTime = j;
                }
            }
        }

        if (flagDate != -1) {
            reserveDateAdapter.setDefFlag(flagDate);
        }
        if (flagTime != -1) {
            reserveTimeAdapter.setDefFlag(flagTime);
            reserveTimeAdapter.notifyData(mDateAndTimes.get(flagDate));
        }
    }

    private void checkDateNew(String date) {

        if (date.length() < 11 && date.length() > 0) {
            mRecommendApppintmentDateD = date.substring(0, 10);
        } else if (date.length() > 11) {
            mRecommendApppintmentDateD = date.substring(0, 10);
            mRecommendApppintmentTime = date.substring(11, date.length());
            DebugLog.i(TAG, mRecommendApppintmentDateD + "-----" + mRecommendApppintmentTime);
        }

        int flagDate = -1;
        int flagTime = -1;
        for (int i = 0; i < mDateNew.size(); i++) {
            if (mRecommendApppintmentDateD.equals(mDateNew.get(i).time)) {
                DebugLog.i(TAG, "date true");
                flagDate = i;
            }
        }

        if (flagDate != -1) {
//            DebugLog.i(TAG, "mDateNew:" + mDateNew.get(flagDate).time);
//            DebugLog.i(TAG, "mDateMap:" + mDateMap.get(mDateNew.get(flagDate).time).size());
            for (int j = 0; j < mDateMap.get(mDateNew.get(flagDate).time).size(); j++) {
                if (mRecommendApppintmentTime.equals(mDateMap.get(mDateNew.get(flagDate).time).get(j).time.substring(11, mDateMap.get(mDateNew.get(flagDate).time).get(j).time.length()))) {
                    DebugLog.i(TAG, "time true");
                    flagTime = j;
                }
            }
        }

        if (flagDate != -1) {
            reserveDateAdapter.setDefFlag(flagDate);

        }
        if (flagTime != -1) {
            reserveTimeAdapter.setDefFlag(flagTime);
            reserveTimeAdapter.notifyDataNew(mDateMap.get(mDateNew.get(flagDate).time));
        }
    }

    private void initView() {
        mReserveDateListsView = (ReserveDateView) view.findViewById(R.id.reserve_date_lists);
//        mReserveDateBtnView = (Button) view.findViewById(R.id.reserve_date_btn);
        mReserveDateListView = mReserveDateListsView.getLeftList();
        mReserveTimeList = mReserveDateListsView.getRightList();
    }

    @Override
    public void onClick(View v) {
     /*   int i = v.getId();
        if (i == R.id.reserve_date_btn) {
            if (reserveDateAdapter.getDefFlag() == 0 || reserveTimeAdapter.getDefFlag() == 0) {
                date = reserveDateAdapter.getDefDate();
                time = reserveTimeAdapter.getDefTime();
            }
            dateAndTime = date + "," + time;
            mOnFinishClickListener.onFinishClick(v, dateAndTime);
        }*/
    }

    public interface OnFinishClickListener {
        void onFinishClick(View v, String mDateATime);

    }

    //    private ArrayList<List<String>> mDateAndTimes;
    private ArrayList<Integer> flag;
    private ArrayList<String[]> mDateATimes;
    private ArrayList<List<String[]>> mDateAndTimes;

    public void analysisDate(ArrayList<String> apppintmentDate) {

        mDateATimes = new ArrayList<String[]>();
        mDateAndTimes = new ArrayList<List<String[]>>();
        flag = new ArrayList<Integer>();
        for (int i = 0; i < apppintmentDate.size(); i++) {
            String[] split = apppintmentDate.get(i).split(" ");
//            DebugLog.i(TAG, split[0]);
            mDateATimes.add(split);
        }

        for (int i = 0; i < mDateATimes.size(); i++) {
            if (i == 0) {
                flag.add(i);
            }

            if (i < (mDateATimes.size() - 1)) {
                if (!mDateATimes.get(i)[0].equals(mDateATimes.get(i + 1)[0])) {
                    flag.add(i + 1);
                }
            }

            if (i == (mDateATimes.size() - 1)) {
                flag.add(i + 1);
            }
        }

        for (int i = 0; i < flag.size(); i++) {
//            DebugLog.i(TAG, flag.get(i) + "");
            if (i < (flag.size() - 1)) {
                List<String[]> strings1 = mDateATimes.subList(flag.get(i), flag.get(i + 1));
                mDateAndTimes.add(strings1);
            }
        }
/*
        for (int i = 0; i < mDateAndTimes.size(); i++) {
//            DebugLog.i(TAG, mDATs.get(i).size() + "");
            for (int j = 0; j < mDateAndTimes.get(i).size(); j++) {
                DebugLog.i(TAG, mDateAndTimes.get(i).get(j)[0] + "------" + mDateAndTimes.get(i).get(j)[1]);
            }
            DebugLog.i(TAG, "=================");
        }*/

    }

  /*  public void analysisDate(ArrayList<String> apppintmentDate) {
        mDateAndTimes = new ArrayList<List<String>>();
        flag = new ArrayList<Integer>();
        defd(apppintmentDate);
        for (int i = 0; i < apppintmentDate.size(); i++) {
            if (i == 0) {
                flag.add(i);
            }

            if (i < (apppintmentDate.size() - 1)) {

                if (!apppintmentDate.get(i).substring(0, 10).equals(apppintmentDate.get(i + 1).substring(0, 10))) {
                    flag.add(i + 1);
                }
            }

            if (i == (apppintmentDate.size() - 1)) {
                flag.add(i + 1);
            }

        }

        for (int i = 0; i < flag.size(); i++) {
//            DebugLog.i(TAG,flag.get(i)+"");
            if (i < (flag.size() - 1)) {
                List<String> strings = apppintmentDate.subList(flag.get(i), flag.get(i + 1));
                mDateAndTimes.add(strings);
            }
        }

        for (int i = 0; i < mDateAndTimes.size(); i++) {
//            DebugLog.i(TAG, mDateAndTimes.get(i).size() + "");
            for (int j = 0; j < mDateAndTimes.get(i).size(); j++) {
                DebugLog.i(TAG, mDateAndTimes.get(i).get(j));
            }
//            DebugLog.i(TAG, "=================");
        }

    }*/

    private void testMethod(ArrayList<String> apppintmentDate) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < apppintmentDate.size(); i++) {
            boolean add = set.add(apppintmentDate.get(i).substring(0, 10));
//            DebugLog.i(AutoInfoContants.LOG_TEST_J, "add: " + add);
            if (add) {
                DebugLog.i(AutoInfoContants.LOG_TEST_J, "apppintmentDate: " + apppintmentDate.get(i));
            }
        }
        DebugLog.i(AutoInfoContants.LOG_TEST_J, "set: " + set.size());
    }

    /**
     * @param apppintmentDateNews
     */
    public void analysisDateNew(ArrayList<ApppintmentDateNew> apppintmentDateNews) {
        if (mDateMap == null && mDateNew == null) {

            Set<String> set = new HashSet<>();
            ArrayList<Integer> flags = new ArrayList<>();
            ArrayList<ApppintmentDateNew> dateList = new ArrayList<>();
            mDateMap = new HashMap<>();

            for (int i = 0; i < apppintmentDateNews.size(); i++) {
                if (set.add(apppintmentDateNews.get(i).time.substring(0, 10))) {
                    flags.add(i);
                    dateList.add(apppintmentDateNews.get(i));
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, "i-1: " + (i));
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, "apppintmentDateNews: " + apppintmentDateNews.get(i));
                }
            }

            flags.add(apppintmentDateNews.size());

            for (int i = 0; i < flags.size(); i++) {
//            DebugLog.i(AutoInfoContants.LOG_TEST_J, "flags: " + flags.get(i));
                if (i < (flags.size() - 1)) {
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, "dateList: " + dateList.get(i));
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, "interval: " + flags.get(i) + "-----" + flags.get(i + 1));
                    mDateMap.put(dateList.get(i).time.substring(0, 10), apppintmentDateNews.subList(flags.get(i), flags.get(i + 1)));
                }
            }

//        DebugLog.i(AutoInfoContants.LOG_TEST_J, "dateMap: " + dateMap.size());

            Set<Integer> strings = new HashSet<>();
            List<Integer> integers = new ArrayList<>();
            mDateNew = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
//            DebugLog.i(AutoInfoContants.LOG_TEST_J, "dateList: " + dateList.get(i));
                List<ApppintmentDateNew> apppintmentDateNews1 = mDateMap.get(dateList.get(i).time.substring(0, 10));
                if (apppintmentDateNews1 != null && !apppintmentDateNews1.isEmpty()) {
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, i + "apppintmentDateNews1: " + apppintmentDateNews1.size());
                    for (int j = 0; j < apppintmentDateNews1.size(); j++) {
                        if (strings.add(apppintmentDateNews1.get(j).enable)) {
                            integers.add(apppintmentDateNews1.get(j).enable);
                        }
                    }
//                DebugLog.i(AutoInfoContants.LOG_TEST_J, "integers: " + integers.size() + "----" + integers.get(0));
                    if (integers.size() == 1) {
                        if (integers.get(0) == 0) {
                            mDateNew.add(new ApppintmentDateNew(dateList.get(i).time.substring(0, 10), 0));
                        } else if (integers.get(0) == 1) {
                            mDateNew.add(new ApppintmentDateNew(dateList.get(i).time.substring(0, 10), 1));
                        }
                    } else if (integers.size() == 2) {
                        mDateNew.add(new ApppintmentDateNew(dateList.get(i).time.substring(0, 10), 1));
                    }
                    strings.clear();
                    integers.clear();
                }
            }

            /*for (int i = 0; i < mDateNew.size(); i++) {
                DebugLog.i(AutoInfoContants.LOG_TEST_J, "mDateNew: " + mDateNew.get(i).toString());
            }*/
        }
    }
}
