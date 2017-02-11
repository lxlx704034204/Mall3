package com.hxqc.mall.drivingexam.db.model.completesubject;

import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 做过的题目
 * Created by zhaofan on 2016/8/3.
 */

@Table(database = AppDatabase.class)
public class CompleteSub extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public Integer kemu;
    @Column
    public String questionId;


    public CompleteSub() {
    }

    public CompleteSub(Integer kemu, String questionId) {
        this.kemu = kemu;
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CompleteSub{" +
                "id=" + id +
                ", kemu=" + kemu +
                ", questionId='" + questionId + '\'' +
                '}';
    }
}
