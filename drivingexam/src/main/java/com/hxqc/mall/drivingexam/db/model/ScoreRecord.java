package com.hxqc.mall.drivingexam.db.model;

import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 成绩记录
 * Created by zhaofan on 2016/8/4.
 */

@Table(database = AppDatabase.class)
public class ScoreRecord extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public Integer kumu;  //科目
    @Column
    public String score;  //得分
    @Column
    public Long time;  //耗时
    @Column
    public String date;  //日期

    public ScoreRecord() {
    }

    public ScoreRecord(Integer kumu, String score, Long time, String date) {
        this.kumu = kumu;
        this.score = score;
        this.time = time;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ScoreRecord{" +
                "id=" + id +
                ", kumu=" + kumu +
                ", score='" + score + '\'' +
                ", time=" + time +
                ", date='" + date + '\'' +
                '}';
    }
}
