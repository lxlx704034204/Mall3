package com.hxqc.mall.drivingexam.db.model.wrongsubject;

import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * * 记录错题
 * Created by zhaofan on 2016/8/2.
 */


@Table(database = AppDatabase.class)
public class WrongB extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String questionId;
    @Column
    public String content;
    @Column
    public String choose;

    public WrongB() {
    }

    public WrongB(String questionId, String content, String choose) {
        this.questionId = questionId;
        this.content = content;
        this.choose = choose;
    }

    @Override
    public String toString() {
        return "B{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", content='" + content + '\'' +
                ", choose='" + choose + '\'' +
                '}';
    }
}
