package com.hxqc.mall.drivingexam.biz;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据库数据
 * Created by zhaofan on 2016/8/5.
 */
public class InitDbData {

    /**
     * 模拟考 答题对错记录
     */
    public static void initRecordDbData(int max) {
        List<ExamRecord> mExamRecordList = new ArrayList<>();
        for (int i = 0; i < max; i++)
            mExamRecordList.add(new ExamRecord(i + 1, "0", "-1", "-1", "-1"));
        DbHelper.saveTransactionFaster(AppDatabase.class, mExamRecordList, ExamRecord.class);
    }

    /**
     * 只看错题 答题对错记录
     */
    public static void initWrongRecordDbData(DiskLruCacheHelper mDiskLruCache) {
        List<ExamRecord> wrongList = new Gson().fromJson(mDiskLruCache.getAsString("wronglist"), new TypeToken<List<ExamRecord>>() {
        }.getType());
        DbHelper.saveTransactionFaster(AppDatabase.class, wrongList, ExamRecord.class);
    }

    /**
     * 我的错题 答题对错记录
     */
    public static void initMyWrongRecordDbData(int mNowPage) {
        DbHelper.delete(ExamRecord.class, ExamRecord_Table.num.eq(mNowPage));
        List<ExamRecord> mOldExamRecordList = DbHelper.query(ExamRecord.class, ExamRecord_Table.num, true);
        DbHelper.delete(ExamRecord.class);
        List<ExamRecord> mNewExamRecordList = new ArrayList<>();
        for (int i = 0; i < mOldExamRecordList.size(); i++) {
            ExamRecord entity = mOldExamRecordList.get(i);
            int newNumber = i < mNowPage - 1 ? entity.num : entity.num - 1;
            mNewExamRecordList.add(new ExamRecord(newNumber, entity.isFinish, entity.choose, entity.answer, entity.isRight));
        }
        for (ExamRecord i : mNewExamRecordList) {
            DebugLog.e("mNewExamRecordList", i.toString());
        }
        DbHelper.saveTransactionFaster(AppDatabase.class, mNewExamRecordList, ExamRecord.class);
    }


}
