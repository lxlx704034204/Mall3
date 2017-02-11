package com.hxqc.mall.drivingexam.db;


import com.hxqc.mall.drivingexam.db.model.ScoreRecord;
import com.hxqc.mall.drivingexam.db.model.ScoreRecord_Table;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub_Table;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA_Table;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongB;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongB_Table;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.drivingexam.model.Options;
import com.hxqc.mall.drivingexam.model.QItems;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofan on 2016/8/4.
 */
public class ExamDAO {

    /**
     * 查询所有错题
     */
    public static List<QItems> queryAllWrongSubject(int kemu) {
        List<QItems> mQItemsList = new ArrayList<>();
        for (WrongA a : queryWrongQuestionList(kemu)) {
            DebugLog.d("WrongA", a.toString());
            List<Options> mOptionsList = new ArrayList<>();
            List<WrongB> list2 = DbHelper.query(WrongB.class, WrongB_Table.questionId.eq(a.questionId));
            for (WrongB b : list2) {
                mOptionsList.add(new Options(b.content, b.choose));
            }
            mQItemsList.add(new QItems(a.question, a.mediaType, mOptionsList, a.mediaUrl, a.konwledge));
        }
        return mQItemsList;
    }

    public static List<WrongA> queryWrongQuestionList(int kemu) {
        return DbHelper.query(WrongA.class, WrongA_Table.kumu.eq(kemu));
    }


    /**
     * 只看错题
     */
    public static List<QItems> queryWrongSubject(long tag) {
        List<QItems> mQItemsList = new ArrayList<>();
        List<WrongA> list = DbHelper.query(WrongA.class, WrongA_Table.dateTag.eq(tag));
        for (WrongA a : list) {
            List<Options> mOptionsList = new ArrayList<>();
            List<WrongB> list2 = DbHelper.query(WrongB.class, WrongB_Table.questionId.eq(a.questionId));
            for (WrongB b : list2) {
                mOptionsList.add(new Options(b.content, b.choose));
            }
            mQItemsList.add(new QItems(a.question, a.mediaType, mOptionsList, a.mediaUrl, a.konwledge));
        }
        return mQItemsList;
    }

    /**
     * 删除错题
     *
     * @param questionId
     */
    public static void deleteWrongSubjectById(String questionId) {
        if (DbHelper.queryEntity(WrongA.class, WrongA_Table.questionId.eq(questionId)) != null) {
            DbHelper.delete(WrongA.class, WrongA_Table.questionId.eq(questionId));
            DbHelper.delete(WrongB.class, WrongB_Table.questionId.eq(questionId));
        }
    }

    /**
     * 删除成绩记录
     *
     * @param kemu
     */
    public static void deleteScoreRecord(int kemu) {
        DbHelper.delete(ScoreRecord.class, ScoreRecord_Table.kumu.eq(kemu));
        DbHelper.delete(CompleteSub.class, CompleteSub_Table.kemu.eq(kemu));
        List<WrongA> listA = DbHelper.query(WrongA.class, WrongA_Table.kumu.eq(kemu));
        for (WrongA i : listA) {
            DbHelper.delete(WrongB.class, WrongB_Table.questionId.eq(i.questionId));
        }
        DbHelper.delete(WrongA.class, WrongA_Table.kumu.eq(kemu));

    }


}
