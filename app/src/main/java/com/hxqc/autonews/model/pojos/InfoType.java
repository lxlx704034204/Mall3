package com.hxqc.autonews.model.pojos;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo 汽车资讯分类
 */
//@Table(database = AutoInformationDataBase.class)
public class InfoType /*extends BaseModel */ {
    //    @Column
    public String guideName;

    public InfoType(String guideName, String guideCode) {
        this.guideName = guideName;
        this.guideCode = guideCode;
    }

    //    @Column
    public String guideCode;

    public ArrayList<InfoTag> tags;

    protected InfoType(Parcel in) {
        guideName = in.readString();
        guideCode = in.readString();
    }

    public InfoType() {
    }
}
