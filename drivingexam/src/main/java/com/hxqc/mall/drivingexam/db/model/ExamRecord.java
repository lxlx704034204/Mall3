package com.hxqc.mall.drivingexam.db.model;

import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 当次模拟考做题记录
 * Created by zhaofan on 2016/8/1.
 */

@Table(database = AppDatabase.class)
public class ExamRecord extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public Long dateTag;
    @Column
    public int num;     //题数
    @Column
    public String isFinish;  //是否完成
    @Column
    public String choose;  //选择的答案
    @Column
    public String answer; //正确答案
    @Column
    public String isRight;  //是否做对

    public ExamRecord() {
    }

    public ExamRecord(int num, String isFinish, String choose, String answer, String isRight) {
        this.num = num;
        this.choose = choose;
        this.answer = answer;
        this.isRight = isRight;
        this.isFinish = isFinish;
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public String toString() {
        return "ExamRecord{" +
                "id=" + id +
                ", num=" + num +
                ", isFinish='" + isFinish + '\'' +
                ", choose='" + choose + '\'' +
                ", answer='" + answer + '\'' +
                ", isRight='" + isRight + '\'' +
                '}';
    }
}