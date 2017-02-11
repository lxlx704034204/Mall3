package com.hxqc.mall.drivingexam.biz;

import com.hxqc.mall.drivingexam.db.ExamDAO;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub;
import com.hxqc.mall.drivingexam.db.model.completesubject.CompleteSub_Table;
import com.hxqc.mall.drivingexam.db.model.wrongsubject.WrongA;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.util.utils.rxjava.RxSchedulers;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhaofan on 2016/8/9.
 */
public class ScoreRecordBiz {

    public static Observable<List<CompleteSub>> getScoreRecord(final int kemu, final Action1<List<WrongA>> a1) {
        return Observable
                .create(new Observable.OnSubscribe<List<WrongA>>() {
                    @Override
                    public void call(Subscriber<? super List<WrongA>> subscriber) {
                        subscriber.onNext(ExamDAO.queryWrongQuestionList(kemu));
                    }
                })
                .flatMap(new Func1<List<WrongA>, Observable<List<CompleteSub>>>() {
                    @Override
                    public Observable<List<CompleteSub>> call(List<WrongA> mWrongSubList) {
                        a1.call(mWrongSubList);
                        return Observable.create(new Observable.OnSubscribe<List<CompleteSub>>() {
                            @Override
                            public void call(Subscriber<? super List<CompleteSub>> subscriber) {
                                subscriber.onNext(DbHelper.query(CompleteSub.class, CompleteSub_Table.kemu.eq(kemu)));
                            }
                        });
                    }
                })
                .compose(RxSchedulers.<List<CompleteSub>>io_main());

    }
}
