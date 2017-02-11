package com.hxqc.mall.usedcar.views;

import android.support.v4.app.FragmentActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-11-15
 * Copyright:恒信汽车电子商务有限公司
 */

public abstract class UsedCarDatePicker {
    private static final long ONE_YEAR = 1000 * 3600 * 24 * 365L;
    //年检有效期      前30年<现在<后2年
    public static final long INSPECTION_DATE = 2 * ONE_YEAR;
    //交强险          前30年<现在<后1年
    public static final long SALI_DATE = ONE_YEAR;
    //商业保险       前30年<现在<后1年
    public static final long INSURANCE_DATE = ONE_YEAR;
    //质保           前30年<现在<后10年
    public static final long WARRANTY_DATE = 10 * ONE_YEAR;
    //首次上牌       前30年<现在
    public static final long FIRST_ON_CARD = 0L;

    private DatePickerDialog mDatePickerDialog;

    public DatePickerDialog getDatePickerDialog() {
        return mDatePickerDialog;
    }

    protected UsedCarDatePicker(FragmentActivity activity, int year, int monthOfYear, int dayOfMonth, long maxDate) {
        mDatePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String month = monthOfYear + 1 + "";
                String day = dayOfMonth + "";
                if (month.length() == 1) {
                    month = "0" + month;
                }
                if (day.length() == 1) {
                    day = "0" + day;
                }
                doNext(year, monthOfYear, dayOfMonth, month, day);
            }
        }, year, monthOfYear, dayOfMonth);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.setTimeInMillis(System.currentTimeMillis() + maxDate);
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTimeInMillis(System.currentTimeMillis() - 30 * ONE_YEAR);
        mDatePickerDialog.setMaxDate(maxCalendar);
        mDatePickerDialog.setMinDate(minCalendar);
        mDatePickerDialog.show(activity.getFragmentManager(), "DatePickerDialog");
    }

    protected abstract void doNext(int year, int monthOfYear, int dayOfMonth, String month, String day);
}
