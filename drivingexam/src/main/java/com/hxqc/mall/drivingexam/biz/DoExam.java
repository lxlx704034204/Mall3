package com.hxqc.mall.drivingexam.biz;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.mall.core.util.utils.rxjava.RxSchedulers;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.drivingexam.db.ExamDAO;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.drivingexam.db.model.ExamRecord_Table;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub_Table;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA_Table;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongB;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongB_Table;
import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.hxqc.mall.drivingexam.model.Options;
import com.hxqc.mall.drivingexam.model.QItems;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;


/**
 * Created by zhaofan on 2016/8/5.
 */
public class DoExam {

    /**
     * 保存已做的题
     */
    public static void saveCompleteSubject(int KEMU, String questionId) {
        if (!TextUtils.isEmpty(questionId) &&
                DbHelper.queryEntity(CompleteSub.class, CompleteSub_Table.questionId.eq(questionId)) == null)
            new CompleteSub(KEMU, questionId).save();

    }

    /**
     * 保存错题
     */
    public static void saveWrongSubject(List<QItems> mList, String questionId, int mNowPage, int KEMU, long dateTag) {
        ExamRecord entity = DbHelper.queryEntity(ExamRecord.class, ExamRecord_Table.num.eq(mNowPage + 1));
        if (entity != null && entity.getIsRight().equals("0") && !TextUtils.isEmpty(questionId)) {
            DebugLog.e("做错啦 + " + questionId, mList.get(mNowPage).toString());
            QItems data2 = mList.get(mNowPage);
            if (DbHelper.queryEntity(WrongA.class, WrongA_Table.questionId.eq(questionId)) != null) {
                DbHelper.delete(WrongA.class, WrongA_Table.questionId.eq(questionId));
                DbHelper.delete(WrongB.class, WrongB_Table.questionId.eq(questionId));
            }
            DebugLog.e("做错啦 ", "save the wrong subject");
            WrongA mWrongA = new WrongA(KEMU, dateTag, questionId, data2.getQuestion(), data2.getMediaType(), data2.getMediaUrl(), data2.getKonwledge());
            mWrongA.save();
            List<WrongB> list3 = new ArrayList<>();
            for (Options i : data2.getOptions()) {
                list3.add(new WrongB(questionId, i.content, i.choose));
            }
            DbHelper.saveTransactionFaster(AppDatabase.class, list3, WrongB.class);
        }

    }

    /**
     * 删除错题
     */
    public static Observable<Boolean> deleteWrongSubject(final int KEMU, String questionId) {
        ExamDAO.deleteWrongSubjectById(questionId);
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(DbHelper.query(WrongA.class, WrongA_Table.kumu.eq(KEMU)).isEmpty());
            }
        })
                .delay(0, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Boolean>io_main());


    }


    /**
     * 清除我的错题中做对的题
     */
    public static void autoDeleteWrongSubjectIfRight(int KEMU) {
        List<ExamRecord> mList = DbHelper.query(ExamRecord.class, ExamRecord_Table.isRight.eq("1"));
        List<WrongA> a = DbHelper.query(WrongA.class, WrongA_Table.kumu.eq(KEMU));
        for (ExamRecord i : mList) {
            DebugLog.d("ExamRecord", i.toString());
            ExamDAO.deleteWrongSubjectById(a.get(i.getNum() - 1).questionId);
        }
    }


    /**
     * 我的错题
     */
    public static Observable<List<QItems>> searchMyWrongSubject(final int menu) {
        return Observable.create(new Observable.OnSubscribe<List<QItems>>() {
            @Override
            public void call(Subscriber<? super List<QItems>> subscriber) {
                subscriber.onNext(ExamDAO.queryAllWrongSubject(menu));
            }
        }).flatMap(new Func1<List<QItems>, Observable<List<QItems>>>() {
            @Override
            public Observable<List<QItems>> call(List<QItems> qItemses) {
                if (qItemses.isEmpty())
                    return Observable.error(null);
                else
                    return Observable.just(qItemses);
            }
        })
                .compose(RxSchedulers.<List<QItems>>io_main());
        //   .subscribe(o);

    }


    /**
     * 获取新题
     */
    public static List<String> getNewSubjectId(DiskLruCacheHelper mDiskLruCache, int KEMU) {
        List<String> newQuesionIDList = new ArrayList<>();
        int max = KEMU == 1 ? 100 : 50;
        String key = KEMU == 1 ? C.KEMU_1_AMOUNT : C.KEMU_4_AMOUNT;
        List<String> kemu1_List = str2List(mDiskLruCache.getAsString(key));
        List<String> kemu1_AllList = str2List(mDiskLruCache.getAsString(key));
        if (kemu1_List == null)
            return new ArrayList<>();
        DebugLog.i("kemu1_List", kemu1_List.size() + "");

        List<CompleteSub> completeSubList = DbHelper.query(CompleteSub.class, CompleteSub_Table.kemu.eq(1));
        DebugLog.i("completeSubList", completeSubList.size() + "");
        for (CompleteSub i : completeSubList) {
            kemu1_List.remove(i.getQuestionId());
        }

        DebugLog.e("newlist", kemu1_List.size() + "");
        Collections.shuffle(kemu1_List);

        //kemu1_List 未做题list
        if (kemu1_List.size() < max && kemu1_List.size() > 0) {
            newQuesionIDList = kemu1_List.subList(0, kemu1_List.size());
            kemu1_AllList.removeAll(newQuesionIDList);
            Collections.shuffle(kemu1_AllList);
            newQuesionIDList.addAll(kemu1_AllList.subList(0, max - kemu1_List.size()));
        } else if (kemu1_List.size() == 0) {
        } else if (kemu1_List.size() > max) {
            newQuesionIDList = kemu1_List.subList(0, max);
        }

        DebugLog.i("newlist", newQuesionIDList.size() + "");
        DebugLog.i("newlist", newQuesionIDList.toString().replace(" ", ""));

        return newQuesionIDList;
    }


    private static List<String> str2List(String str) {
        return new Gson().fromJson(str, new TypeToken<List<String>>() {
        }.getType());
    }


}
