package com.hxqc.mall.drivingexam.db.model.wrongsubject;

import com.hxqc.mall.drivingexam.db.utils.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 记录错题
 * Created by zhaofan on 2016/8/2.
 */

@Table(database = AppDatabase.class)
public class WrongA extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public Integer kumu;
    @Column
    public Long dateTag;
    @Column
    public String questionId;
    @Column
    public String question;
    @Column
    public String mediaType;
    @Column
    public String mediaUrl;
    @Column
    public String konwledge;

    public WrongA() {
    }

    public WrongA(Integer kumu, Long dateTag, String questionId, String question, String mediaType, String mediaUrl, String konwledge) {
        this.kumu = kumu;
        this.dateTag = dateTag;
        this.questionId = questionId;
        this.question = question;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.konwledge = konwledge;
    }

    @Override
    public String toString() {
        return "WrongA{" +
                "id=" + id +
                ", kumu=" + kumu +
                ", dateTag=" + dateTag +
                ", questionId='" + questionId + '\'' +
                ", question='" + question + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", konwledge='" + konwledge + '\'' +
                '}';
    }
}
